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

package com.googlecode.starflow.core.key.impl;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import com.googlecode.starflow.core.key.CacheValue;
import com.googlecode.starflow.core.key.UniqueException;
import com.googlecode.starflow.core.key.UniqueTableApp;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class DefaultUniqueTableApp implements UniqueTableApp {
	private static final Logger logger = LoggerFactory.getLogger(DefaultUniqueTableApp.class);
	protected String selectSQL = null;
	protected String updateSQL = null;
	protected String insertSQL = null;

	private final int initCode = 0;
	
	protected JdbcTemplate jdbcTemplate = null;
	
	public DefaultUniqueTableApp(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		this.selectSQL = "SELECT code FROM WF_PRIMARYKEY WHERE name = ? FOR UPDATE";
		this.updateSQL = "UPDATE WF_PRIMARYKEY SET code = code + ? WHERE name = ? ";
		this.insertSQL = "INSERT INTO WF_PRIMARYKEY (code, name) VALUES (?, ?)";
	}

	public CacheValue getCacheValue(int cacheNum, String name) {
		Assert.isTrue(TransactionSynchronizationManager.isSynchronizationActive(), 
				"Transaction must be running");

		CacheValue cache = null;
		try {
			cache = getCurrCode(name);
			
			if(cache == null) {
				insert(name);
		        cache = getCurrCode(name);
			}
			
			update(cacheNum, name);
		    cache.setMaxVal(cache.getMinVal() + cacheNum);
		} catch(Exception e) {
			logger.error("获取主键失败", e);
		}
		return cache;
	}
	
	private CacheValue getCurrCode(String name) {
		CacheValue value = null;
		try {
			Long code = jdbcTemplate.queryForLong(this.selectSQL, new Object[] {name});
			value = new CacheValue();
			value.setMinVal(code + 1);
		} catch (EmptyResultDataAccessException e) {
			logger.debug(name + " 没有找到记录");
		} catch (Exception e) {
			throw new UniqueException(name + " 获取主键失败");
		}
		return value;
	}
	
	private void insert(String name) {
		jdbcTemplate.update(this.insertSQL,  new Object[] {initCode, name});
	}
	
	private void update(int cacheNum, String name) {
		jdbcTemplate.update(this.updateSQL,  new Object[] {cacheNum, name});
	}
	
	/**
	 * @param selectSQL the selectSQL to set
	 */
	public void setSelectSQL(String selectSQL) {
		this.selectSQL = selectSQL;
	}

	/**
	 * @param updateSQL the updateSQL to set
	 */
	public void setUpdateSQL(String updateSQL) {
		this.updateSQL = updateSQL;
	}

	/**
	 * @param insertSQL the insertSQL to set
	 */
	public void setInsertSQL(String insertSQL) {
		this.insertSQL = insertSQL;
	}

	/**
	 * @param jdbcTemplate the simpleJdbcTemplate to set
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}