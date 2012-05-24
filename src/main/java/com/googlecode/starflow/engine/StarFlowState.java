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

package com.googlecode.starflow.engine;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface StarFlowState {
	
	/**
	 * 流程未发布
	 */
	public static int PROCESS_DEF_UNDPUBLISH = 1;
	
	/**
	 * 流程已发布
	 */
	public static int PROCESS_DEF_PUBLISH = 3;
	
	//----------------------------------------------------
	
	/**
	 * 流程实例处于启动状态
	 */
	public static int PROCESS_INST_START = 2;
	
	/**
	 * 流程实例处于终止状态
	 */
	public static int PROCESS_INST_STOPPED = 7;
	
	/**
	 * 流程实例处于运行状态
	 */
	public static int PROCESS_INST_RUNNING = 10;
	
	/**
	 * 流程实例运行完成
	 */
	public static int PROCESS_INST_COMPLETED = 12;
	
	//-----------------------------------------------------
	
	/**
	 * 环节实例处于待激活状态
	 */
	public static int ACT_INST_WAITING = 2;
	
	/**
	 * 环节实例处于运行状态
	 */
	public static int ACT_INST_RUNING = 10;
	
	/**
	 * 环节实例处于终止状态
	 */
	public static int ACT_INST_STOPPED = 7;
	
	/**
	 * 环节实例处于完成状态
	 */
	public static int ACT_INST_COMPLETED = 12;
	
	/**
	 * 应用发生异常
	 */
	public static int ACT_APP_EXCEPTION  = -1;
	
	//------------------------------------------------------
	
	/**
	 * 工作项待接受状态
	 */
	public static int WORKITEM_WAITING_RECEIVE = 4;
	
	/**
	 * 工作项终止状态
	 */
	public static int WORKITEM_STOPPED = 7;
	
	/**
	 * 工作项运行状态
	 */
	public static int WORKITEM_RUNNING = 10;
	
	/**
	 * 工作项完成状态
	 */
	public static int WORKITEM_COMPLETED = 12;
	
}
