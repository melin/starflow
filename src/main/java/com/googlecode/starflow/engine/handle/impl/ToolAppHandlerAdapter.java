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

package com.googlecode.starflow.engine.handle.impl;

import java.lang.reflect.Method;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.googlecode.starflow.core.util.ApplicationContextHolder;
import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.core.RelaDataManagerBuilder;
import com.googlecode.starflow.engine.core.data.RelaDataManager;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.event.ActivityStartEvent;
import com.googlecode.starflow.engine.handle.BaseHandlerAdapter;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.service.spi.IToolAppAction;

/**
 * 执行自动环节业务逻辑
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ToolAppHandlerAdapter extends BaseHandlerAdapter {

	public void action(final ActivityStartEvent event, final ActivityInst activityInst) {
		ActivityElement activityXml = event.getProcessElement().getActivitys().get(activityInst.getActivityDefId());
		String beanName = activityXml.getExecuteAction();
		if(StringUtils.hasText(beanName)) {
			IAction action = new Action(beanName); 
			action(event, activityInst, activityXml, action);
		} else {
			logger.warn("自动环节【{}】没有设置执行逻辑", activityXml.getName());
		}
	}
	
	protected void saveResultRelaData(ActivityStartEvent event, Object result, ActivityElement activityXml) {
		//执行结果放入相关数据区。
		if(result != null) {
			RelaDataManager relaDataManager = RelaDataManagerBuilder.buildRelaDataManager();
			long processInstId = event.getProcessInstance().getProcessInstId();
			String activityDefId = event.getPreActivityXml().getId();
			relaDataManager.bindAutoActLogicResult(processInstId, activityDefId, result);
		}
	}

	private static class Action implements IAction {
		private String beanName;
		
		public Action(String beanName) {
			this.beanName = beanName.trim();
		}

		@Override
		public Object execute(AbstractFlowEvent event, ActivityInst activityInst) {
			ProcessInstance cloneProcessInstance = new ProcessInstance();
			BeanUtils.copyProperties(event.getProcessInstance(), cloneProcessInstance);
			ActivityInst cloneActivityInst = new ActivityInst();
			BeanUtils.copyProperties(activityInst, cloneActivityInst);
			
			try {
				//beanName 名称后面没有指定调用方法时。直接调用IToolAppAction.execute
				int index = beanName.indexOf("#");
				if(index == -1) {
					IToolAppAction action = ApplicationContextHolder.getBean(beanName, IToolAppAction.class);
					return action.execute(cloneProcessInstance, cloneActivityInst);
				} else {
					//反射调用bean指定的方法。
					String methodName = beanName.substring(index + 1);
					if("".equals(beanName))
						throw new ProcessEngineException("IToolAppAction 实现类Bean："+beanName+"，没有指定方法名称");
					
					beanName = beanName.substring(0, index);
					IToolAppAction action = ApplicationContextHolder.getBean(beanName, IToolAppAction.class);
					try {
						Method method = action.getClass().getMethod(methodName, ProcessInstance.class, ActivityInst.class);
						return method.invoke(action, cloneProcessInstance, cloneActivityInst);
					} catch (Exception e) {
						throw new ProcessEngineException("IToolAppAction 实现类Bean："+beanName+"，没有此方法", e);
					}
				}
			} catch (Exception e) {
				throw new ProcessEngineException("自动环节业务逻辑执行失败", e);
			}
		}
	}
}
