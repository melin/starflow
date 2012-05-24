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

package com.googlecode.starflow.engine.event;

import org.springframework.context.ApplicationContext;

import com.googlecode.starflow.engine.ProcessEngine;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.elements.ActivityElement;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ActivityStartEvent extends AbstractFlowEvent {
	private ActivityInst activityInst;
	private ActivityElement activityXml;

	public ActivityStartEvent(ProcessEngine processEngine) {
		this(processEngine.getApplicationContext());
		this.setProcessEngine(processEngine);
	}
	
	private ActivityStartEvent(ApplicationContext source) {
		super(source);
	}

	public ActivityInst getActivityInst() {
		return activityInst;
	}

	public void setActivityInst(ActivityInst activityInst) {
		this.activityInst = activityInst;
	}

	public ActivityElement getActivityXml() {
		return activityXml;
	}

	public void setActivityXml(ActivityElement activityXml) {
		this.activityXml = activityXml;
	}
}