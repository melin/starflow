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

import java.util.List;

import com.googlecode.starflow.engine.model.Participant;
import com.googlecode.starflow.engine.model.WorkItem;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface IWorkItemService {
	
	/**
	 * 查询个人工作项
	 * 
	 * @param userid 用户ID
	 * @return
	 */
	public List<WorkItem> queryPersonWorkItems(String userid);
	
	/**
	 * 完成工作项
	 * 
	 * @param workItemId 要完成的工作项ID
	 * @param userid 用户ID
	 */
	public void finishWorkItem(long workItemId, String userId);
	
	/**
	 * 查询环节所有工作项执行人
	 * 
	 * @param processInstId 流程实例
	 * @param activityDefId 环节定义ID
	 * @return List<Participant> 所有执行人ID
	 */
	public List<Participant> queryActivityExecutors(long processInstId, String activityDefId);
}
