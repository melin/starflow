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

package com.googlecode.starflow.engine.core.activity;

import java.util.Date;

import com.googlecode.starflow.core.key.Keys;
import com.googlecode.starflow.core.util.PrimaryKeyUtil;
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.elements.ActivityElement;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class EndActivityType implements ActivityType {

	public ActivityInst createActivity(AbstractFlowEvent event, ActivityElement activityXml) {
		ProcessInstance procInst = event.getProcessInstance();
		
		ActivityInst activityInst = new ActivityInst();
		activityInst.setActivityDefId(activityXml.getId());
		activityInst.setActivityType(activityXml.getType());
		activityInst.setCurrentState(StarFlowState.ACT_INST_WAITING);
		activityInst.setCreateTime(new Date());
		activityInst.setActivityInstName(activityXml.getName());
		activityInst.setProcessInstId(procInst.getProcessInstId());
		
		long actInstId = PrimaryKeyUtil.getPrimaryKey(Keys.ACTIVITYINSTID);
		activityInst.setActivityInstId(actInstId);
		
		event.getActInstRep().inertActivityInst(activityInst);
		
		return activityInst;
	}

	public boolean isCompleteActivity(AbstractFlowEvent event, ActivityElement activityXml) {
		return true;
	}
}
