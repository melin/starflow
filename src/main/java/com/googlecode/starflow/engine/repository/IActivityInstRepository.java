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

import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.TransCtrl;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface IActivityInstRepository {
	
	/**
	 * 环节启动时，保存ActivityInst数据
	 * 
	 * @param activityInst
	 */
	public void inertActivityInst(ActivityInst activityInst);
	
	/**
	 * 环节结束时，保存环节状态和结束时间
	 * 
	 * @param actInstId
	 * @param currentState
	 * @param endTime
	 */
	public void updateActivityStateAndEndTime(long actInstId, int currentState, Date endTime);
	
	/**
	 * 查询环节实例
	 * 
	 * @param activityInstId
	 * @return
	 */
	public ActivityInst findActivityInst(long activityInstId);
	
	/**
	 * 根据环节定义查询环节实例数据，按照降序排序
	 * 
	 * @param activityDefId
	 * @return
	 */
	public ActivityInst findActivityInstByActDefId(String activityDefId);
	
	/**
	 * 根据环节定义查找当前处于待激活的环节
	 * 
	 * @param processInstId
	 * @param activityDefId
	 * @param currentState
	 * @return
	 */
	public ActivityInst findWaitingActivityInst(long processInstId, String activityDefId, int currentState);
	
	/**
	 * 查询某流程实例环节实例状态为终止或运行状态的数据
	 * （主要为演示流程运行监控所用）
	 * 
	 * @param processInstId
	 * @return
	 */
	public List<ActivityInst> findWaitingAndTerminateAndRunningActivityInst(long processInstId);
	
	/**
	 * 流程结束或终止时，终止所有处于运行状态的环节
	 * 
	 * @param processInstId
	 * @param currentState
	 * @param finalTime
	 */
	public void updateProcActivityStateAndFinalTime(long processInstId, int currentState, Date finalTime);
	
	/**
	 * 终止环节实例 或者 自动环节进入异常状态
	 * 
	 * @param activityInstId
	 * @param currentState
	 * @param finalTime
	 */
	public void updateActivityStateAndFinalTime(long activityInstId, int currentState, Date finalTime);
	
	/**
	 * 更新环节从待激活状态为运行状态
	 * 回退重启环节时，更新环节为运行状态
	 * 
	 * @param activityInstId
	 * @param currentState
	 * @param startTime
	 */
	public void updateActivityStateToRunning(long activityInstId, int currentState, Date startTime);
	
	/**
	 * 保存环节运行轨迹
	 * 
	 * @param transCtrl
	 */
	public void insertTransCtrl(TransCtrl transCtrl);
	
	/**
	 * 查询流程实例所有运行轨迹数据
	 * 
	 * @param processInstId
	 * @return java.util.List
	 */
	public List<TransCtrl> findTransCtrls(long processInstId);
	
	/**
	 * 查找某个环节的前驱环节已经运行完成的数目
	 * 
	 * @param processInstId
	 * @param transCtrl
	 * @return String
	 */
	public Integer findFromTransCtrls(long processInstId, String activityDefId);
	
	/**
	 * 如果环节设置为多路或全部聚合，且启动后，设置前驱轨迹IsUse值为: Y
	 * 
	 * @param processInstId
	 * @param activityDefId
	 * @return
	 */
	public void updateTransCtrlIsUse(long processInstId, String activityDefId);
}
