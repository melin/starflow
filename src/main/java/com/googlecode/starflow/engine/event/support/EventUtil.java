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

import java.util.List;

import com.googlecode.starflow.engine.ProcessEngine;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.event.ActivityCreateEvent;
import com.googlecode.starflow.engine.event.ActivityFinishEvent;
import com.googlecode.starflow.engine.event.ActivityRestartEvent;
import com.googlecode.starflow.engine.event.ActivityRollBackEvent;
import com.googlecode.starflow.engine.event.ActivityStartEvent;
import com.googlecode.starflow.engine.event.ActivityTerminalEvent;
import com.googlecode.starflow.engine.event.ProcessFinishEvent;
import com.googlecode.starflow.engine.event.ProcessStartEvent;
import com.googlecode.starflow.engine.event.ProcessTerminalEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.support.TriggerProcessEventUtil;
import com.googlecode.starflow.service.filter.ProcessFilter;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class EventUtil {
	
	/**
	 * 发布流程启动事件
	 * 
	 * @param processEngine
	 * @param processInstance
	 */
	public static void publishProcessStartEvent(ProcessEngine processEngine,
			ProcessInstance processInstance) {
		ProcessStartEvent processStartEvent = new ProcessStartEvent(processEngine);
		processStartEvent.setProcessInstance(processInstance);
		processEngine.getApplicationContext().publishEvent(processStartEvent);
	}
	
	/**
	 * 发布流程终止事件
	 * 
	 * @param processEngine
	 * @param processInstance
	 */
	public static void publishProcessTerminalEvent(ProcessEngine processEngine,
			ProcessInstance processInstance) {
		ProcessTerminalEvent processTerminalEvent = new ProcessTerminalEvent(processEngine);
		processTerminalEvent.setProcessInstance(processInstance);
		processEngine.getApplicationContext().publishEvent(processTerminalEvent);
	}
	
	/**
	 * 发布流程结束事件
	 * 
	 * @param processEngine
	 * @param processInstance
	 */
	public static void publishProcessFinishEvent(ProcessEngine processEngine,
			ProcessInstance processInstance) {
		ProcessFinishEvent processEndEvent = new ProcessFinishEvent(processEngine);
		processEndEvent.setProcessInstance(processInstance);
		
		//流程完成前触发事件
		TriggerProcessEventUtil.beforeComplete(processEngine, null, processEndEvent.getProcessInstance(),
				processEndEvent.getProcessElement().getEvents());
		processEngine.getApplicationContext().publishEvent(processEndEvent);
	}
	
	/**
	 * 发布环节创建事件
	 * 
	 * @param event
	 * @param activityInst
	 */
	public static void publishActivityCreateEvent(AbstractFlowEvent event, ActivityInst activityInst) {
		ActivityCreateEvent activityCreateEvent = new ActivityCreateEvent(event.getProcessEngine());
		activityCreateEvent.setProcessInstance(event.getProcessInstance());
		activityCreateEvent.setActivityInst(activityInst);
		activityCreateEvent.setPreActivityXml(event.getPreActivityXml());
		event.getProcessEngine().getApplicationContext().publishEvent(activityCreateEvent);
	}
	
	/**
	 * 发布环节启动事件
	 * 
	 * @param event
	 * @param activityInst
	 * @param activityXml
	 */
	public static void publishActivityStartEvent(AbstractFlowEvent event, ActivityInst activityInst, ActivityElement activityXml) {
		ActivityStartEvent activityStartEvent = new ActivityStartEvent(event.getProcessEngine());
		activityStartEvent.setProcessInstance(event.getProcessInstance());
		activityStartEvent.setActivityInst(activityInst);
		activityStartEvent.setActivityXml(activityXml);
		activityStartEvent.setPreActivityXml(activityXml);
		event.getProcessEngine().getApplicationContext().publishEvent(activityStartEvent);
	}
	
	/**
	 * 发布环节启动事件
	 * 
	 * @param event
	 * @param activityInst
	 * @param activityXml
	 */
	public static void publishActivityStartEvent(ProcessEngine processEngine, ProcessInstance processInstance, 
			ActivityInst activityInst, ActivityElement activityXml) {
		ActivityStartEvent activityStartEvent = new ActivityStartEvent(processEngine);
		activityStartEvent.setProcessInstance(processInstance);
		activityStartEvent.setActivityInst(activityInst);
		activityStartEvent.setActivityXml(activityXml);
		processEngine.getApplicationContext().publishEvent(activityStartEvent);
	}
	
	/**
	 * 发布环节重新启动事件
	 * 
	 * @param event
	 * @param activityInst
	 * @param activityXml
	 */
	public static void publishActivityRestartEvent(ProcessEngine processEngine, ProcessInstance processInstance, 
			ActivityInst activityInst, ActivityElement activityXml) {
		ActivityRestartEvent activityRestartEvent = new ActivityRestartEvent(processEngine);
		activityRestartEvent.setProcessInstance(processInstance);
		activityRestartEvent.setActivityInst(activityInst);
		activityRestartEvent.setActivityXml(activityXml);
		processEngine.getApplicationContext().publishEvent(activityRestartEvent);
	}
	
	/**
	 * 发布环节结束事件
	 * 
	 * @param processEngine
	 * @param processInstance
	 * @param activityInst
	 */
	public static void publishActivityFinishEvent(ActivityStartEvent event, ProcessInstance processInstance, ActivityInst activityInst) {
		ActivityFinishEvent activityEndEvent = new ActivityFinishEvent(event.getProcessEngine());
		activityEndEvent.setProcessInstance(processInstance);
		activityEndEvent.setActivityInst(activityInst);
		activityEndEvent.setPreActivityXml(event.getPreActivityXml());
		event.getProcessEngine().getApplicationContext().publishEvent(activityEndEvent);
	}
	
	/**
	 * 发布环节终止事件
	 * 
	 * @param processEngine
	 * @param processInstance
	 * @param activityInst
	 */
	public static void publishActivityTerminalEvent(ProcessEngine processEngine, ProcessInstance processInstance, ActivityInst activityInst) {
		ActivityTerminalEvent activityTerminalEvent = new ActivityTerminalEvent(processEngine);
		activityTerminalEvent.setProcessInstance(processInstance);
		activityTerminalEvent.setActivityInst(activityInst);
		processEngine.getApplicationContext().publishEvent(activityTerminalEvent);
	}
	
	/**
	 * 发布环节回滚事件
	 * 
	 * @param processEngine
	 * @param processInstance
	 * @param activityInst
	 * @param actDefIds
	 */
	public static void publishActivityRollBackEvent(ProcessEngine processEngine, ProcessInstance processInstance, 
			ActivityInst activityInst, List<String> actDefIds) {
		ActivityRollBackEvent activityRollBackEvent = new ActivityRollBackEvent(processEngine);
		activityRollBackEvent.setProcessInstance(processInstance);
		activityRollBackEvent.setActivityInst(activityInst);
		activityRollBackEvent.setActDefIds(actDefIds);
		processEngine.getApplicationContext().publishEvent(activityRollBackEvent);
	}
	
	/**
	 *  
	 * @param event
	 * @param actInst
	 */
	public static void triggerFilterExecuter(AbstractFlowEvent event, ActivityInst activityInst) {
		//执行环节创建filter
		for(ProcessFilter filter : event.getProcessEngine().getProcessFilters()) {
			filter.activityCreate(event, activityInst);
		}
	}
}
