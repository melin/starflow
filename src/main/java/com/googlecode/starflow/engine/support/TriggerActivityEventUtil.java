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

package com.googlecode.starflow.engine.support;

import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.googlecode.starflow.core.util.ApplicationContextHolder;
import com.googlecode.starflow.engine.ExecutorService;
import com.googlecode.starflow.engine.ProcessEngine;
import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.model.elements.EventElement;
import com.googlecode.starflow.service.spi.IActivityTriggerEvent;

/**
 * 流程和环节调用触发事件帮助类。
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class TriggerActivityEventUtil {
	private static Logger logger = LoggerFactory.getLogger(TriggerActivityEventUtil.class);
	
	/**
	 * 环节启动前
	 * 
	 * @param processEngine
	 * @param activityXml
	 * @param activityInst
	 * @param triggerEvents
	 */
	public static void beforeStart(ProcessEngine processEngine, ActivityElement activityXml, ActivityInst activityInst, List<EventElement> triggerEvents) {
		for(final EventElement eventXml : triggerEvents) {
			if(TriggerEventType.BEFORE_START_ACT.equals(eventXml.getEventType())) {
				action(processEngine, activityXml, activityInst, eventXml, new IAction() {
					
					@Override
					public void execute(ActivityElement activityXml, ActivityInst activityInst) {
						IActivityTriggerEvent activityTriggerEvent = 
							(IActivityTriggerEvent)ApplicationContextHolder.getBean(eventXml.getAction());
						activityTriggerEvent.beforeStart(activityInst.getProcessInstId(), activityXml.getId());
					}
				});
			}
		}
	}
	
	/**
	 * 环节启动后
	 * 
	 * @param processEngine
	 * @param activityXml
	 * @param activityInst
	 * @param triggerEvents
	 */
	public static void afterStart(ProcessEngine processEngine, ActivityElement activityXml, ActivityInst activityInst, List<EventElement> triggerEvents) {
		for(final EventElement eventXml : triggerEvents) {
			if(TriggerEventType.AFTER_START_ACT.equals(eventXml.getEventType())) {
				action(processEngine, activityXml, activityInst, eventXml, new IAction() {
					
					@Override
					public void execute(ActivityElement activityXml, ActivityInst activityInst) {
						IActivityTriggerEvent activityTriggerEvent = 
							(IActivityTriggerEvent)ApplicationContextHolder.getBean(eventXml.getAction());
						activityTriggerEvent.afterStart(activityInst.getProcessInstId(), activityInst.getActivityInstId());
					}
				});
			}
		}
	}
	
	/**
	 * 环节完成前
	 * 
	 * @param processEngine
	 * @param activityXml
	 * @param activityInst
	 * @param triggerEvents
	 */
	public static void beforeComplete(ProcessEngine processEngine, ActivityElement activityXml, ActivityInst activityInst, List<EventElement> triggerEvents) {
		for(final EventElement eventXml : triggerEvents) {
			if(TriggerEventType.BEFORE_COMPLETE_ACT.equals(eventXml.getEventType())) {
				action(processEngine, activityXml, activityInst, eventXml, new IAction() {
					
					@Override
					public void execute(ActivityElement activityXml, ActivityInst activityInst) {
						IActivityTriggerEvent activityTriggerEvent = 
							(IActivityTriggerEvent)ApplicationContextHolder.getBean(eventXml.getAction());
						activityTriggerEvent.beforeComplete(activityInst.getProcessInstId(), activityInst.getActivityInstId());
					}
				});
			}
		}
	}
	
	/**
	 * 环节完成后
	 * 
	 * @param processEngine
	 * @param activityXml
	 * @param activityInst
	 * @param triggerEvents
	 */
	public static void afterComplete(ProcessEngine processEngine, ActivityElement activityXml, ActivityInst activityInst, List<EventElement> triggerEvents) {
		for(final EventElement eventXml : triggerEvents) {
			if(TriggerEventType.AFTER_COMPLETE_ACT.equals(eventXml.getEventType())) {
				action(processEngine, activityXml, activityInst, eventXml, new IAction() {
					
					@Override
					public void execute(ActivityElement activityXml, ActivityInst activityInst) {
						IActivityTriggerEvent activityTriggerEvent = 
							(IActivityTriggerEvent)ApplicationContextHolder.getBean(eventXml.getAction());
						activityTriggerEvent.afterComplete(activityInst.getProcessInstId(), activityInst.getActivityInstId());
					}
				});
			}
		}
	}

	/**
	 * 执行触发事件
	 * 
	 * 异步执行触发事件，使用suspend事务策略，忽略异常。
	 * 同步调用
	 *        join事务策略。回滚和忽略异常两种处理方式
	 *        suspend事务策略。回滚和忽略异常两种处理方式
	 * 
	 * @param processEngine
	 * @param activityXml
	 * @param activityInst
	 * @param eventXml
	 * @param action
	 */
	private static void action(ProcessEngine processEngine, final ActivityElement activityXml, final ActivityInst activityInst, 
			EventElement eventXml, final IAction action) {
		String invokePattern = eventXml.getInvokePattern();
		final String transactionType = eventXml.getTransactionType();
		
		ExecutorService executor = processEngine.getExecutorService();
		if(Constants.ACT_AUTO_CALL_SYN.equalsIgnoreCase(invokePattern)) {
			//同步调用可以返回运行结果，前提是要设置返回运行结果。
			try {
				executor.execute(new Callable<Object>() {
					public Object call() throws Exception {
						if(Constants.ACT_TRANSACTION_JOIN.equalsIgnoreCase(transactionType)) {
							action.execute(activityXml, activityInst);
						} else {
							executeLogicInNewTransaction(activityXml, activityInst, action);
						}
						return null;
					}
				}, invokePattern);
			} catch (Exception e) {
				handleException(e, eventXml);
			}
			
		} else {
			//异步执行使用suspend事务。忽略异常
			try {
				executor.execute(new Callable<Object>() {
					public Object call() throws Exception {
						executeLogicInNewTransaction(activityXml, activityInst, action);
						return null;
					}
				}, invokePattern);
			} catch (Exception e) {
				logger.error("自动环节Action执行失败", e);
			}
		}
	}
	
	/**
	 * 
	 * @param e
	 * @param eventXml
	 */
	private static void handleException(Exception e, EventElement eventXml) {
		String exceptionStrategy = eventXml.getExceptionStrategy();
		
		if(Constants.ACT_EXCEPTIONSTRATEGY_ROLLBACK.equals(exceptionStrategy))
			throw new ProcessEngineException("触发事件执行失败", e);
		else {
			logger.error("触发事件执行失败", e);
		}
	}
	
	/**
	 * 挂起当前事务，在一个新事物中执行业务逻辑
	 * 
	 * @param activityXml
	 * @param activityInst
	 * @param action
	 * @return
	 */
	private static void executeLogicInNewTransaction(ActivityElement activityXml, ActivityInst activityInst, IAction action) {
		PlatformTransactionManager txManager = ApplicationContextHolder.getBean(PlatformTransactionManager.class);
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = txManager.getTransaction(definition);
		try {
			action.execute(activityXml, activityInst);
			txManager.commit(status);
		} catch (Exception e) {
			txManager.rollback(status);
			throw new ProcessEngineException("触发事件执行失败", e);
		}
	}
	
	private static interface IAction {
		public void execute(ActivityElement activityXml, ActivityInst activityInst);
	}
}
