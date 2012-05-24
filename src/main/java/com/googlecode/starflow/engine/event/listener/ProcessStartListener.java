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
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.core.ActivityTypeFactory;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.core.activity.StartActivityType;
import com.googlecode.starflow.engine.event.ProcessStartEvent;
import com.googlecode.starflow.engine.event.support.EventUtil;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.model.elements.ProcessElement;
import com.googlecode.starflow.engine.support.TriggerProcessEventUtil;
import com.googlecode.starflow.engine.xml.StarFlowNames;
import com.googlecode.starflow.service.filter.ProcessFilter;

/**
 * 流程启动事件监听器。
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ProcessStartListener extends AbstractProcessListener {

	@Override
	public void processStart(ProcessStartEvent event) {
		ProcessInstance processInstance = event.getProcessInstance();
		ProcessEngine processEngine = event.getProcessEngine();
		ProcessElement processElement = event.getProcessElement();
		
		//设置流程实例为运行状态
		event.getProcInstFacade().updateProcessStateAndStartTime(processInstance.getProcessInstId(), 
				StarFlowState.PROCESS_INST_RUNNING, new Date());
		
		//执行流程开始filter
		for(ProcessFilter filter : processEngine.getProcessFilters()) {
			filter.processStart(event);
		}
		
		//流程启动后触发事件
		TriggerProcessEventUtil.afterStart(processEngine, null, processInstance, 
				processElement.getEvents());
		
		StartActivityType type = (StartActivityType)ActivityTypeFactory.buildActivityType(Constants.ACT_TYPE_START);
		ActivityElement activityXml = processElement.getActivitys().get(StarFlowNames.ACT_START_ID);
		ActivityInst activityInst = type.createActivity(event, activityXml);
		
		EventUtil.publishActivityStartEvent(event, activityInst, activityXml);
	}
}
