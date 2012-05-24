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

package com.googlecode.starflow.engine.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ActivityInst implements Serializable {
	private long activityInstId;
	private String activityInstName;
	private String description;
	private String activityType;
	private String activityDefId;
	private long processInstId;
	private int currentState;
	private long limitTime;
	private Date createTime;
	private Date startTime;
	private Date endTime;
	private Date finalTime;
	
	List<WorkItem> workItems;
	
	public long getActivityInstId() {
		return activityInstId;
	}
	public void setActivityInstId(long activityInstId) {
		this.activityInstId = activityInstId;
	}
	public String getActivityInstName() {
		return activityInstName;
	}
	public void setActivityInstName(String activityInstName) {
		this.activityInstName = activityInstName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getActivityDefId() {
		return activityDefId;
	}
	public void setActivityDefId(String activityDefId) {
		this.activityDefId = activityDefId;
	}
	public long getProcessInstId() {
		return processInstId;
	}
	public void setProcessInstId(long processInstId) {
		this.processInstId = processInstId;
	}
	public int getCurrentState() {
		return currentState;
	}
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}
	public long getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(long limitTime) {
		this.limitTime = limitTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getFinalTime() {
		return finalTime;
	}
	public void setFinalTime(Date finalTime) {
		this.finalTime = finalTime;
	}
	public List<WorkItem> getWorkItems() {
		return workItems;
	}
	public void setWorkItems(List<WorkItem> workItems) {
		this.workItems = workItems;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
