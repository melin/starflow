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

import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.model.ActivityInst;

public abstract class AbstractActivityType implements ActivityType {

	
	public ActivityInst findWaitingActInst(AbstractFlowEvent event, long processInstId, String activityDefId) {
		return event.getActInstRep().findWaitingActivityInst(processInstId, activityDefId, StarFlowState.ACT_INST_WAITING);
	}

}
