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

package com.googlecode.starflow.engine.event.support;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.googlecode.starflow.core.util.ApplicationContextHolder;
import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.service.spi.IActivateRuleAction;

public class ActivityStartRuleUtil {
	private static Logger logger = LoggerFactory.getLogger(ActivityStartRuleUtil.class);
	
	/**
	 * 获取环节的激活规则
	 * 
	 * @param event
	 * @return
	 */
	public static boolean isStartActivity(AbstractFlowEvent event, ActivityInst activityInst) {
		boolean isStart = true;
		ActivityElement activityElement = event.getProcessElement().getActivitys().get(activityInst.getActivityDefId());
		
		if(logger.isDebugEnabled()) {
			logger.debug("环节【{}】启动规则：{}", activityElement.getName(), activityElement.getActivateRuleType());
		}
		
		if(Constants.ACT_ACTIVATE_RULE_WAIT.equalsIgnoreCase(activityElement.getActivateRuleType()))
			isStart = false;
		else if(Constants.ACT_ACTIVATE_RULE_LOGIC.equalsIgnoreCase(activityElement.getActivateRuleType())) {
			String beanName = activityElement.getStartStrategybyAppAction();
			
			try {
				ProcessInstance cloneProcessInstance = new ProcessInstance();
				BeanUtils.copyProperties(event.getProcessInstance(), cloneProcessInstance);
				ActivityInst cloneActivityInst = new ActivityInst();
				BeanUtils.copyProperties(activityInst, cloneActivityInst);
				isStart = executeActivateRule(beanName, cloneProcessInstance, cloneActivityInst);
			} catch (Exception e) {
				throw new ProcessEngineException("环节激活规则逻辑失败", e);
			}
		}
			
		return isStart;
	}
	
	/**
	 * 执行环节激活规则逻辑
	 * @param beanName
	 * @param cloneProcessInstance
	 * @param cloneActivityInst
	 * @return
	 */
	private static boolean executeActivateRule(String beanName, ProcessInstance cloneProcessInstance, ActivityInst cloneActivityInst) {
		boolean isStart = true;
		
		try {
			//beanName 名称后面没有指定调用方法时。直接调用IToolAppAction.execute
			int index = beanName.indexOf("#");
			IActivateRuleAction action = ApplicationContextHolder.getBean(beanName, IActivateRuleAction.class);
			if(index == -1) {
				isStart = action.execute(cloneProcessInstance, cloneActivityInst);
			} else {
				//反射调用bean指定的方法。
				String methodName = beanName.substring(index + 1);
				if("".equals(beanName))
					throw new ProcessEngineException("IActivateRuleAction 实现类Bean："+beanName+"，没有指定方法名称");
				
				beanName = beanName.substring(0, index);
				try {
					Method method = action.getClass().getMethod(methodName, long.class, long.class);
					isStart = (Boolean)method.invoke(action, cloneProcessInstance, cloneActivityInst);
				} catch (Exception e) {
					throw new ProcessEngineException("IActivateRuleAction 实现类Bean："+beanName+"，没有此方法", e);
				}
			}
		} catch (Exception e) {
			throw new ProcessEngineException("环节激活规则逻辑执行失败", e);
		}
		return isStart;
	}
}
