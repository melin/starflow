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

import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.TransCtrl;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface IActivityInstService {
	/**
	 * 结束环节实例
	 * 
	 * @param activityInstId 环节实例ID
	 */
	public void finishActivity(long activityInstId);
	
	/**
	 * 启动指定环节，该环节不处于运行状态。
	 * 
	 * @param processInstId
	 * @param activityDefId
	 */
	public void startActivityInst(long processInstId, String activityDefId);
	
	/**
	 * 终止环节实例
	 * 
	 * @param activityInstId 环节实例ID
	 */
	public void terminateActivity(long activityInstId);
	
	/**
	 * 环节必须运行完成或终止，再重新启动
	 * 
	 * @param activityInstId 环节实例ID
	 * @param processInstId 流程实例ID
	 */
	public void restartActivity(long processInstId, long activityInstId);
	
	/**
	 * 待激活环节设置为启动状态
	 * 
	 * @param activityInstId 环节实例ID
	 */
	public void activateActivity(long activityInstId);
	
	/**
	 * <b>回退到最近的人工活动策略</b>把一个流程实例从当前活动回退到目标活动
	 * <h4>所有符合如下规则的活动都将回退</h4>
	 *	<ul>
	 *		<li>以当前活动为参照，到达最近完成的人工活动的路径上的所有活动</li>
	 *	  	<li>当前活动必须处于运行状态</li>
	 *	  	<li>所有活动流程实例ID与起始活动和目标活动一致</li>
	 *		<li>包含目标活动</li>
	 *	</ul>
	 * <h4>执行动作：是将起始活动结束，执行所有符合第一条规则的活动的回退动作（业务补偿；包含目标活动），重启目标活动</h4>
	 * 
	 * @param activityInstId 环节实例ID
	 */
	public void rollbackToActivityFroRecentManual(long currentActInstID);
	
	/**
	 * <b>单步回退策略</b>把一个流程实例从当前活动回退到目标活动
	 * <h4>所有符合如下规则的活动都将回退： </h4>
	 * 	<ul>
	 * 		<li>以当前活动为参照，所有此活动的上一个活动； </li>
	 * 		<li>当前活动必须处于运行状态； </li>
	 * 		<li>所有活动实例都属于同一个流程实例； </li>
	 * 		<li>包含目标活动。 </li>
	 * </ul>
	 * <h4>执行动作：是将起始活动结束，执行所有符合第一条规则的活动的回退动作（业务补偿；包含目标活动），重启目标活动</h4>
	 * 
	 * @param activityInstId 环节实例ID
	 */
	public void rollbackToActivityFroOneStep(long currentActInstID);
	
	/**
	 * 查询流程实例运行轨迹记录数据
	 * 
	 * @param processInstId
	 * @return
	 */
	public List<TransCtrl> findTransCtrls(long processInstId);
	
	/**
	 * 查询某流程实例环节实例状态为终止或运行状态的数据
	 * （主要为演示流程运行监控所用）
	 * 
	 * @param processInstId
	 * @return
	 */
	public List<ActivityInst> findWaitingAndTerminateAndRunningActivityInst(long processInstId);
}
