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
public interface ProcessFilter {
	/**
	 * 流程创建
	 * @param processInstance
	 */
	public void processCreate(ProcessInstance processInstance);
	
	/**
	 * 流程开始
	 * @param event
	 */
	public void processStart(ProcessStartEvent event);
	
	/**
	 * 流程运行完成
	 * @param event
	 */
	public void processComplete(ProcessFinishEvent event);
	
	/**
	 * 流程终止
	 * @param event
	 */
	public void processTerminal(ProcessTerminalEvent event);
	
	/**
	 * 环节创建
	 * @param event
	 * @param destActInst
	 */
	public void activityCreate(AbstractFlowEvent event, ActivityInst destActInst);
	
	/**
	 * 环节启动
	 * @param event
	 * @param destActInst
	 */
	public void activityStart(ActivityStartEvent event, ActivityInst destActInst);
	
	/**
	 * 环节终止
	 * @param event
	 */
	public void activityTerminal(ActivityTerminalEvent event);
	
	/**
	 * 环节结束
	 * @param event
	 */
	public void activityComplete(ActivityFinishEvent event);
}
