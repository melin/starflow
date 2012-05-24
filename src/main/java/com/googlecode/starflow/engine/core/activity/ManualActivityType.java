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

package com.googlecode.starflow.engine.core.activity;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.googlecode.starflow.core.key.Keys;
import com.googlecode.starflow.core.util.PrimaryKeyUtil;
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.core.WorkItemModeFactory;
import com.googlecode.starflow.engine.core.workitem.WorkItemMode;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.event.ActivityCreateEvent;
import com.googlecode.starflow.engine.event.ActivityFinishEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.Participant;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.WorkItem;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.model.elements.EventElement;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ManualActivityType extends AbstractActivityType {

	public ActivityInst createActivity(AbstractFlowEvent event, ActivityElement activityXml) {
		String activityDefId = activityXml.getId();
		long processInstId = event.getProcessInstance().getProcessInstId();
		ActivityInst activityInst = this.findWaitingActInst(event, processInstId, activityDefId);
		if(activityInst != null)
			return activityInst;
		
		List<WorkItem> workItems = null;
		ProcessInstance procInst = ((ActivityCreateEvent)event).getProcessInstance();
		
		List<EventElement> events = activityXml.getEvents();
		//执行环节开始时间
		executeBefore(event.getApplicationContext(), events, procInst.getParentProcInstId());
		
		//获取工作项模式
		String mode = activityXml.getWiMode();
		WorkItemMode wiMode = WorkItemModeFactory.buildWorkItemMode(mode);
		workItems = wiMode.createWorkItem(event, activityXml);
		
		activityInst = new ActivityInst();
		activityInst.setActivityDefId(activityDefId);
		activityInst.setActivityType(Constants.ACT_TYPE_MANUL);
		activityInst.setActivityInstName(activityXml.getName());
		activityInst.setDescription(activityXml.getDescription());
		//TODO 添加多种设置环节时限的方式。
		activityInst.setLimitTime(activityXml.getLimitTime());
		activityInst.setCurrentState(StarFlowState.ACT_INST_WAITING);
		activityInst.setCreateTime(new Date());
		activityInst.setProcessInstId(procInst.getProcessInstId());
		
		long actInstId = PrimaryKeyUtil.getPrimaryKey(Keys.ACTIVITYINSTID);
		activityInst.setActivityInstId(actInstId);
		for(WorkItem w : workItems) {
			w.setActivityInstId(actInstId);
		}
		
		activityInst.setWorkItems(workItems);
		
		//保存环节、工作项、参与者数据
		event.getActInstRep().inertActivityInst(activityInst);
		
		for(WorkItem wi : workItems)
			event.getWorkItemRep().insertWorkItem(wi);
		
		for(WorkItem w : workItems) {
			for(Participant participant : w.getParticipants())
				event.getWorkItemRep().insertPaericipant(participant);
		}
		
		return activityInst;
	}
	
	private void executeBefore(ApplicationContext context, List<EventElement> events, long processInstId) {
//		for(EventXml event : events) {
//			if("before".equalsIgnoreCase(event.getOn())) {
//				if("spring".equalsIgnoreCase(event.getType())) {
//					ActivityBeforeEvent beforeEvent = (ActivityBeforeEvent)context.getBean(event.getExpress());
//					beforeEvent.execute(processInstId);
//				} else if("class".equalsIgnoreCase(event.getType())) {
//					try {
//						Class clazz = Class.forName(event.getExpress());
//						ActivityBeforeEvent beforeEvent = (ActivityBeforeEvent)clazz.newInstance();
//						beforeEvent.execute(processInstId);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
	}
	
	public boolean isCompleteActivity(AbstractFlowEvent event, ActivityElement activityXml) {
		String rule = activityXml.getFinishRule();
		long activityInstId = ((ActivityFinishEvent)event).getActivityInst().getActivityInstId();
		boolean isComplete = false;
		
		if(Constants.ACT_WI_FINISHRULE_ALL.equalsIgnoreCase(rule)) {
			int unfinishCount = event.getWorkItemRep().getUnFinishedWorkItemCount(activityInstId);
			if(unfinishCount == 0) 
				isComplete = true; 
		} else if(Constants.ACT_WI_FINISHRULE_NUM.equalsIgnoreCase(rule)) {
			int ruleCount = activityXml.getFinishRquiredNum();
			int finishCount = event.getWorkItemRep().getFinishedWorkItemCount(activityInstId);
			if(ruleCount == finishCount)
				isComplete = true;
		} else if(Constants.ACT_WI_FINISHRULE_PERCENT.equalsIgnoreCase(rule)) {
			int total = event.getWorkItemRep().getWorkItemCount(activityInstId);
			int finishCount = event.getWorkItemRep().getFinishedWorkItemCount(activityInstId);
			
			double c = Double.parseDouble(String.valueOf(finishCount)) / total;

			DecimalFormat df1 = new DecimalFormat("##.0000"); // ##.0000%  百分比格式，后面不足2位的用0补齐
			String d = df1.format(c);
			
			double finishPercent = Double.parseDouble(d)*100;
			
			double mustPercent = activityXml.getFinishRequiredPercent();
			if(finishPercent >= mustPercent)
				isComplete = true;
		}
		return isComplete;
	}
}
