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

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.repository.IProcessInstanceRepository;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ProcessInstanceRepositoryImpl extends JdbcDaoSupport implements IProcessInstanceRepository {
	
	private static String insertProcessInstanceSQL = "insert into WF_PROCESSINST (processInstId, processDefId, processInstName, " +
			"creator, currentState, subFlow, limitNum, createTime, mainProcInstId, parentProcInstId, activityInstId) " +
			"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static String findProcessInstanceSQL = "select * from WF_PROCESSINST where processInstId = ?";
	
	private static String updateProcessStateAndEndTimeSQL = "update WF_PROCESSINST set currentState = ?, endTime = ? where processInstId = ?";
	
	private static String updateProcessStateAndFinalTimeSQL = "update WF_PROCESSINST set currentState = ?, finalTime = ? where processInstId = ?";
	
	private static String updateProcessStateAndStartTimeSQL = "update WF_PROCESSINST set currentState = ?, startTime = ? where processInstId = ?";
	
	
	public void insertProcessInstance(ProcessInstance processInstance) {
		this.getJdbcTemplate().update(insertProcessInstanceSQL, processInstance.getProcessInstId(), processInstance.getProcessDefId(),
				processInstance.getProcessInstName(), processInstance.getCreator(), processInstance.getCurrentState(), processInstance.getSubFlow(),
				processInstance.getLimitNum(), processInstance.getCreateTime(), processInstance.getMainProcInstId(), 
				processInstance.getParentProcInstId(), processInstance.getActivityInstId());
	}
	
	public ProcessInstance findProcessInstance(long processInstId) {
		return this.getJdbcTemplate().queryForObject(findProcessInstanceSQL, new RowMapper<ProcessInstance>() {

			@Override
			public ProcessInstance mapRow(ResultSet resultSet, int index)
					throws SQLException {
				ProcessInstance processInstance = new ProcessInstance();
				processInstance.setProcessInstId(resultSet.getLong("processInstId"));
				processInstance.setProcessDefId(resultSet.getLong("processDefId"));
				processInstance.setProcessInstName(resultSet.getString("processInstName"));
				processInstance.setCreator(resultSet.getString("creator"));
				processInstance.setCreateTime(resultSet.getDate("createTime"));
				processInstance.setSubFlow(resultSet.getString("subFlow"));
				processInstance.setLimitNum(resultSet.getLong("limitNum"));
				processInstance.setCurrentState(resultSet.getInt("currentState"));
				processInstance.setMainProcInstId(resultSet.getLong("mainProcInstId"));
				processInstance.setParentProcInstId(resultSet.getLong("parentProcInstId"));
				processInstance.setActivityInstId(resultSet.getLong("activityInstId"));
				return processInstance;
			}
			
		}, processInstId);
	}
	
	public void updateProcessStateAndEndTime(long processInstId, int currentState, Date endTime) {
		this.getJdbcTemplate().update(updateProcessStateAndEndTimeSQL, currentState, endTime, processInstId);
	}
	
	public void updateProcessStateAndFinalTime(long processInstId, int currentState, Date finalTime) {
		this.getJdbcTemplate().update(updateProcessStateAndFinalTimeSQL, currentState, finalTime, processInstId);		
	}
	
	public void updateProcessStateAndStartTime(long processInstId, int currentState, Date startTime) {
		this.getJdbcTemplate().update(updateProcessStateAndStartTimeSQL, currentState, startTime, processInstId);		
	}
}
