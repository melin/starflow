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

package com.googlecode.starflow.engine.event.listener;

import java.util.Date;
import java.util.List;

import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.event.ActivityRollBackEvent;
import com.googlecode.starflow.engine.event.support.RollbackEngineException;
import com.googlecode.starflow.engine.model.ActivityInst;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ActivityRollBackListener extends AbstractProcessListener {
	//private static final Log logger = LogFactory.getLog(ActivityRollBackListener.class);
	
	@Override
	public void activityRollback(ActivityRollBackEvent event) {
		List<String> actDefIds = event.getActDefIds();
		Date nowDate = new Date();
		for(String actDefId : actDefIds) {
			ActivityInst activityInst = event.getActInstRep().findActivityInstByActDefId(actDefId);
			if(activityInst.getCurrentState() == StarFlowState.ACT_INST_RUNING)
				throw new RollbackEngineException("环节【"+actDefId+"】处于运行状态，无法重启");
			
			event.getActInstRep().updateActivityStateToRunning(activityInst.getActivityInstId(), 
					StarFlowState.ACT_INST_RUNING, nowDate);
			
			event.getWorkItemRep().updateActWorkItemStateAndStartTime(activityInst.getActivityInstId(), 
					StarFlowState.WORKITEM_RUNNING, nowDate);
		}
	}
}
