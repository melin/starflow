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

package com.googlecode.starflow.engine.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.TransCtrl;
import com.googlecode.starflow.engine.repository.IActivityInstRepository;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ActivityInstRepositoryImpl extends JdbcDaoSupport implements IActivityInstRepository {
	
	//--------------------------------------------SQL Start--------------------------------------------------------
	private static String inertActivityInstSQL = "insert into WF_ACTIVITYINST(activityInstId, activityInstName, " +
			"activityType, activityDefId, processInstId, limitTime, currentState, createTime, description) " +
			"values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static String updateActivityStateAndEndTimeSQL = "update WF_ACTIVITYINST set currentState=?, " +
			"endTime=? where activityInstId = ?";
	
	private static String findActivityInstSQL = "select * from WF_ACTIVITYINST where activityInstId = ?";
	
	private static String findActivityInstByActDefIdSQL = "select * from WF_ACTIVITYINST where activityDefId = ? " +
			"order by activityInstId desc";
	
	private static String findWaitingActivityInstSQL = "select * from WF_ACTIVITYINST where processInstId = ? " +
			"and activityDefId = ? and currentState = ?";
	
	private static String findWaitingAndTerminateAndRunningActivityInstSQL = "select * from WF_ACTIVITYINST " +
			"where currentState in (2,7,10) and processInstId = ?";
	
	private static String updateProcActivityStateAndFinalTimeSQL = "update WF_ACTIVITYINST set currentState = ?, " +
			"finalTime = ? where currentState = 10 and processInstId = ?";
	
	private static String updateActivityStateAndFinalTimeSQL = "update WF_ACTIVITYINST set currentState = ?, " +
			"finalTime = ? where activityInstId = ?";
	
	private static String updateActivityStateToRunningSQL = "update WF_ACTIVITYINST set currentState = ?, " +
			"startTime = ? where activityInstId = ?";
	
	private static String insertTransCtrlSQL = "insert into WF_TRANSCTRL(transCtrlId, srcActDefId, srcActDefName, " +
			"srcActType, destActDefId, destActDefName, destActType, transTime, processInstId, isUse, isStartDestAct) " +
			"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static String findTransCtrlsSQL = "select * from WF_TRANSCTRL where processInstId = ? order by transCtrlId";
	
	private static String findFromTransCtrlsSQL = "select count(*) from WF_TRANSCTRL where isUse='N' and " +
			"destActDefId = ? and processInstId = ?";
	
	private static String updateTransCtrlIsUseSQL = "update WF_TRANSCTRL set isUse = 'Y' where destActDefId = ? and processInstId = ?";
	
	//--------------------------------------------SQL End--------------------------------------------------------
	
	public void inertActivityInst(ActivityInst activityInst) {
		this.getJdbcTemplate().update(inertActivityInstSQL, activityInst.getActivityInstId(), 
				activityInst.getActivityInstName(),activityInst.getActivityType(), 
				activityInst.getActivityDefId(), activityInst.getProcessInstId(), activityInst.getLimitTime(),
				activityInst.getCurrentState(), activityInst.getCreateTime(), activityInst.getDescription());
	}
	
	public void updateActivityStateAndEndTime(long actInstId, int currentState, Date endTime) {
		this.getJdbcTemplate().update(updateActivityStateAndEndTimeSQL, currentState, endTime, actInstId);
	}
	
	public ActivityInst findActivityInst(long activityInstId) {
		return this.getJdbcTemplate().queryForObject(findActivityInstSQL, 
				new ActivityInstRowMapper(), activityInstId);
	}
	
	public ActivityInst findActivityInstByActDefId(String activityDefId) {
		return this.getJdbcTemplate().queryForObject(findActivityInstByActDefIdSQL, 
				new ActivityInstRowMapper(), activityDefId);
	}
	
	public ActivityInst findWaitingActivityInst(long processInstId, String activityDefId, int currentState) {
		try{
			return this.getJdbcTemplate().queryForObject(findWaitingActivityInstSQL, 
					new ActivityInstRowMapper(), processInstId, activityDefId, currentState);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<ActivityInst> findWaitingAndTerminateAndRunningActivityInst(long processInstId) {
		return this.getJdbcTemplate().query(findWaitingAndTerminateAndRunningActivityInstSQL, 
				new ActivityInstRowMapper(), processInstId);
	}
	
	public void updateProcActivityStateAndFinalTime(long processInstId, int currentState, Date finalTime) {
		this.getJdbcTemplate().update(updateProcActivityStateAndFinalTimeSQL, currentState, finalTime, processInstId);
	}
	
	public void updateActivityStateAndFinalTime(long activityInstId, int currentState, Date finalTime) {
		this.getJdbcTemplate().update(updateActivityStateAndFinalTimeSQL, currentState, finalTime, activityInstId);
	}
	
	public void updateActivityStateToRunning(long activityInstId, int currentState, Date startTime) {
		this.getJdbcTemplate().update(updateActivityStateToRunningSQL, currentState, startTime, activityInstId);
	}
	
	public void insertTransCtrl(TransCtrl transCtrl) {
		this.getJdbcTemplate().update(insertTransCtrlSQL, transCtrl.getTransCtrlId(), transCtrl.getSrcActDefId(), transCtrl.getSrcActDefName(),
				transCtrl.getSrcActType(), transCtrl.getDestActDefId(), transCtrl.getDestActDefName(), transCtrl.getDestActType(),
				transCtrl.getTransTime(), transCtrl.getProcessInstId(), transCtrl.getIsUse(), transCtrl.getIsStartDestAct());
	}
	
	public List<TransCtrl> findTransCtrls(long processInstId) {
		return this.getJdbcTemplate().query(findTransCtrlsSQL, new TransCtrlRowMapper(), processInstId);
	}
	
	public Integer findFromTransCtrls(long processInstId, String activityDefId) {
		return this.getJdbcTemplate().queryForInt(findFromTransCtrlsSQL, activityDefId, processInstId);
	}
	
	public void updateTransCtrlIsUse(long processInstId, String activityDefId) {
		this.getJdbcTemplate().update(updateTransCtrlIsUseSQL, activityDefId, processInstId);
	}
	
	private static class ActivityInstRowMapper implements RowMapper<ActivityInst> {
		@Override
		public ActivityInst mapRow(ResultSet resultSet, int index)
				throws SQLException {
			ActivityInst activityInst = new ActivityInst();
			activityInst.setActivityInstId(resultSet.getLong("activityInstId"));
			activityInst.setActivityDefId(resultSet.getString("activityDefId"));
			activityInst.setActivityInstName(resultSet.getString("activityInstName"));
			activityInst.setActivityType(resultSet.getString("activityType"));
			activityInst.setCreateTime(resultSet.getDate("createTime"));
			activityInst.setCurrentState(resultSet.getInt("currentState"));
			activityInst.setDescription(resultSet.getString("description"));
			activityInst.setEndTime(resultSet.getDate("endTime"));
			activityInst.setFinalTime(resultSet.getDate("finalTime"));
			activityInst.setLimitTime(resultSet.getLong("limitTime"));
			activityInst.setProcessInstId(resultSet.getLong("processInstId"));
			activityInst.setStartTime(resultSet.getDate("startTime"));
			return activityInst;
		}
	}
	
	private static class TransCtrlRowMapper implements RowMapper<TransCtrl> {
		@Override
		public TransCtrl mapRow(ResultSet resultSet, int index) 
				throws SQLException {
			TransCtrl transCtrl = new TransCtrl();
			transCtrl.setDestActDefId(resultSet.getString("destActDefId"));
			transCtrl.setDestActDefName(resultSet.getString("destActDefName"));
			transCtrl.setDestActType(resultSet.getString("destActType"));
			transCtrl.setSrcActDefId(resultSet.getString("srcActDefId"));
			transCtrl.setSrcActDefName(resultSet.getString("srcActDefName"));
			transCtrl.setSrcActType(resultSet.getString("srcActType"));
			transCtrl.setIsStartDestAct(resultSet.getString("isStartDestAct"));
			transCtrl.setIsUse(resultSet.getString("isUse"));
			transCtrl.setProcessInstId(resultSet.getLong("processInstId"));
			transCtrl.setTransCtrlId(resultSet.getLong("transCtrlId"));
			transCtrl.setTransTime(resultSet.getDate("transTime"));
			return transCtrl;
		}
	}
}
