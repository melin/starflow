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

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProcessInstance implements Serializable {
	private long processInstId;
	private long processDefId;
	private String processInstName;
	private String creator;
	private int currentState;
	private String subFlow; //N：非子流程，Y：是子流程
	private long limitNum;
	private long mainProcInstId;
	private long parentProcInstId;
	private long activityInstId;
	private Date createTime;
	private Date startTime;
	private Date endTime;
	private Date finalTime;
	
	public long getProcessInstId() {
		return processInstId;
	}
	public void setProcessInstId(long processInstId) {
		this.processInstId = processInstId;
	}
	public long getProcessDefId() {
		return processDefId;
	}
	public void setProcessDefId(long processDefId) {
		this.processDefId = processDefId;
	}
	public String getProcessInstName() {
		return processInstName;
	}
	public void setProcessInstName(String processInstName) {
		this.processInstName = processInstName;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public int getCurrentState() {
		return currentState;
	}
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}
	public String getSubFlow() {
		return subFlow;
	}
	public void setSubFlow(String subFlow) {
		this.subFlow = subFlow;
	}
	public long getLimitNum() {
		return limitNum;
	}
	public void setLimitNum(long limitNum) {
		this.limitNum = limitNum;
	}
	public long getMainProcInstId() {
		return mainProcInstId;
	}
	public void setMainProcInstId(long mainProcInstId) {
		this.mainProcInstId = mainProcInstId;
	}
	public long getParentProcInstId() {
		return parentProcInstId;
	}
	public void setParentProcInstId(long parentProcInstId) {
		this.parentProcInstId = parentProcInstId;
	}
	public long getActivityInstId() {
		return activityInstId;
	}
	public void setActivityInstId(long activityInstId) {
		this.activityInstId = activityInstId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	
	
	//--------------------------hashCode and equals-------------------------------
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (processDefId ^ (processDefId >>> 32));
		result = prime * result
				+ (int) (processInstId ^ (processInstId >>> 32));
		result = prime * result
				+ ((processInstName == null) ? 0 : processInstName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessInstance other = (ProcessInstance) obj;
		if (processDefId != other.processDefId)
			return false;
		if (processInstId != other.processInstId)
			return false;
		if (processInstName == null) {
			if (other.processInstName != null)
				return false;
		} else if (!processInstName.equals(other.processInstName))
			return false;
		return true;
	}
}
