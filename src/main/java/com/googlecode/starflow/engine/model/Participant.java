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

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Participant implements Serializable {
	private long particId;
	private long workItemId;
	private String particType;
	private String participant;
	private String participant2;
	
	public long getParticId() {
		return particId;
	}
	public void setParticId(long particId) {
		this.particId = particId;
	}
	public long getWorkItemId() {
		return workItemId;
	}
	public void setWorkItemId(long workItemId) {
		this.workItemId = workItemId;
	}
	public String getParticType() {
		return particType;
	}
	public void setParticType(String particType) {
		this.particType = particType;
	}
	public String getParticipant() {
		return participant;
	}
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	public String getParticipant2() {
		return participant2;
	}
	public void setParticipant2(String participant2) {
		this.participant2 = participant2;
	}
}
