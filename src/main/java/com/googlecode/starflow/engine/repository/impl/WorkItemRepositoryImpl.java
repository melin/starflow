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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.googlecode.starflow.engine.model.Participant;
import com.googlecode.starflow.engine.model.WorkItem;
import com.googlecode.starflow.engine.repository.IWorkItemRepository;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class WorkItemRepositoryImpl extends JdbcDaoSupport implements IWorkItemRepository {
	
	private static String insertWorkItemSQL = "insert into WF_WORKITEM(workItemId, workItemName, workItemType, " +
			"currentState, participant, limitTime, activityDefId, activityInstId, processInstId, startTime)" +
			"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static String insertPaericipantSQL = "insert into WF_PARTICIPANT (particId, workItemId, particType, " +
			"participant, participant2) values(?, ?, ?, ?, ?)";
	
	private static String findWorkItemSQL = "select * from WF_WORKITEM where workItemId = ?";
	
	private static String findActivityWorkItemsSQL = "select * from WF_WORKITEM where activityInstId = ?";
	
	private static String findWorkItemParticipantsSQL = "select * from WF_PARTICIPANT where workItemId = ?";
	
	private static String updateWorkItemStateAndEndTimeSQL = "update WF_WORKITEM set currentState = ?, " +
			"endTime = ?, participant = ? where workItemId = ?";
	
	private static String updateProcWorkItemStateAndFinalTimeSQL = "update WF_WORKITEM set currentState = ?, " +
			"finalTime = ? where currentState in (4, 10) and processInstId = ?";
	
	private static String updateActWorkItemStateAndFinalTimeSQL = "update WF_WORKITEM set currentState = ?, " +
			"finalTime = ? where currentState in (4, 10) and activityInstId = ?";
	
	private static String updateActWorkItemStateAndStartTimeSQL = "update WF_WORKITEM set currentState = ?, " +
			"startTime = ? where activityInstId = ?";
	
	private static String updateWorkItemStateAndStartTimeSQL = "update WF_WORKITEM set currentState = ?, " +
			"startTime = ? where workItemId = ?";
	
	private static String getWorkItemCountSQL = "select count(workItemId) count from WF_WORKITEM where activityInstId = ?";
	
	private static String getUnFinishedWorkItemCountSQL = "select count(workItemId) count from WF_WORKITEM " +
			"where activityInstId = ? and currentState in (4, 10)";
	
	private static String getFinishedWorkItemCountSQL = "select count(workItemId) count from WF_WORKITEM " +
			"where activityInstId = ? and currentState=12";
	
	private static String queryActivityExecutorsSQL = "select participant from WF_WORKITEM " +
			"where activityDefId = ? and currentState=12 and processInstId=?";
	
	public void insertWorkItem(WorkItem workItem) {
		this.getJdbcTemplate().update(insertWorkItemSQL, workItem.getWorkItemId(), workItem.getWorkItemName(),
				workItem.getWorkItemType(), workItem.getCurrentState(), workItem.getParticipant(), workItem.getLimitTime(),
				workItem.getActivityDefId(), workItem.getActivityInstId(), workItem.getProcessInstId(), workItem.getStartTime());
	}
	
	public void insertPaericipant(Participant participant) {
		this.getJdbcTemplate().update(insertPaericipantSQL, participant.getParticId(), participant.getWorkItemId(),
				participant.getParticType(), participant.getParticipant(), participant.getParticipant2());
	}
	
	public WorkItem findWorkItem(long workItemId) {
		return (WorkItem)this.getJdbcTemplate().queryForObject(findWorkItemSQL, new WorkItemRowMapper(), workItemId);
	}
	
	public List<WorkItem> findActivityWorkItems(long activityInstId) {
		return this.getJdbcTemplate().query(findActivityWorkItemsSQL, new WorkItemRowMapper(), activityInstId);
	}
	
	public List<Participant> findWorkItemParticipants(long workItemId) {
		return this.getJdbcTemplate().query(findWorkItemParticipantsSQL, new ParticipantRowMapper(), workItemId);
	}
	
	public void updateWorkItemStateAndEndTime(long workItemId, String userid, 
			int currentState, Date endTime) {
		this.getJdbcTemplate().update(updateWorkItemStateAndEndTimeSQL, currentState, endTime, userid, workItemId);
	}
	
	public void updateProcWorkItemStateAndFinalTime(long processInstId, int currentState, Date finalTime) {
		this.getJdbcTemplate().update(updateProcWorkItemStateAndFinalTimeSQL, currentState, finalTime, processInstId);
	}
	
	public void updateActWorkItemStateAndFinalTime(long activityInstId, int currentState, Date finalTime) {
		this.getJdbcTemplate().update(updateActWorkItemStateAndFinalTimeSQL, currentState, finalTime, activityInstId);
	}
	
	public void updateActWorkItemStateAndStartTime(long activityInstId, int currentState, Date startTime) {
		this.getJdbcTemplate().update(updateActWorkItemStateAndStartTimeSQL, currentState, startTime, activityInstId);
	}
	
	public void updateWorkItemStateAndStartTime(long workItemId, int currentState, Date startTime) {
		this.getJdbcTemplate().update(updateWorkItemStateAndStartTimeSQL, currentState, startTime, workItemId);
	}
	
	public Integer getWorkItemCount(long activityInstId) {
		return this.getJdbcTemplate().queryForInt(getWorkItemCountSQL, activityInstId);
	}
	
	public Integer getUnFinishedWorkItemCount(long activityInstId) {
		return this.getJdbcTemplate().queryForInt(getUnFinishedWorkItemCountSQL, activityInstId);
	}
	
	public Integer getFinishedWorkItemCount(long activityInstId) {
		return this.getJdbcTemplate().queryForInt(getFinishedWorkItemCountSQL, activityInstId);
	}
	
	public List<String> queryActivityExecutors(long processInstId, String activityDefId) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("processInstId", processInstId);
		parameter.put("activityDefId", activityDefId);
		return this.getJdbcTemplate().queryForList(queryActivityExecutorsSQL, String.class, activityDefId, processInstId);
	}
	
	private static class WorkItemRowMapper implements RowMapper<WorkItem> {
		@Override
		public WorkItem mapRow(ResultSet resultSet, int index) throws SQLException {
			WorkItem workItem = new WorkItem();
			workItem.setActivityDefId(resultSet.getString("activityDefId"));
			workItem.setActivityInstId(resultSet.getLong("activityInstId"));
			workItem.setCurrentState(resultSet.getInt("currentState"));
			workItem.setEndTime(resultSet.getDate("endTime"));
			workItem.setFinalTime(resultSet.getDate("finalTime"));
			workItem.setLimitTime(resultSet.getLong("limitTime"));
			workItem.setParticipant(resultSet.getString("participant"));
			workItem.setProcessInstId(resultSet.getLong("processInstId"));
			workItem.setStartTime(resultSet.getDate("startTime"));
			workItem.setWorkItemId(resultSet.getLong("workItemId"));
			workItem.setWorkItemName(resultSet.getString("workItemName"));
			workItem.setWorkItemType(resultSet.getString("workItemType"));
			return workItem;
		}
	}
	
	private static class ParticipantRowMapper implements RowMapper<Participant> {
		@Override
		public Participant mapRow(ResultSet resultSet, int index) throws SQLException {
			Participant participant = new Participant();
			participant.setParticId(resultSet.getLong("particId"));
			participant.setParticipant2(resultSet.getString("participant2"));
			participant.setParticipant(resultSet.getString("participant"));
			participant.setParticType(resultSet.getString("particType"));
			participant.setWorkItemId(resultSet.getLong("workItemId"));
			return participant;
		}
	}
}
