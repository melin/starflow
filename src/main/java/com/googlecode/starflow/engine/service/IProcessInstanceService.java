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

package com.googlecode.starflow.engine.service;

import com.googlecode.starflow.engine.model.ProcessInstance;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface IProcessInstanceService {
	/**
	 * 根据流程定义名称创建流程实例 
	 * 
	 * 同一个流程实例有多个版本，但只有一个版本处于发布状态
	 * 
	 * @param processDefName 流程定义名称
	 * @param userId 创建流程实例的用户ID
	 * @return ProcessInstance
	 */
	public ProcessInstance createProcess(String processDefName, String userId);
	
	/**
	 * 内部创建子流程使用
	 * 
	 * @param processDefName
	 * @param userId
	 * @param parentProcInstId
	 * @param activityInstId
	 * @return
	 */
	public ProcessInstance innerCreateSubProcess(String processDefName, String userId, long mainProcInstId,
			long parentProcInstId, long activityInstId);
	
	/**
	 * 根据流程实例ID，启动当前流程实例
	 * 
	 * @param processInstId 流程实例ID
	 */
	public void startProcess(long processInstId);
	
	/**
	 * 创建并启动流程实例ID
	 * 
	 * 同一个流程实例有多个版本，但只有一个版本处于发布状态
	 * 
	 * @param processDefName 流程定义名称
	 * @param userId 创建流程实例的用户ID
	 * @return ProcessInstance
	 */
	public ProcessInstance createAndStartProcess(String processDefName, String userId);
	
	/**
	 * 终止流程实例，状态不可再回复运行，永远over！
	 * 
	 * @param processInstId 流程实例ID
	 */
	public void terminateProcess(long processInstId);
}
