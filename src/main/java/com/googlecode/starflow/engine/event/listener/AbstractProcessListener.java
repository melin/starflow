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

package com.googlecode.starflow.engine.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;

import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.event.ActivityCreateEvent;
import com.googlecode.starflow.engine.event.ActivityFinishEvent;
import com.googlecode.starflow.engine.event.ActivityRestartEvent;
import com.googlecode.starflow.engine.event.ActivityRollBackEvent;
import com.googlecode.starflow.engine.event.ActivityStartEvent;
import com.googlecode.starflow.engine.event.ActivityTerminalEvent;
import com.googlecode.starflow.engine.event.ProcessFinishEvent;
import com.googlecode.starflow.engine.event.ProcessStartEvent;
import com.googlecode.starflow.engine.event.ProcessTerminalEvent;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public abstract class AbstractProcessListener implements ApplicationListener<ApplicationContextEvent> {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void onApplicationEvent(ApplicationContextEvent event) {
		AbstractFlowEvent _event = null;
		if(event instanceof AbstractFlowEvent)
			_event = (AbstractFlowEvent)event;
		else
			return;
		
		if (_event instanceof ProcessStartEvent)
			processStart((ProcessStartEvent)_event);
		else if (_event instanceof ProcessFinishEvent)
			processEnd((ProcessFinishEvent)_event);
		else if (_event instanceof ActivityCreateEvent)
			activityCreate((ActivityCreateEvent)_event);
		else if (_event instanceof ActivityStartEvent)
			activityStart((ActivityStartEvent)_event);
		else if (_event instanceof ActivityRestartEvent)
			activityRestart((ActivityRestartEvent)_event);
		else if (_event instanceof ActivityFinishEvent)
			activityEnd((ActivityFinishEvent)_event);
		else if (_event instanceof ActivityTerminalEvent)
			activityTerminal((ActivityTerminalEvent)_event);
		else if (_event instanceof ActivityRollBackEvent)
			activityRollback((ActivityRollBackEvent)_event);
		else if (_event instanceof ProcessTerminalEvent)
			processTerminal((ProcessTerminalEvent)_event);
	}
	
	public void processStart(ProcessStartEvent event) {}
	
	public void processEnd(ProcessFinishEvent event) {};
	
	public void activityCreate(ActivityCreateEvent event) {};
	
	public void activityStart(ActivityStartEvent event) {};
	
	public void activityRestart(ActivityRestartEvent event) {};
	
	public void activityEnd(ActivityFinishEvent event) {};
	
	public void activityTerminal(ActivityTerminalEvent event) {};
	
	public void activityRollback(ActivityRollBackEvent event) {};
	
	public void processTerminal(ProcessTerminalEvent event) {};
}
