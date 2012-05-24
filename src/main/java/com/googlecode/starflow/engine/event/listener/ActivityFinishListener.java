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

import com.googlecode.starflow.engine.ProcessEngine;
import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.core.ActivityTypeFactory;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.core.activity.ActivityType;
import com.googlecode.starflow.engine.event.ActivityFinishEvent;
import com.googlecode.starflow.engine.event.support.EventUtil;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.model.elements.ProcessElement;
import com.googlecode.starflow.engine.support.TriggerActivityEventUtil;
import com.googlecode.starflow.service.filter.ProcessFilter;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ActivityFinishListener extends AbstractProcessListener {
	
	@Override
	public void activityEnd(ActivityFinishEvent event) {
		ActivityInst activityInst = event.getActivityInst();
		//判断环节是否处于运行状态，才可结束该工作项
		if(StarFlowState.ACT_INST_RUNING != activityInst.getCurrentState())
			throw new ProcessEngineException("当前环节（activityInstId="+activityInst.getActivityInstId()+"）不处于状态，"+
					"无法完成环节的运行！");
		
		Date nowDate = new Date();
		ProcessEngine processEngine = event.getProcessEngine();
		activityInst.setCurrentState(StarFlowState.ACT_INST_COMPLETED);
		activityInst.setEndTime(nowDate);
		
		ProcessElement processElement = event.getProcessElement();
		ActivityElement activityXml = processElement.getActivitys().get(activityInst.getActivityDefId());
		String actType = activityXml.getType();
		
		//判断当前环节是否可以结束
		boolean completeFlag = isCompleteActivity(activityXml, event);
		if(!completeFlag)
			return ;
		
		TriggerActivityEventUtil.beforeComplete(processEngine, activityXml, activityInst, activityXml.getEvents());
		
		//结束当前环节
		event.getActInstRep().updateActivityStateAndEndTime(activityInst.getActivityInstId(), 
				activityInst.getCurrentState(), activityInst.getEndTime());
		
		//未完成工作项自动终止
		if(Constants.ACT_TYPE_MANUL.equalsIgnoreCase(actType) && activityXml.getIsAutoCancel())
			event.getWorkItemRep().updateActWorkItemStateAndFinalTime(activityInst.getActivityInstId(), StarFlowState.WORKITEM_STOPPED, nowDate);
		
		//执行环节结束filter
		for(ProcessFilter filter : processEngine.getProcessFilters()) {
			filter.activityComplete(event);
		}
		
		TriggerActivityEventUtil.afterComplete(processEngine, activityXml, activityInst, activityXml.getEvents());
		
		publishEvent(event, processEngine, actType);
	}

	/**
	 * 如果当前环节是结束环节，发布流程结束事件，否则发布环节开始创建事件
	 * 
	 * @param event
	 * @param processEngine
	 * @param actType
	 */
	private void publishEvent(ActivityFinishEvent event, ProcessEngine processEngine, String actType) {
		if(Constants.ACT_TYPE_END.equalsIgnoreCase(actType)) {
			//如果当前为结束环节，发布流程结束事件
			EventUtil.publishProcessFinishEvent(processEngine, event.getProcessInstance());
		} else {
			//发布环节开始事件
			EventUtil.publishActivityCreateEvent(event, event.getActivityInst());
		}
	}
	
	/**
	 * 判断当前环节是否可以结束，单一工作项，
	 * 
	 * @param activityXml
	 * @param event
	 * @return
	 */
	private boolean isCompleteActivity(ActivityElement activityXml, ActivityFinishEvent event) {
		boolean completeFlag = false;
		if(Constants.ACT_TYPE_MANUL.equalsIgnoreCase(activityXml.getType())) {
			String mode = activityXml.getWiMode();
			if(Constants.WORKITEM_SINGLE.equalsIgnoreCase(mode))
				completeFlag = true;
			else if(Constants.WORKITEM_MULTI.equalsIgnoreCase(mode)) {
				ActivityType type = ActivityTypeFactory.buildActivityType(activityXml.getType());
				completeFlag = type.isCompleteActivity(event, activityXml);
			}
		} else {
			completeFlag = true;
		}
		return completeFlag;
	}
}
