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

package com.googlecode.starflow.service.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.event.ActivityFinishEvent;
import com.googlecode.starflow.engine.event.ActivityStartEvent;
import com.googlecode.starflow.engine.event.ActivityTerminalEvent;
import com.googlecode.starflow.engine.event.ProcessFinishEvent;
import com.googlecode.starflow.engine.event.ProcessStartEvent;
import com.googlecode.starflow.engine.event.ProcessTerminalEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.ProcessInstance;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class LoggerProcessFilter extends ProcessFilterAdapter {
	private Logger logger = LoggerFactory.getLogger(LoggerProcessFilter.class);
	
	@Override
	public void activityComplete(ActivityFinishEvent event) {
		ActivityInst activityInst = event.getActivityInst();
		logger.info("环节【{}】执行完成, 环节实例ID = {}",
				activityInst.getActivityInstName(),
				activityInst.getActivityInstId());
	}
	
	@Override
	public void activityTerminal(ActivityTerminalEvent event) {
		ActivityInst activityInst = event.getActivityInst();
		logger.info("环节【{}】终止成功, 环节实例ID = {}",
				activityInst.getActivityInstName(),
				activityInst.getActivityInstId());
	}
	
	@Override
	public void activityCreate(AbstractFlowEvent event, ActivityInst destActInst) {
		logger.info("环节【{}】创建成功, 环节实例ID = {}",
				destActInst.getActivityInstName(),
				destActInst.getActivityInstId());
	}

	@Override
	public void activityStart(ActivityStartEvent event, ActivityInst destActInst) {
		logger.info("环节【{}】启动成功, 环节实例ID = {}",
				destActInst.getActivityInstName(),
				destActInst.getActivityInstId());
	}

	@Override
	public void processCreate(ProcessInstance processInstance) {
		logger.info("流程【{}】创建成功, 流程实例ID = {}",
				processInstance.getProcessInstName(),
				processInstance.getProcessInstId());
	}

	@Override
	public void processComplete(ProcessFinishEvent event) {
		logger.info("流程【{}】运行完成, 流程实例ID = {}",
				event.getProcessInstance().getProcessInstName(),
				event.getProcessInstance().getProcessInstId());
	}

	@Override
	public void processStart(ProcessStartEvent event) {
		logger.info("流程【{}】启动成功, 流程实例ID = {}",
				event.getProcessInstance().getProcessInstName(),
				event.getProcessInstance().getProcessInstId());
	}
	
	@Override
	public void processTerminal(ProcessTerminalEvent event) {
		logger.info("流程【{}】终止成功, 流程实例ID = {}",
				event.getProcessInstance().getProcessInstName(),
				event.getProcessInstance().getProcessInstId());
	}
}
