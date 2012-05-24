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

package com.googlecode.starflow.engine.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

import com.googlecode.starflow.engine.ProcessEngine;
import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.event.ActivityFinishEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.Participant;
import com.googlecode.starflow.engine.model.ProcessDefine;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.WorkItem;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.repository.IActivityInstRepository;
import com.googlecode.starflow.engine.repository.IProcessDefineRepository;
import com.googlecode.starflow.engine.repository.IProcessInstanceRepository;
import com.googlecode.starflow.engine.repository.IWorkItemRepository;
import com.googlecode.starflow.engine.service.IWorkItemService;
import com.googlecode.starflow.engine.transaction.TransactionCallbackWithoutResult;
import com.googlecode.starflow.engine.transaction.TransactionTemplate;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */

public class WorkItemService implements IWorkItemService {
	final private ProcessEngine processEngine;
	
	private IProcessDefineRepository procDefRep;
	private IProcessInstanceRepository procInstRep;
	private IActivityInstRepository actInstRep;
	private final IWorkItemRepository workItemRep;
	private TransactionTemplate transactionTemplate;
	
	public WorkItemService(ProcessEngine processEngine) {
		Assert.notNull(processEngine);
		
		this.processEngine = processEngine;
		
		this.procDefRep = this.processEngine.getApplicationContext().getBean(IProcessDefineRepository.class);
		this.procInstRep = this.processEngine.getApplicationContext().getBean(IProcessInstanceRepository.class);
		this.actInstRep = this.processEngine.getApplicationContext().getBean(IActivityInstRepository.class);
		this.workItemRep = this.processEngine.getApplicationContext().getBean(IWorkItemRepository.class);
		this.transactionTemplate = processEngine.getTransactionTemplate();
	}
	
	/* (non-Javadoc)
	 * @see com.googlecode.starflow.engine.service.IWorkItemService#finishWorkItem(long, java.lang.String)
	 */
	public void finishWorkItem(long workItemId, final String userId) {
		final WorkItem workItem = this.workItemRep.findWorkItem(workItemId);
		final ActivityInst activityInst = this.actInstRep.findActivityInst(workItem.getActivityInstId());
		
		//判断环节是否处于运行状态，才可完成该工作项
		if(StarFlowState.ACT_INST_RUNING != activityInst.getCurrentState())
			throw new ProcessEngineException("当前环节（activityInstId="+activityInst.getActivityInstId()+"）不处于状态，"+
					"无法完成环节的运行！");
		
		//判断工作项是否处于待接收或运行状态，才可完成该工作项
		if(workItem.getCurrentState() != StarFlowState.WORKITEM_WAITING_RECEIVE &&
				workItem.getCurrentState() != StarFlowState.WORKITEM_RUNNING)
			throw new ProcessEngineException("当前工作项（workitemid="+workItemId+"）不处于状态，"+
					"无法完成工作项的运行！");
		
		final ProcessInstance processInstance = this.procInstRep.findProcessInstance(workItem.getProcessInstId());
		final ProcessDefine processDefine = this.procDefRep.findProcessDefine(processInstance.getProcessDefId());
		final ActivityElement activityElement = processDefine.getProcessElement().getActivitys().get(activityInst.getActivityDefId()); 
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				workItemRep.updateWorkItemStateAndEndTime(workItem.getWorkItemId(), userId, StarFlowState.WORKITEM_COMPLETED, new Date());
				
				//发布环节结束事件
				ActivityFinishEvent endEvent = new ActivityFinishEvent(processEngine);
				endEvent.setProcessInstance(processInstance);
				endEvent.setActivityInst(activityInst);
				endEvent.setPreActivityXml(activityElement);
				processEngine.getApplicationContext().publishEvent(endEvent);
			}
		});
		
	}

	/* (non-Javadoc)
	 * @see com.googlecode.starflow.engine.service.IWorkItemService#queryPersonWorkItems(java.lang.String)
	 */
	public List<WorkItem> queryPersonWorkItems(String userid) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.googlecode.starflow.engine.service.IWorkItemService#queryActivityExecutors(long, java.lang.String)
	 */
	public List<Participant> queryActivityExecutors(long processInstId, String activityDefId) {
		List<Participant> participants = new ArrayList<Participant>();
		List<String> users = workItemRep.queryActivityExecutors(processInstId, activityDefId);
		if(users != null) {
			for(String userid : users) {
				Participant participant = new Participant();
				participant.setParticipant(userid);
				participant.setParticType(Constants.PARTICIPANT_TYPE_PERSON);
				participants.add(participant);
			}
		}
		return participants;
	}
}
