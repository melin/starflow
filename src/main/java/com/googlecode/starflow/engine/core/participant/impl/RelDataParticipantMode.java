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

package com.googlecode.starflow.engine.core.participant.impl;

import java.util.List;

import com.googlecode.starflow.engine.core.RelaDataManagerBuilder;
import com.googlecode.starflow.engine.core.data.RelaDataManager;
import com.googlecode.starflow.engine.core.participant.ParticipantMode;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.model.Participant;
import com.googlecode.starflow.engine.model.elements.ActivityElement;

/**
 * 从相关数据区获取流程参与者
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class RelDataParticipantMode implements ParticipantMode {

	public List<Participant> creatParticipants(AbstractFlowEvent event, ActivityElement activityXml) {
		RelaDataManager relaDataManager = RelaDataManagerBuilder.buildRelaDataManager();
		long processInstId = event.getProcessInstance().getProcessInstId();
		String activityDefId = event.getPreActivityXml().getId();
		List<Participant> participants = relaDataManager.getNextActParticipant(processInstId, activityDefId);
		return participants;
	}
}