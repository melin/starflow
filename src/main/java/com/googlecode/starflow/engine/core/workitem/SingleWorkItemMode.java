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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.starflow.core.key.Keys;
import com.googlecode.starflow.core.util.PrimaryKeyUtil;
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.event.ActivityCreateEvent;
import com.googlecode.starflow.engine.model.Participant;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.WorkItem;
import com.googlecode.starflow.engine.model.elements.ActivityElement;

/**
 * 单一工作项模式
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class SingleWorkItemMode extends AbstractWorkItemMode {

	public List<WorkItem> createWorkItem(AbstractFlowEvent event, ActivityElement activityXml) {
		ProcessInstance procInst = ((ActivityCreateEvent)event).getProcessInstance();
		List<WorkItem> workItems = new ArrayList<WorkItem>();
		
		List<Participant> participants = getWIParticipants(event, activityXml);
		
		WorkItem workItem = new WorkItem();
		workItem.setWorkItemName(activityXml.getName());
		workItem.setWorkItemType(Constants.WORKITEM_MAN_TYPE);
		int currentState = this.getCurrentState(participants);
		workItem.setCurrentState(currentState);
		if(StarFlowState.WORKITEM_RUNNING == currentState)
			workItem.setParticipant(participants.get(0).getParticipant());
		workItem.setProcessInstId(procInst.getProcessInstId());
		workItem.setActivityDefId(activityXml.getId());
		workItem.setLimitTime(activityXml.getLimitTime());
		workItem.setStartTime(new Date());
		
		long workItemId = PrimaryKeyUtil.getPrimaryKey(Keys.WORKITEMID);
		workItem.setWorkItemId(workItemId);
		
		for(Participant p : participants) {
			long particId = PrimaryKeyUtil.getPrimaryKey(Keys.PARTICIPANTID);
			p.setParticId(particId);
			p.setWorkItemId(workItemId);
		}
		
		workItem.setParticipants(participants);
		workItems.add(workItem);
		return workItems;
	}

}
