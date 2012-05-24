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

import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.event.ActivityFinishEvent;
import com.googlecode.starflow.engine.event.ProcessFinishEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.model.elements.ProcessElement;
import com.googlecode.starflow.engine.support.TriggerProcessEventUtil;
import com.googlecode.starflow.service.filter.ProcessFilter;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ProcessFinishListener extends AbstractProcessListener {
	@Override
	public void processEnd(ProcessFinishEvent event) {
		Date date = new Date();
		ProcessInstance processInstance = event.getProcessInstance();
		long processInstId = processInstance.getProcessInstId();
		
		event.getWorkItemRep().updateProcWorkItemStateAndFinalTime(processInstId, StarFlowState.WORKITEM_STOPPED, date);
		event.getActInstRep().updateProcActivityStateAndFinalTime(processInstId, StarFlowState.ACT_INST_STOPPED, date);
		event.getProcInstFacade().updateProcessStateAndEndTime(processInstId, StarFlowState.PROCESS_INST_COMPLETED, date);
		
		//执行流程完成filter
		for(ProcessFilter filter : event.getProcessEngine().getProcessFilters()) {
			filter.processComplete(event);
		}
		
		//流程完成后触发事件
		TriggerProcessEventUtil.afterComplete(event.getProcessEngine(), null, processInstance, 
				event.getProcessElement().getEvents());
		
		//判断当前流程是否为子流程。
		long actInstId = processInstance.getActivityInstId();
		if(actInstId != 0)
			finishSubFlowAct(event, processInstance);
	}

	/**
	 * 结束子流程环节
	 * 
	 * @param event
	 * @param processInstance
	 */
	private void finishSubFlowAct(ProcessFinishEvent event, ProcessInstance processInstance) {
		ProcessInstance mainProcess = event.getProcInstFacade().findProcessInstance(processInstance.getParentProcInstId());
		ActivityInst activityInst = event.getActInstRep().findActivityInst(processInstance.getActivityInstId());
		//异步子流程，不再需要发布结束事件
		if(StarFlowState.ACT_INST_RUNING == activityInst.getCurrentState()) {
			ActivityFinishEvent endEvent = new ActivityFinishEvent(event.getProcessEngine());
			endEvent.setProcessInstance(mainProcess);
			endEvent.setActivityInst(activityInst);
			
			ProcessElement mainProcessElement =  event.getProcessEngine().getProcessDefineService()
										.findProcessDefine(mainProcess.getProcessDefId()).getProcessElement();
			
			ActivityElement activityXml = mainProcessElement.getActivitys().get(activityInst.getActivityDefId());
			
			endEvent.setPreActivityXml(activityXml);
			
			event.getProcessEngine().getApplicationContext().publishEvent(endEvent);
		}
	}
}
