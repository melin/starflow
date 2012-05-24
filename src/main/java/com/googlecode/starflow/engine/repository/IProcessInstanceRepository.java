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

import com.googlecode.starflow.engine.model.ProcessInstance;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface IProcessInstanceRepository {
	public void insertProcessInstance(ProcessInstance processInstance);
	
	public ProcessInstance findProcessInstance(long processInstId);
	
	public void updateProcessStateAndEndTime(long processInstId, int currentState, Date endTime);
	
	public void updateProcessStateAndFinalTime(long processInstId, int currentState, Date finalTime);
	
	public void updateProcessStateAndStartTime(long processInstId, int currentState, Date startTime);
}
