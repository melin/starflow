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

package com.googlecode.starflow.engine.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

import com.googlecode.starflow.engine.ProcessEngine;
import com.googlecode.starflow.engine.model.ProcessDefine;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.model.elements.ProcessElement;
import com.googlecode.starflow.engine.repository.IActivityInstRepository;
import com.googlecode.starflow.engine.repository.IProcessDefineRepository;
import com.googlecode.starflow.engine.repository.IProcessInstanceRepository;
import com.googlecode.starflow.engine.repository.IWorkItemRepository;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractFlowEvent extends ApplicationContextEvent {

	private ProcessEngine processEngine;
	private ProcessInstance processInstance;
	private ProcessElement processElement;
	private ActivityElement preActivityXml;
	
	private IProcessInstanceRepository procInstRep;
	private IActivityInstRepository actInstRep;
	private IWorkItemRepository workItemRep;
	
	public AbstractFlowEvent(ApplicationContext source) {
		super(source);
		this.procInstRep = this.getApplicationContext().getBean(IProcessInstanceRepository.class);
		this.actInstRep = this.getApplicationContext().getBean(IActivityInstRepository.class);
		this.workItemRep = this.getApplicationContext().getBean(IWorkItemRepository.class);
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
		IProcessDefineRepository procDefFacade = this.getApplicationContext().getBean(IProcessDefineRepository.class);
		ProcessDefine processDefine = procDefFacade.findProcessDefine(processInstance.getProcessDefId());
		processElement = processDefine.getProcessElement();
	}
	
	public ProcessElement getProcessElement() {
		return processElement;
	}
	
	public IProcessInstanceRepository getProcInstFacade() {
		return procInstRep;
	}

	public IActivityInstRepository getActInstRep() {
		return actInstRep;
	}

	public IWorkItemRepository getWorkItemRep() {
		return workItemRep;
	}

	public ActivityElement getPreActivityXml() {
		return preActivityXml;
	}

	public void setPreActivityXml(ActivityElement preActivityXml) {
		this.preActivityXml = preActivityXml;
	}
	
}
