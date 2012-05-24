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

import java.util.Date;
import java.util.List;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class WorkItem {
	private long workItemId;
	private String workItemName;
	private String workItemType;
	private int currentState;
	private String participant;
	private long limitTime;
	private String activityDefId;
	private long activityInstId;
	private long processInstId;
	private Date startTime;
	private Date endTime;
	private Date finalTime;
	
	private List<Participant> participants; 
	
	public long getWorkItemId() {
		return workItemId;
	}
	public void setWorkItemId(long workItemId) {
		this.workItemId = workItemId;
	}
	public String getWorkItemName() {
		return workItemName;
	}
	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}
	public String getWorkItemType() {
		return workItemType;
	}
	public void setWorkItemType(String workItemType) {
		this.workItemType = workItemType;
	}
	public int getCurrentState() {
		return currentState;
	}
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}
	public String getParticipant() {
		return participant;
	}
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	public long getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(long limitTime) {
		this.limitTime = limitTime;
	}
	public String getActivityDefId() {
		return activityDefId;
	}
	public void setActivityDefId(String activityDefId) {
		this.activityDefId = activityDefId;
	}
	public long getActivityInstId() {
		return activityInstId;
	}
	public void setActivityInstId(long activityInstId) {
		this.activityInstId = activityInstId;
	}
	public long getProcessInstId() {
		return processInstId;
	}
	public void setProcessInstId(long processInstId) {
		this.processInstId = processInstId;
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
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
}
