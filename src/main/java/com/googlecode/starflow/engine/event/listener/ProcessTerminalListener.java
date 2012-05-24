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

import com.googlecode.starflow.engine.ProcessEngine;
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.event.ProcessTerminalEvent;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.service.filter.ProcessFilter;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ProcessTerminalListener extends AbstractProcessListener {
	
	@Override
	public void processTerminal(ProcessTerminalEvent event) {
		ProcessEngine processEngine = event.getProcessEngine();
		ProcessInstance processInstance = event.getProcessInstance();
		long processInstId = processInstance.getProcessInstId();
		
		Date finalTime = new Date();
		event.getWorkItemRep().updateProcWorkItemStateAndFinalTime(processInstId, StarFlowState.WORKITEM_STOPPED, finalTime);
		event.getActInstRep().updateProcActivityStateAndFinalTime(processInstId, StarFlowState.ACT_INST_STOPPED, finalTime);
		event.getProcInstFacade().updateProcessStateAndFinalTime(processInstId, StarFlowState.PROCESS_INST_STOPPED, finalTime);
		
		//执行环节终止filter
		for(ProcessFilter filter : processEngine.getProcessFilters()) {
			filter.processTerminal(event);
		}
	}
}
