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

package com.googlecode.starflow.engine.core.participant.impl;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.googlecode.starflow.core.util.ApplicationContextHolder;
import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.core.participant.ParticipantMode;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.model.Participant;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.service.spi.IParticipantService;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ActLogicParticipantMode implements ParticipantMode {

	@SuppressWarnings("unchecked")
	public List<Participant> creatParticipants(AbstractFlowEvent event,
			ActivityElement activityXml) {
		ProcessInstance cloneProcessInstance = new ProcessInstance();
		BeanUtils.copyProperties(event.getProcessInstance(), cloneProcessInstance);
		String beanName = activityXml.getParticiLogic();
		List<Participant> participants;
		try {
			//beanName 名称后面没有指定调用方法时。直接调用IToolAppAction.execute
			int index = beanName.indexOf("#");
			IParticipantService action = ApplicationContextHolder.getBean(beanName, IParticipantService.class);
			if(index == -1) {
				participants = action.createWorkItemParticipants(cloneProcessInstance);
			} else {
				//反射调用bean指定的方法。
				String methodName = beanName.substring(index + 1);
				if("".equals(beanName))
					throw new ProcessEngineException("IParticipantService 实现类Bean："+beanName+"，没有指定方法名称");
				
				beanName = beanName.substring(0, index);
				try {
					Method method = action.getClass().getMethod(methodName, long.class, long.class);
					participants = (List<Participant>)method.invoke(action, cloneProcessInstance);
				} catch (Exception e) {
					throw new ProcessEngineException("IParticipantService 实现类Bean："+beanName+"，没有此方法", e);
				}
			}
		} catch (Exception e) {
			throw new ProcessEngineException("通过业务逻辑获取参与者失败", e);
		}
		
		return participants;
	}

}
