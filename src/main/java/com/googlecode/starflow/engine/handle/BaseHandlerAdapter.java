/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.starflow.engine.handle;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.googlecode.starflow.core.util.ApplicationContextHolder;
import com.googlecode.starflow.engine.ExecutorService;
import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.event.ActivityStartEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.transaction.TransactionCallback;
import com.googlecode.starflow.engine.transaction.TransactionTemplate;
import com.googlecode.starflow.service.spi.IApplicationExecptionAction;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public abstract class BaseHandlerAdapter implements IHandlerAdapter {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 执行环节逻辑
	 * 
	 * 异步调用业务逻辑，使用suspend事务策略，忽略异常。
	 * 同步调用
	 *        join事务策略。只能回滚和忽略异常两种处理方式
	 *        suspend事务策略。适合所有处理策略
	 * 
	 * @param event
	 * @param actEl
	 * @param activityXml
	 * @param action
	 * @param activityInst
	 */
	public void action(final ActivityStartEvent event, final ActivityInst activityInst, ActivityElement activityXml, final IAction action) {
		String invokePattern = activityXml.getInvokePattern();
		final String transactionType = activityXml.getTransactionType();
		
		ExecutorService executor = event.getProcessEngine().getExecutorService();
		
		if(Constants.ACT_AUTO_CALL_SYN.equalsIgnoreCase(invokePattern)) {
			Object result = null;
			//同步调用可以返回运行结果，前提是要设置返回运行结果。
			try {
				result = executor.execute(new Callable<Object>() {
					@Override
					public Object call() throws Exception {
						if(Constants.ACT_TRANSACTION_JOIN.equalsIgnoreCase(transactionType)) {
							return action.execute(event, activityInst);
						} else {
							return executeLogicInNewTransaction(event, activityInst, action);
						}
					}
				}, invokePattern);
				
			} catch (Exception e) {
				handleException(e, event, activityXml);
			}
			
			//执行结果放入相关数据区。
			saveResultRelaData(event, result, activityXml);
		} else {
			//异步执行使用suspend事务。忽略异常
			
			try {
				executor.execute(new Callable<Object>() {
					public Object call() throws Exception {
						return executeLogicInNewTransaction(event, activityInst, action);
					}
				}, invokePattern);
			} catch (Exception e) {
				logger.error("自动环节Action执行失败", e);
			}
		}
	}
	
	/**
	 * 执行结果存放到相关数据区。
	 * @param result
	 * @param activityXml
	 */
	protected abstract void saveResultRelaData(ActivityStartEvent event, Object result, ActivityElement activityXml);
	
	/**
	 * 异常处理
	 * 
	 * @param e
	 * @param event
	 * @param activityXml
	 */
	protected void handleException(Exception e, ActivityStartEvent event, ActivityElement activityXml) {
		String exceptionStrategy = activityXml.getExceptionStrategy();
		
		if(Constants.ACT_EXCEPTIONSTRATEGY_ROLLBACK.equals(exceptionStrategy))
			throw new ToolAppRollBackException("自动环节Action执行失败", e);
		else if(Constants.ACT_EXCEPTIONSTRATEGY_INTERRUPT.equals(exceptionStrategy)) {
			long activityInstId = event.getActivityInst().getActivityInstId();
			event.getActInstRep().updateActivityStateAndFinalTime(activityInstId, StarFlowState.ACT_APP_EXCEPTION, new Date());
			throw new InterruptStrategyException();
		} else if(Constants.ACT_EXCEPTIONSTRATEGY_APPLICATION.equals(exceptionStrategy)) {
			String beanName = activityXml.getExceptionAction();
			ProcessInstance cloneProcessInstance = new ProcessInstance();
			BeanUtils.copyProperties(event.getProcessInstance(), cloneProcessInstance);
			ActivityInst cloneActivityInst = new ActivityInst();
			BeanUtils.copyProperties(event.getActivityInst(), cloneActivityInst);
			executeExceptionAction(beanName, e, cloneProcessInstance, cloneActivityInst);	
		} else if(Constants.ACT_EXCEPTIONSTRATEGY_STEPROLLBACK.equals(exceptionStrategy)) { 
			throw new UnsupportedOperationException("单步回退功能没有实现");
	    } else {
			logger.error("自动环节Action执行失败", e);
		}
	}
	
	/**
	 * 自动环节异常处理逻辑
	 * @param beanName
	 * @param exception
	 * @param cloneProcessInstance
	 * @param cloneActivityInst
	 * @return
	 */
	private void executeExceptionAction(String beanName, Exception exception, ProcessInstance cloneProcessInstance, ActivityInst cloneActivityInst) {
		try {
			//beanName 名称后面没有指定调用方法时。直接调用IToolAppAction.execute
			int index = beanName.indexOf("#");
			IApplicationExecptionAction action = ApplicationContextHolder.getBean(beanName, IApplicationExecptionAction.class);
			if(index == -1) {
				action.execute(exception, cloneProcessInstance, cloneActivityInst);
			} else {
				//反射调用bean指定的方法。
				String methodName = beanName.substring(index + 1);
				if("".equals(beanName))
					throw new ProcessEngineException("IApplicationExecptionAction 实现类Bean："+beanName+"，没有指定方法名称");
				
				beanName = beanName.substring(0, index);
				try {
					Method method = action.getClass().getMethod(methodName, long.class, long.class);
					method.invoke(action, exception, cloneProcessInstance, cloneActivityInst);
				} catch (Exception e) {
					throw new ProcessEngineException("IApplicationExecptionAction 实现类Bean："+beanName+"，没有此方法", e);
				}
			}
		} catch (Exception e) {
			throw new ProcessEngineException("自动环节异常处理逻辑执行失败", e);
		}
	}
	
	/**
	 * 挂起当前事务，在一个新事物中执行业务逻辑
	 * 
	 * @param event
	 * @param activityInst
	 * @param action
	 * @return
	 */
	protected Object executeLogicInNewTransaction(final AbstractFlowEvent event, final ActivityInst activityInst, final IAction action) {
		TransactionTemplate template = event.getProcessEngine().getTransactionTemplate();
		
		return template.execute(TransactionDefinition.PROPAGATION_REQUIRES_NEW, 
				new TransactionCallback<Object>() {

			@Override
			public Object doInTransaction(TransactionStatus status) {
				return action.execute(event, activityInst);
			}
		});
	}
	
	protected static interface IAction {
		public Object execute(AbstractFlowEvent event, ActivityInst activityInst);
	}
}
