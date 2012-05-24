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

package com.googlecode.starflow.engine.event.listener;


import java.util.Date;
import java.util.List;

import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.event.ActivityRestartEvent;
import com.googlecode.starflow.engine.event.support.ActivityStartRuleUtil;
import com.googlecode.starflow.engine.event.support.EventUtil;
import com.googlecode.starflow.engine.model.Participant;
import com.googlecode.starflow.engine.model.WorkItem;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ActivityRestartListener extends AbstractProcessListener {

	@Override
	public void activityRestart(ActivityRestartEvent event) {
		//环节实例状态更新为待激活状态。
		event.getActInstRep().updateActivityStateToRunning(event.getActivityInst().getActivityInstId(), 
				StarFlowState.ACT_INST_WAITING, new Date());
		
		if(!ActivityStartRuleUtil.isStartActivity(event, event.getActivityInst()))
			return;
		
		String type = event.getActivityXml().getType();
		if(Constants.ACT_TYPE_MANUL.equals(type))
			setManualActParticipants(event);
		
		EventUtil.publishActivityStartEvent(event.getProcessEngine(), event.getProcessInstance(), 
				event.getActivityInst(), event.getActivityXml());
	}
	
	//人工环节重启规则：最终参与者
	private void setManualActParticipants(ActivityRestartEvent event) {
		if(Constants.ACT_RESTART_FINAL.equals(event.getActivityXml().getResetParticipant())) {
			event.getWorkItemRep().updateActWorkItemStateAndStartTime(event.getActivityInst().getActivityInstId(), 
					StarFlowState.WORKITEM_RUNNING, new Date());
		} else {
			List<WorkItem> workItems = event.getWorkItemRep().findActivityWorkItems(event.getActivityInst().getActivityInstId());
			for(WorkItem workItem : workItems) {
				int state = StarFlowState.WORKITEM_RUNNING;
				List<Participant> participants = event.getWorkItemRep().findWorkItemParticipants(workItem.getWorkItemId());
				if(participants.size() == 1) {
					Participant participant = participants.get(0);
					if(Constants.PARTICIPANT_TYPE_PERSON.equals(participant.getParticType()))
						state = StarFlowState.WORKITEM_RUNNING;
					else
						state = StarFlowState.WORKITEM_WAITING_RECEIVE;
				} else
					state = StarFlowState.WORKITEM_WAITING_RECEIVE;
				
				event.getWorkItemRep().updateWorkItemStateAndStartTime(workItem.getWorkItemId(), state, new Date());
			}
		}
	}
}
