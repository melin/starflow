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

import com.googlecode.starflow.engine.model.elements.ProcessElement;

/**
 *  
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProcessDefine implements Serializable {
	private long processDefId;
	private String processDefName;
	private String processCHName;
	private String description;
	private int currentState;
	private long limitTime;
	private String versionSign;
	private String processDefContent;
	private Date createTime;
	private String creator;
	private Date updateTime;
	private String updator;
	
	private ProcessElement processElement;
	
	public long getProcessDefId() {
		return processDefId;
	}
	public void setProcessDefId(long processDefId) {
		this.processDefId = processDefId;
	}
	public String getProcessDefName() {
		return processDefName;
	}
	public void setProcessDefName(String processDefName) {
		this.processDefName = processDefName;
	}
	public String getProcessCHName() {
		return processCHName;
	}
	public void setProcessCHName(String processCHName) {
		this.processCHName = processCHName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCurrentState() {
		return currentState;
	}
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}
	public String getVersionSign() {
		return versionSign;
	}
	public void setVersionSign(String versionSign) {
		this.versionSign = versionSign;
	}
	public long getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(long limitTime) {
		this.limitTime = limitTime;
	}
	public String getProcessDefContent() {
		return processDefContent;
	}
	public void setProcessDefContent(String processDefContent) {
		this.processDefContent = processDefContent;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public ProcessElement getProcessElement() {
		return processElement;
	}
	public void setProcessElement(ProcessElement processElement) {
		this.processElement = processElement;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	//--------------------------hashCode and equals-------------------------------
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (processDefId ^ (processDefId >>> 32));
		result = prime * result
				+ ((processDefName == null) ? 0 : processDefName.hashCode());
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
		ProcessDefine other = (ProcessDefine) obj;
		if (processDefId != other.processDefId)
			return false;
		if (processDefName == null) {
			if (other.processDefName != null)
				return false;
		} else if (!processDefName.equals(other.processDefName))
			return false;
		return true;
	}
}
