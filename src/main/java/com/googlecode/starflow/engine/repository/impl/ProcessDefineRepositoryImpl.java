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
import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.googlecode.starflow.engine.model.ProcessDefine;
import com.googlecode.starflow.engine.model.elements.ProcessElement;
import com.googlecode.starflow.engine.repository.IProcessDefineRepository;
import com.googlecode.starflow.engine.xml.ProcessDefineParser;

/**
 * 流程定义数据缓存，要注意流程分水岭模式的处理。
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ProcessDefineRepositoryImpl extends JdbcDaoSupport implements IProcessDefineRepository {
	private Cache cache; 
	
	public ProcessDefineRepositoryImpl(Cache cache) {
		this.cache = cache;
	}

	//--------------------------------------------SQL Start--------------------------------------------------------
	private static String inertProcessDefineSQL = "insert into WF_PROCESSDEFINE (processDefId, " +
			"processDefName, processCHName, description, currentState, versionSign, processDefContent, " +
			"createTime, creator, limitTime) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static String updateProcessDefineSQL = "update WF_PROCESSDEFINE set processDefName=?, processCHName=?, " +
			"description=?, versionSign=?, processDefContent=?, updateTime=?, updator=? where processDefId=?";
	
	private static String deleteProcessDefineSQL = "delete from WF_PROCESSDEFINE where processDefId=?";
	
	private static String findProcessDefineSQL = "select * from WF_PROCESSDEFINE where processDefId = ?";
	
	private static String findPublishProcessDefineSQL = "select * from WF_PROCESSDEFINE " +
			"where processDefName = ? and currentState = 3";
	
	private static String updateProcessDefineUnPublishStatusSQL = "update WF_PROCESSDEFINE " +
			"set currentState = 1 where processDefName = ?";
	
	private static String updateProcessDefinePublishStatusSQL = "update WF_PROCESSDEFINE " +
			"set currentState = 3 where processDefId = ?";
	
	private static String findProcessDefinesSQL = "select processDefId, processDefName, processCHName, " +
			"currentState, versionSign  from WF_PROCESSDEFINE where processDefName = ?";
	//--------------------------------------------SQL End--------------------------------------------------------
	
	public void inertProcessDefine(ProcessDefine processDefine) {
		this.getJdbcTemplate().update(inertProcessDefineSQL, processDefine.getProcessDefId(), processDefine.getProcessDefName(), processDefine.getProcessCHName(),
				processDefine.getDescription(), processDefine.getCurrentState(), processDefine.getVersionSign(), processDefine.getProcessDefContent(),
				processDefine.getCreateTime(), processDefine.getCreator(), processDefine.getLimitTime());
	}
	
	public void updateProcessDefine(ProcessDefine processDefine) {
		this.getJdbcTemplate().update(updateProcessDefineSQL, processDefine.getProcessDefName(), processDefine.getProcessCHName(),
				processDefine.getDescription(), processDefine.getVersionSign(), processDefine.getProcessDefContent(),
				processDefine.getUpdateTime(), processDefine.getUpdator(), processDefine.getProcessDefId());
		
		cache.evict("prodef-" + processDefine.getProcessDefId());
		cache.evict("prodef-" + processDefine.getProcessDefName());
	}
	
	public void deleteProcessDefine(long processDefId) {
		this.getJdbcTemplate().update(deleteProcessDefineSQL, processDefId);
		
		ProcessDefine processDefine = getCacheValue("prodef-" + processDefId);
		if(processDefine != null) {
			cache.evict("prodef-" + processDefine.getProcessDefId());
			cache.evict("prodef-" + processDefine.getProcessDefName());
		}
	}
	
	public ProcessDefine findProcessDefine(long processDefId) {
		ProcessDefine processDefine = getCacheValue("prodef-" + processDefId);
		if(processDefine == null || processDefine.getProcessElement() == null) {
			processDefine = this.getJdbcTemplate().queryForObject(findProcessDefineSQL, new ProcessDefineRowMapper(), processDefId);
			
			ProcessElement processElement = ProcessDefineParser.createProcessXml(processDefine.getProcessDefContent());
			processDefine.setProcessElement(processElement);
			cache.put("prodef-" + processDefine.getProcessDefId(), processDefine);
		}
		return processDefine;
	}
	
	public ProcessDefine findPublishProcessDefine(String processDefName) {
		ProcessDefine processDefine = getCacheValue("prodef-" + processDefName);
		if(processDefine == null) {
			processDefine = this.getJdbcTemplate().queryForObject(findPublishProcessDefineSQL, new ProcessDefineRowMapper(), processDefName);
			
			ProcessElement processXml = ProcessDefineParser.createProcessXml(processDefine.getProcessDefContent());
			processDefine.setProcessElement(processXml);
			cache.put("prodef-" + processDefine.getProcessDefId(), processDefine);
			cache.put("prodef-" + processDefine.getProcessDefName(), processDefine);
		}
		return processDefine;
	}
	
	public void updateProcessDefineUnPublishStatus(String processDefName) {
		this.getJdbcTemplate().update(updateProcessDefineUnPublishStatusSQL, processDefName);
		
		ProcessDefine processDefine = getCacheValue("prodef-" + processDefName);
		if(processDefine != null) {
			cache.evict("prodef-" + processDefine.getProcessDefName());
		}
	}
	
	public void updateProcessDefinePublishStatus(long processDefId) {
		this.getJdbcTemplate().update(updateProcessDefinePublishStatusSQL, processDefId);
		
		ProcessDefine processDefine = findProcessDefine(processDefId);
		if(processDefine != null) {
			cache.evict("prodef-" + processDefine.getProcessDefName());
		}
	}
	
	private ProcessDefine getCacheValue(String key) {
		ValueWrapper wrapper = cache.get(key);
		if(wrapper != null)
			return (ProcessDefine)cache.get(key).get();
		else
			return null;
	}
	
	public List<ProcessDefine> findProcessDefines(String processDefName) {
		return this.getJdbcTemplate().query(findProcessDefinesSQL, new RowMapper<ProcessDefine>(){

			@Override
			public ProcessDefine mapRow(ResultSet resultSet, int index)
					throws SQLException {
				ProcessDefine processDefine = new ProcessDefine();
				processDefine.setProcessDefId(resultSet.getLong("processDefId"));
				processDefine.setProcessDefName(resultSet.getString("processDefName"));
				processDefine.setProcessCHName(resultSet.getString("processCHName"));
				processDefine.setCurrentState(resultSet.getInt("currentState"));
				processDefine.setVersionSign(resultSet.getString("versionSign"));
				return processDefine;
			}
			
		}, processDefName);
	}
	
	private static class ProcessDefineRowMapper implements RowMapper<ProcessDefine> {
		@Override
		public ProcessDefine mapRow(ResultSet resultSet, int index)
				throws SQLException {
			ProcessDefine processDefine = new ProcessDefine();
			processDefine.setProcessDefId(resultSet.getLong("processDefId"));
			processDefine.setProcessDefName(resultSet.getString("processDefName"));
			processDefine.setProcessCHName(resultSet.getString("processCHName"));
			processDefine.setCurrentState(resultSet.getInt("currentState"));
			processDefine.setVersionSign(resultSet.getString("versionSign"));
			processDefine.setDescription(resultSet.getString("description"));
			processDefine.setCreateTime(resultSet.getDate("createTime"));
			processDefine.setCreator(resultSet.getString("creator"));
			processDefine.setUpdateTime(resultSet.getDate("updateTime"));
			processDefine.setUpdator(resultSet.getString("updator"));
			processDefine.setLimitTime(resultSet.getLong("limitTime"));
			processDefine.setProcessDefContent(resultSet.getString("processDefContent"));
			return processDefine;
		}
	}
}
