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

package com.googlecode.starflow.engine.service.impl;

import java.util.Date;

import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.googlecode.starflow.core.key.Keys;
import com.googlecode.starflow.core.util.PrimaryKeyUtil;
import com.googlecode.starflow.engine.ProcessEngine;
import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.event.support.EventUtil;
import com.googlecode.starflow.engine.model.ProcessDefine;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.repository.IProcessDefineRepository;
import com.googlecode.starflow.engine.repository.IProcessInstanceRepository;
import com.googlecode.starflow.engine.service.IProcessInstanceService;
import com.googlecode.starflow.engine.service.ProcessDefineNotFoundException;
import com.googlecode.starflow.engine.support.TriggerProcessEventUtil;
import com.googlecode.starflow.engine.transaction.TransactionCallback;
import com.googlecode.starflow.engine.transaction.TransactionCallbackWithoutResult;
import com.googlecode.starflow.engine.transaction.TransactionTemplate;
import com.googlecode.starflow.service.filter.ProcessFilter;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ProcessInstanceService implements IProcessInstanceService {
	//private final Logger logger = LoggerFactory.getLogger(ProcessInstanceService.class);
	private final ProcessEngine processEngine;
	 
	private IProcessDefineRepository procDefRep;
	private IProcessInstanceRepository procInstRep;
	private TransactionTemplate transactionTemplate;
	
	public ProcessInstanceService(ProcessEngine processEngine) {
		Assert.notNull(processEngine);
		
		this.processEngine = processEngine;
		
		this.procDefRep = this.processEngine.getApplicationContext().getBean(IProcessDefineRepository.class);
		this.procInstRep = this.processEngine.getApplicationContext().getBean(IProcessInstanceRepository.class);
		this.transactionTemplate = processEngine.getTransactionTemplate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ProcessInstance createProcess(String processDefName, String userId) {
		return innercreateProcess(processDefName, userId, -1, -1, -1, Constants.FLOW_ISNOT_SUBFLOW);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ProcessInstance innerCreateSubProcess(String processDefName, String userId, final long mainProcInstId,
			long parentProcInstId, long activityInstId) {
		return innercreateProcess(processDefName, userId, mainProcInstId, parentProcInstId, activityInstId, Constants.FLOW_IS_SUBFLOW);
	}
	
	/**
	 * {@inheritDoc}
	 */
	private ProcessInstance innercreateProcess(String processDefName, final String userId, final long mainProcInstId,
			final long parentProcInstId, final long activityInstId, final String subFlow) {
		if(!StringUtils.hasText(userId))
			throw new IllegalArgumentException("创建流程时，必须指定用户 ID");
		
		final ProcessDefine processDefine = procDefRep.findPublishProcessDefine(processDefName);
		if(processDefine == null)
			throw new ProcessDefineNotFoundException("没有创建流程，或者流程定义版本没有发布");
		
		//流程启动前触发事件
		TriggerProcessEventUtil.beforeStart(processEngine, processDefine, null,
				processDefine.getProcessElement().getEvents());
		
		ProcessInstance processInstance = transactionTemplate.execute(new TransactionCallback<ProcessInstance>() {
			public ProcessInstance doInTransaction(TransactionStatus status) {
				ProcessInstance _processInstance = new ProcessInstance();
				_processInstance.setProcessDefId(processDefine.getProcessDefId());
				_processInstance.setProcessInstName(processDefine.getProcessDefName());
				_processInstance.setCreator(userId);
				_processInstance.setCurrentState(StarFlowState.PROCESS_INST_START);
				_processInstance.setSubFlow(subFlow);
				_processInstance.setCreateTime(new Date());
				
				_processInstance.setLimitNum(processDefine.getLimitTime());
				long _id = PrimaryKeyUtil.getPrimaryKey(Keys.PROCESSINSTID);
				_processInstance.setProcessInstId(_id);
				if(mainProcInstId == -1 && parentProcInstId == -1) {
					_processInstance.setMainProcInstId(_id);
					_processInstance.setParentProcInstId(_id);
				} else {
					_processInstance.setMainProcInstId(mainProcInstId);
					_processInstance.setParentProcInstId(parentProcInstId);
				}
				
				if(activityInstId != -1)
					_processInstance.setActivityInstId(activityInstId);
				procInstRep.insertProcessInstance(_processInstance);
				return _processInstance;
			}
		});
		for(ProcessFilter filter : this.processEngine.getProcessFilters()) {
			filter.processCreate(processInstance);
		}
		return processInstance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void startProcess(long processInstId) {
		final ProcessInstance processInstance = procInstRep.findProcessInstance(processInstId);
		
		//检查流程是否处于启动状态
		if(StarFlowState.PROCESS_INST_START != processInstance.getCurrentState()) 
			throw new ProcessEngineException("流程实例【"+processInstId+"】未处于启动状态，不能启动流程，当前状态为：" + processInstance.getCurrentState());

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				EventUtil.publishProcessStartEvent(processEngine, processInstance);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ProcessInstance createAndStartProcess(String processDefName, String userId) {
		final ProcessInstance processInstance = this.createProcess(processDefName, userId);
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				startProcess(processInstance.getProcessInstId());
				processInstance.setCurrentState(StarFlowState.PROCESS_INST_RUNNING);
			}
		});
		
		return processInstance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void terminateProcess(long processInstId) {
		final ProcessInstance processInstance = procInstRep.findProcessInstance(processInstId);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				EventUtil.publishProcessTerminalEvent(processEngine, processInstance);
			}
		});
	}
}
