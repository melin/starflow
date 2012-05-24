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

package com.googlecode.starflow.engine.model.elements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProcessElement implements Serializable {
	private String name;
	private String version;
	private String chname;
	private long limitTime;
	private String description;
	private List<EventElement> events;
	private Map<String, ActivityElement> activitys;
	private List<TransitionElement> transitions;
	private Map<String, String> properties = new HashMap<String, String>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getChname() {
		return chname;
	}
	public void setChname(String chname) {
		this.chname = chname;
	}
	public long getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(long limitTime) {
		this.limitTime = limitTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Map<String, ActivityElement> getActivitys() {
		return activitys;
	}
	public void setActivitys(Map<String, ActivityElement> activitys) {
		this.activitys = activitys;
	}
	public List<TransitionElement> getTransitions() {
		return transitions;
	}
	public void setTransitions(List<TransitionElement> transitions) {
		this.transitions = transitions;
	}
	public List<EventElement> getEvents() {
		return events;
	}
	public void setEvents(List<EventElement> events) {
		this.events = events;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
