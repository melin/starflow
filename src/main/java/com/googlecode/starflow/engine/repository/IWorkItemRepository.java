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

package com.googlecode.starflow.engine.repository;

import java.util.Date;
import java.util.List;

import com.googlecode.starflow.engine.model.Participant;
import com.googlecode.starflow.engine.model.WorkItem;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface IWorkItemRepository {
	
	/**
	 * 保存工作项信息
	 * 
	 * @param workItem
	 */
	public void insertWorkItem(WorkItem workItem);
	
	/**
	 * 保存参与者
	 * 
	 * @param participant
	 */
	public void insertPaericipant(Participant participant);
	
	/**
	 * 根据工作ID，查询工作项信息
	 * 
	 * @param workItemId
	 * @return
	 */
	public WorkItem findWorkItem(long workItemId);
	
	/**
	 * 查询环节实例的所有工作项信息
	 * 
	 * @param activityInstId
	 * @return
	 */
	public List<WorkItem> findActivityWorkItems(long activityInstId);
	
	/**
	 * 查询工作项的所有参与者信息
	 * 
	 * @param workItemId
	 * @return
	 */
	public List<Participant> findWorkItemParticipants(long workItemId);
	
	/**
	 * 工作项完成时，更新工作项状态。
	 * 
	 * @param workItemId
	 * @param userid
	 * @param currentState
	 * @param endTime
	 */
	public void updateWorkItemStateAndEndTime(long workItemId, String userid, int currentState, Date endTime);
	
	/**
	 * 流程结束或终止时，终止所有处于运行状态的的工作项
	 * 
	 * @param processInstId
	 * @param currentState
	 * @param finalTime
	 */
	public void updateProcWorkItemStateAndFinalTime(long processInstId, int currentState, Date finalTime);
	
	/**
	 * 环节结束或终止时，终止所有处于运行状态的的工作项
	 * 
	 * @param processInstId
	 * @param currentState
	 * @param finalTime
	 */
	public void updateActWorkItemStateAndFinalTime(long activityInstId, int currentState, Date finalTime);
	
	/**
	 * 回退后，环节重启更新运行状态和开始时间
	 * 
	 * @param activityInstId
	 * @param currentState
	 * @param startTime
	 */
	public void updateActWorkItemStateAndStartTime(long activityInstId, int currentState, Date startTime);
	
	/**
	 * 环节重启更新运行状态和开始时间
	 * 
	 * @param workItemId
	 * @param currentState
	 * @param startTime
	 */
	public void updateWorkItemStateAndStartTime(long workItemId, int currentState, Date startTime);
	
	/**
	 * 查询指定环节所有工作项数目
	 * 
	 * @param activityInstId
	 * @return
	 */
	public Integer getWorkItemCount(long activityInstId);
	
	/**
	 * 查询指定环节未完成工作项数目
	 * 
	 * @param activityInstId
	 * @return
	 */
	public Integer getUnFinishedWorkItemCount(long activityInstId);
	
	/**
	 * 查询指定环节已完成工作项数目
	 * 
	 * @param activityInstId
	 * @return
	 */
	public Integer getFinishedWorkItemCount(long activityInstId);
	
	/**
	 * 查询环节所有工作项执行人
	 * 
	 * @param processInstId 流程实例
	 * @param activityDefId 环节定义ID
	 * @return List<String> 所有执行人ID
	 */
	public List<String> queryActivityExecutors(long processInstId, String activityDefId);
}
