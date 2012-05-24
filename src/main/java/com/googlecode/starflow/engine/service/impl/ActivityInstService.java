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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

import com.googlecode.starflow.engine.ProcessEngine;
import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.core.ActivityTypeFactory;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.core.activity.ActivityType;
import com.googlecode.starflow.engine.event.ActivityCreateEvent;
import com.googlecode.starflow.engine.event.ActivityStartEvent;
import com.googlecode.starflow.engine.event.support.EventUtil;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.ProcessDefine;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.TransCtrl;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.repository.IActivityInstRepository;
import com.googlecode.starflow.engine.repository.IProcessDefineRepository;
import com.googlecode.starflow.engine.repository.IProcessInstanceRepository;
import com.googlecode.starflow.engine.service.IActivityInstService;
import com.googlecode.starflow.engine.transaction.TransactionCallbackWithoutResult;
import com.googlecode.starflow.engine.transaction.TransactionTemplate;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ActivityInstService implements IActivityInstService {
	final private Logger logger = LoggerFactory.getLogger(ActivityInstService.class);
	final private ProcessEngine processEngine;
	
	private final IProcessDefineRepository procDefRep;
	private final IProcessInstanceRepository procInstRep;
	private final IActivityInstRepository actInstRep;
	
	private TransactionTemplate transactionTemplate;

	public ActivityInstService(ProcessEngine processEngine) {
		Assert.notNull(processEngine);
		this.processEngine =  processEngine;
		this.procDefRep = this.processEngine.getApplicationContext().getBean(IProcessDefineRepository.class);
		this.procInstRep = this.processEngine.getApplicationContext().getBean(IProcessInstanceRepository.class);
		this.actInstRep = this.processEngine.getApplicationContext().getBean(IActivityInstRepository.class);
		this.transactionTemplate = processEngine.getTransactionTemplate();
	}

	/* (non-Javadoc)
	 * @see com.starit.wf.engine.domain.service.IActivityInstService#finishActivity(long)
	 */
	public void finishActivity(long activityInstId) {
		final ActivityInst activityInst = this.actInstRep.findActivityInst(activityInstId);
		//判断环节是否处于运行状态，才可完成该工作项
		if(StarFlowState.ACT_INST_RUNING != activityInst.getCurrentState())
			throw new ProcessEngineException("当前环节（activityInstId="+activityInst.getActivityInstId()+"）不处于状态，"+
					"无法完成环节的运行！");
		
		final ProcessInstance processInstance = this.procInstRep.findProcessInstance(activityInst.getProcessInstId());
		final ProcessDefine processDefine = this.procDefRep.findProcessDefine(processInstance.getProcessDefId());
		final ActivityElement activityElement = processDefine.getProcessElement().getActivitys().get(activityInst.getActivityDefId()); 
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				//发布环节结束事件
				ActivityStartEvent event = new ActivityStartEvent(processEngine); 
				event.setProcessInstance(processInstance);
				event.setPreActivityXml(activityElement);
				
				EventUtil.publishActivityFinishEvent(event, processInstance, activityInst);
			}
		});
	}
	
	public void startActivityInst(long processInstId, String activityDefId) {
		final ProcessInstance processInstance = procInstRep.findProcessInstance(processInstId);
		if(StarFlowState.PROCESS_INST_RUNNING != processInstance.getCurrentState())
			throw new ProcessEngineException("流程不处于运行状态，不能重启环节！");
		
		final ProcessDefine processDefine = this.procDefRep.findProcessDefine(processInstance.getProcessDefId());
		final ActivityElement activityXml = processDefine.getProcessElement().getActivitys().get(activityDefId); 
		
		if(activityXml == null)
			throw new ProcessEngineException("指定启动环节【{}】不存在" + activityDefId);
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				String actType = activityXml.getType();
				ActivityType type = ActivityTypeFactory.buildActivityType(actType);
				
				//创建环节
				ActivityCreateEvent event = new ActivityCreateEvent(processEngine); 
				event.setProcessInstance(processInstance);
				ActivityInst activityInst = type.createActivity(event, activityXml);
				
				//启动环节
				EventUtil.publishActivityStartEvent(event, activityInst, activityXml);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.starit.wf.engine.domain.service.IActivityInstService#terminateActivity(long)
	 */
	public void terminateActivity(final long activityInstId) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				ActivityInst activityInst = actInstRep.findActivityInst(activityInstId);
				ProcessInstance processInstance = procInstRep.findProcessInstance(activityInst.getProcessInstId());
				EventUtil.publishActivityTerminalEvent(processEngine, processInstance, activityInst);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.starit.wf.engine.domain.service.IActivityInstService#restartActivity(long)
	 */
	public void restartActivity(long processInstId, long activityInstId) {
		ProcessInstance processInstance = procInstRep.findProcessInstance(processInstId);
		if(StarFlowState.PROCESS_INST_RUNNING != processInstance.getCurrentState())
			throw new ProcessEngineException("流程不处于运行状态，不能重启环节！");
		
		ActivityInst activityInst = actInstRep.findActivityInst(activityInstId);
		if(activityInst == null || activityInst.getCurrentState() == StarFlowState.ACT_INST_RUNING
				|| activityInst.getCurrentState() == StarFlowState.ACT_INST_WAITING
				|| activityInst.getCurrentState() == StarFlowState.ACT_APP_EXCEPTION) {
			throw new ProcessEngineException("环节只有处在完成、终止、应用异常状态，才可以重启环节");
		}
		
//		if(!Constants.ACT_TYPE_MANUL.equals(activityInst.getActivityType())) {
//			throw new ProcessEngineException("只有人工环节才可以重启环节，当前环节类型为："+activityInst.getActivityType());
//		}
		
		ProcessDefine processDefine = this.procDefRep.findProcessDefine(processInstance.getProcessDefId());
		ActivityElement activityElement = processDefine.getProcessElement().getActivitys().get(activityInst.getActivityDefId()); 
		
		EventUtil.publishActivityRestartEvent(processEngine, processInstance, activityInst, activityElement);
	}
	
	public void activateActivity(final long activityInstId) {
		final ActivityInst activityInst = actInstRep.findActivityInst(activityInstId);
		final ProcessInstance processInstance = procInstRep.findProcessInstance(activityInst.getProcessInstId());
		final ProcessDefine processDefine = this.procDefRep.findProcessDefine(processInstance.getProcessDefId());
		final ActivityElement activityElement = processDefine.getProcessElement().getActivitys().get(activityInst.getActivityDefId()); 
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				
				//判断环节是否处于运行状态，才可完成该工作项
				if(StarFlowState.ACT_INST_WAITING != activityInst.getCurrentState())
					throw new ProcessEngineException("当前环节（activityInstId="+activityInst.getActivityInstId()+"）不处于待激活状态，"+
							"无法激活该环节！");
				
				//创建环节
				ActivityCreateEvent event = new ActivityCreateEvent(processEngine); 
				event.setProcessInstance(processInstance);
				
				//启动环节
				EventUtil.publishActivityStartEvent(event, activityInst, activityElement);
				
				if(logger.isDebugEnabled())
					logger.debug("环节【{}】人工激活", activityInst.getActivityInstName());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.starit.wf.engine.domain.service.IActivityInstService#rollbackToActivityFroRecentManual(long)
	 */
	public void rollbackToActivityFroRecentManual(final long currentActInstID) {
		final ActivityInst activityInst = this.actInstRep.findActivityInst(currentActInstID);
		//判断环节是否处于运行状态，才可执行回退
		if(StarFlowState.ACT_INST_RUNING != activityInst.getCurrentState())
			throw new ProcessEngineException("当前环节（activityInstId="+activityInst.getActivityInstId()+"）不处于运行状态，"+
					"不能执行回退。");
		
		final ProcessInstance processInstance = this.procInstRep.findProcessInstance(activityInst.getProcessInstId());
		List<TransCtrl> trans = this.actInstRep.findTransCtrls(activityInst.getProcessInstId());
		
		String recentActDefId = null;
		String destActId = activityInst.getActivityDefId();
		for(TransCtrl transCtrl : trans) {
			if(transCtrl.getDestActDefId().equalsIgnoreCase(destActId) //如果不是人工环节，继续向前循环
					&& !Constants.ACT_TYPE_MANUL.equalsIgnoreCase(transCtrl.getSrcActType())) {
				destActId = transCtrl.getSrcActDefId();
			} else if(transCtrl.getDestActDefId().equalsIgnoreCase(destActId)
					&& Constants.ACT_TYPE_MANUL.equalsIgnoreCase(transCtrl.getSrcActType())) {
				recentActDefId = transCtrl.getSrcActDefId();
				break;
			}
		}
		if(recentActDefId == null)
			throw new ProcessEngineException("最近一个人工环节没有找到，无法回退");
		
		rollBackExecute(currentActInstID, activityInst, processInstance,
				recentActDefId);
	}

	/* (non-Javadoc)
	 * @see com.starit.wf.engine.domain.service.IActivityInstService#rollbackToActivityFroOneStep(long)
	 */
	public void rollbackToActivityFroOneStep(long currentActInstID) {
		throw new UnsupportedOperationException("单步回退功能没有实现");
	}
	
	private void rollBackExecute(final long currentActInstID,
			final ActivityInst activityInst,
			final ProcessInstance processInstance, final String recentActDefId) {
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				//先终止当前环节
				terminateActivity(currentActInstID);
				List<String> actDefIds = new ArrayList<String>();
				actDefIds.add(recentActDefId);
				//启动目标环节
				EventUtil.publishActivityRollBackEvent(processEngine, processInstance, activityInst, actDefIds);
			}
		});
	}

	public List<TransCtrl> findTransCtrls(long processInstId) {
		return actInstRep.findTransCtrls(processInstId);
	}
	
	public List<ActivityInst> findWaitingAndTerminateAndRunningActivityInst(long processInstId) {
		return actInstRep.findWaitingAndTerminateAndRunningActivityInst(processInstId);
	}
}
