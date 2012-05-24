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

package com.googlecode.starflow.engine.core.workitem;

import java.util.LinkedList;
import java.util.List;

import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.core.ParticipantModeFactory;
import com.googlecode.starflow.engine.core.participant.ParticipantMode;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.model.Participant;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.service.spi.IWFOMService;

abstract public class AbstractWorkItemMode implements WorkItemMode {
	/**
	 * 判断工作项状态
	 * 
	 * @param participants
	 * @return
	 */
	protected int getCurrentState(List<Participant> participants) {
		if(participants.size()>1)
			return StarFlowState.WORKITEM_WAITING_RECEIVE;
		else {
			Participant participant = participants.get(0);
			if(Constants.PARTICIPANT_TYPE_PERSON.equalsIgnoreCase(participant.getParticType()))
				return StarFlowState.WORKITEM_RUNNING;
			else 
				return StarFlowState.WORKITEM_WAITING_RECEIVE;
		}
	}

	/**
	 * 获取环节参与者
	 */
	protected List<Participant> getWIParticipants(AbstractFlowEvent event, ActivityElement activityXml) {
		List<Participant> participants = null;
		
		String mode = activityXml.getParticipantType();
		ParticipantMode particiMode = ParticipantModeFactory.buildParticipantMode(mode);
		participants = particiMode.creatParticipants(event, activityXml);
		
		if(participants ==null || participants.size() == 0) {
			String actName = activityXml.getName();
			throw new ProcessEngineException("【" + actName + "】环节指定的参与为空，不能启动环节");
		}
		
		return participants;
	}
	
	/**
	 * 获取环节参与者, 如果参与者为机构或角色，转换为机构和较色下的所有人员。
	 * 为了避免与具体的组织机构相关联，调用IWFOMService相关实现去获取数据。
	 */
	protected List<Participant> getWIParticipantsExt(AbstractFlowEvent event, ActivityElement activityXml) {
		List<Participant> _particis = null;
		List<Participant> participants = new LinkedList<Participant>();
		
		IWFOMService service = (IWFOMService)event.getApplicationContext().getBean("WFOMService");
		
		String mode = activityXml.getParticipantType();
		ParticipantMode particiMode = ParticipantModeFactory.buildParticipantMode(mode);
		_particis = particiMode.creatParticipants(event, activityXml);
		for(Participant p : _particis) {
			participants.addAll(service.getParticipants(p.getParticType(), p.getParticipant()));
		}
		
		if(participants ==null || participants.size() == 0) {
			String actName = activityXml.getName();
			throw new ProcessEngineException("【" + actName + "】环节指定的参与为空，不能启动流程");
		}
		
		return participants;
	}
}