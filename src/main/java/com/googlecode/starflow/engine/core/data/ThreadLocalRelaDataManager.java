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

package com.googlecode.starflow.engine.core.data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.Assert;

import com.googlecode.starflow.engine.model.Participant;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
@SuppressWarnings({"unchecked", "unused"})
public class ThreadLocalRelaDataManager implements RelaDataManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ThreadLocalRelaDataManager.class);
	
	private static final ThreadLocal<List<String>> nextFreeActs =
		new NamedThreadLocal<List<String>>("Freee Activity");
	
	private static final ThreadLocal<List<Participant>> nextActParticipants =
		new NamedThreadLocal<List<Participant>>("Next Activity Participant");
	
	private static final ThreadLocal<Map<Object, Object>> resources =
		new NamedThreadLocal<Map<Object, Object>>("Activity resources");
	
	private static final String AUTO_ACT_LOGIC_RESULT = "autoActLogicResult";
	private static final String TRANSITION_EXPRESS_CONDITION = "transitionExpressCondition";
	private static final String MESSAGE_PARAMETER = "messageParameter";


	@Override
	public Map<String, Object> getExpressConditions(long processInstId,
			String activityDefId) {
		return (Map<String, Object>) getResource(TRANSITION_EXPRESS_CONDITION);
	}

	@Override
	public void setExpressConditions(long processInstId, String activityDefId,
			Map<String, Object> conditions) {
		bindResource(TRANSITION_EXPRESS_CONDITION, conditions);
	}

	@Override
	public void bindAutoActLogicResult(long processInstId,
			String activityDefId, Object value) {
		bindResource(AUTO_ACT_LOGIC_RESULT, value);
	}

	@Override
	public Object getAutoActLogicResult(long processInstId, String activityDefId) {
		return getResource(AUTO_ACT_LOGIC_RESULT);
	}

	@Override
	public void setMessageParameters(long processInstId, String activityDefId,
			Map<String, Object> parameters) {
		bindResource(MESSAGE_PARAMETER, parameters);
	}

	@Override
	public Map<String, Object> getMessageParameters(long processInstId,
			String activityDefId) {
		return (Map<String, Object>) getResource(MESSAGE_PARAMETER);
	}

	@Override
	public void setNextFreeActs(long processInstId, String activityDefId,
			List<String> nextActs) {
		nextFreeActs.set(nextActs);
	}

	@Override
	public List<String> getNextFreeActs(long processInstId, String activityDefId) {
		return nextFreeActs.get();
	}

	@Override
	public void setNextActParticipants(long processInstId,
			String activityDefId, List<Participant> participants) {
		nextActParticipants.set(participants);
	}

	@Override
	public List<Participant> getNextActParticipant(long processInstId,
			String activityDefId) {
		return nextActParticipants.get();
	}

	private Object getResource(Object key) {
		Object value = doGetResource(key);
		if (value != null && logger.isTraceEnabled()) {
			logger.trace("Retrieved value [" + value + "] for key [" + key + "] bound to thread [" +
					Thread.currentThread().getName() + "]");
		}
		return value;
	}
	
	private Object doGetResource(Object key) {
		Map<Object, Object> map = resources.get();
		if (map == null) {
			return null;
		}
		Object value = map.get(key);

		return value;
	}
	
	private void bindResource(Object key, Object value) throws IllegalStateException {
		Assert.notNull(value, "Value must not be null");
		Map<Object, Object> map = resources.get();

		if (map == null) {
			map = new ConcurrentHashMap<Object, Object>();
			resources.set(map);
		}
		if (map.put(key, value) != null) {
			logger.trace("Retry bound value [" + value + "] for key [" + key + "] to thread [" +
					Thread.currentThread().getName() + "]");
		}
		if (logger.isTraceEnabled()) {
			logger.trace("Bound value [" + value + "] for key [" + key + "] to thread [" +
					Thread.currentThread().getName() + "]");
		}
	}

	private Object unbindResource(Object key) throws IllegalStateException {
		Object value = doUnbindResource(key);
		if (value == null) {
			throw new IllegalStateException(
					"No value for key [" + key + "] bound to thread [" + Thread.currentThread().getName() + "]");
		}
		return value;
	}
	
	private Object doUnbindResource(Object actualKey) {
		Map<Object, Object> map = resources.get();
		if (map == null) {
			return null;
		}
		Object value = map.remove(actualKey);
		// Remove entire ThreadLocal if empty...
		if (map.isEmpty()) {
			resources.set(null);
		}
		if (value != null && logger.isTraceEnabled()) {
			logger.trace("Removed value [" + value + "] for key [" + actualKey + "] from thread [" +
					Thread.currentThread().getName() + "]");
		}
		return value;
	}
	
	public void clear() {
		nextFreeActs.set(null);
		nextActParticipants.set(null);
		resources.set(null);
	}
}
