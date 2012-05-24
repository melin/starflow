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

import com.googlecode.starflow.engine.model.Participant;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class NoSqlRelaDataManager implements RelaDataManager {

	@Override
	public Map<String, Object> getExpressConditions(long processInstId,
			String activityDefId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setExpressConditions(long processInstId, String activityDefId,
			Map<String, Object> conditions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bindAutoActLogicResult(long processInstId,
			String activityDefId, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getAutoActLogicResult(long processInstId, String activityDefId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMessageParameters(long processInstId, String activityDefId,
			Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getMessageParameters(long processInstId,
			String activityDefId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNextFreeActs(long processInstId, String activityDefId,
			List<String> nextActs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getNextFreeActs(long processInstId, String activityDefId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNextActParticipants(long processInstId,
			String activityDefId, List<Participant> nextActs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Participant> getNextActParticipant(long processInstId,
			String activityDefId) {
		// TODO Auto-generated method stub
		return null;
	}

}
