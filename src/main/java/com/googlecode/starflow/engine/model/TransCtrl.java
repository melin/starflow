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

public class TransCtrl {
	private long transCtrlId;
	private String srcActDefId;
	private String destActDefId;
	private String srcActDefName;
	private String destActDefName;
	private String srcActType;
	private String destActType;
	private Date transTime;
	private long processInstId;
	private String isUse;
	private String isStartDestAct;
	
	public long getTransCtrlId() {
		return transCtrlId;
	}
	public void setTransCtrlId(long transCtrlId) {
		this.transCtrlId = transCtrlId;
	}
	public String getSrcActDefId() {
		return srcActDefId;
	}
	public void setSrcActDefId(String srcActDefId) {
		this.srcActDefId = srcActDefId;
	}
	public String getDestActDefId() {
		return destActDefId;
	}
	public void setDestActDefId(String destActDefId) {
		this.destActDefId = destActDefId;
	}
	public String getSrcActType() {
		return srcActType;
	}
	public void setSrcActType(String srcActType) {
		this.srcActType = srcActType;
	}
	public String getDestActType() {
		return destActType;
	}
	public void setDestActType(String destActType) {
		this.destActType = destActType;
	}
	public String getSrcActDefName() {
		return srcActDefName;
	}
	public void setSrcActDefName(String srcActDefName) {
		this.srcActDefName = srcActDefName;
	}
	public String getDestActDefName() {
		return destActDefName;
	}
	public void setDestActDefName(String destActDefName) {
		this.destActDefName = destActDefName;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public long getProcessInstId() {
		return processInstId;
	}
	public void setProcessInstId(long processInstId) {
		this.processInstId = processInstId;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getIsStartDestAct() {
		return isStartDestAct;
	}
	public void setIsStartDestAct(String isStartDestAct) {
		this.isStartDestAct = isStartDestAct;
	}
}
