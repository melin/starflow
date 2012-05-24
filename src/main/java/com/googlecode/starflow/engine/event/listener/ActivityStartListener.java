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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.event.ActivityStartEvent;
import com.googlecode.starflow.engine.event.support.EventUtil;
import com.googlecode.starflow.engine.handle.ActivityHandlerAdapterFactory;
import com.googlecode.starflow.engine.handle.IHandlerAdapter;
import com.googlecode.starflow.engine.handle.InterruptStrategyException;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.support.TriggerActivityEventUtil;
import com.googlecode.starflow.service.filter.ProcessFilter;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ActivityStartListener extends AbstractProcessListener {
	private static Logger logger = LoggerFactory.getLogger(ActivityStartListener.class);

	@Override
	public void activityStart(ActivityStartEvent event) {
		ActivityInst activityInst = event.getActivityInst();
		activityInst.setCurrentState(StarFlowState.ACT_INST_RUNING);
		event.getActInstRep().updateActivityStateToRunning(activityInst.getActivityInstId(), 
				StarFlowState.ACT_INST_RUNING, new Date());
		
		//进入异常状态，等待人工干预，不继续往下运行
		IHandlerAdapter handle = ActivityHandlerAdapterFactory.buildHandler(activityInst.getActivityType());
		try {
			handle.action(event, activityInst);
		} catch (InterruptStrategyException e) {
			logger.error("非人工环节执行逻辑失败", e);
			return;
		} finally {
			triggerFilterExecuter(event, activityInst);
		}
		
		//流程启动后触发事件
		TriggerActivityEventUtil.afterStart(event.getProcessEngine(), event.getActivityXml(), 
				activityInst, event.getActivityXml().getEvents());
		
		if(Constants.ACT_TYPE_SUBPROCESS.equalsIgnoreCase(event.getActivityXml().getType()))
			//TODO 子流程，流程启动者的问题。
			startSubProcess(event, event.getActivityXml().getSubProcess(), 
					event.getProcessInstance().getCreator(), activityInst.getActivityInstId());
		
		updateTransCtrlIsUse(event, event.getActivityXml());
		innerFinishAct(event, activityInst);
	}
	
	private static void startSubProcess(AbstractFlowEvent event, String subProcessDefName, String creator, long activityInstId) {
		ProcessInstance subProcInst = event.getProcessEngine().getProcessInstanceService().innerCreateSubProcess(subProcessDefName, 
				creator, event.getProcessInstance().getMainProcInstId(), event.getProcessInstance().getProcessInstId(), activityInstId);
		event.getProcessEngine().getProcessInstanceService().startProcess(subProcInst.getProcessInstId());		
	}
	
	/**
	 *  
	 * @param event
	 * @param actInst
	 */
	protected void triggerFilterExecuter(ActivityStartEvent event, ActivityInst actInst) {
		//执行环节开始filter
		for(ProcessFilter filter : event.getProcessEngine().getProcessFilters()) {
			filter.activityStart(event, actInst);
		}
	}
	
	/**
	 * 
	 * @param event
	 * @param activityXml
	 */
	public void updateTransCtrlIsUse(ActivityStartEvent event, ActivityElement activityXml) {
		String joinMode = activityXml.getJoinMode();
		if(Constants.JOIN_MULTI.equalsIgnoreCase(joinMode) || Constants.JOIN_ALL.equalsIgnoreCase(joinMode))
			event.getActInstRep().updateTransCtrlIsUse(event.getProcessInstance().getProcessInstId(), activityXml.getId());
	}
	
	/**
	 * 判断下个环节为：结束环节或自动环节完成类型为自动完成时，进行调用
	 * 
	 * @param event
	 * @param actInsts
	 */
	private void innerFinishAct(ActivityStartEvent event, ActivityInst activityInst) {
		String type = activityInst.getActivityType();
		//发布【结束活动】结束事件
		if(Constants.ACT_TYPE_END.equalsIgnoreCase(type)) {
			ProcessInstance processInstance = event.getProcessInstance();
			EventUtil.publishActivityFinishEvent(event, processInstance, activityInst);
			
			//如果是子流程的，且响应父流程的子流程环节是同步执行，需要发布父流程的子流程环节的结束
			if(Constants.FLOW_IS_SUBFLOW.equals(processInstance.getSubFlow())) {
				ActivityInst pActivityInst = event.getActInstRep().findActivityInst(processInstance.getActivityInstId());
				if(pActivityInst.getCurrentState() == StarFlowState.ACT_INST_RUNING) {
					ProcessInstance pProcessInstance = event.getProcInstFacade().findProcessInstance(processInstance.getParentProcInstId());
					EventUtil.publishActivityFinishEvent(event, pProcessInstance, pActivityInst);
				}
			}
		} else if(Constants.ACT_TYPE_TOOLAPP.equalsIgnoreCase(type)) {
			//自动环节结束模式为：toolApp
			String callType = event.getProcessElement().getActivitys().get(activityInst.getActivityDefId()).getFinishType();
			if(Constants.ACT_ATUO_FINISH_TOOLAPP.equalsIgnoreCase(callType)) {
				EventUtil.publishActivityFinishEvent(event, event.getProcessInstance(), activityInst);
			}
		} else if(Constants.ACT_TYPE_START.equalsIgnoreCase(type)) {
			//发布【开始活动】结束事件
			EventUtil.publishActivityFinishEvent(event, event.getProcessInstance(), activityInst);
		} else if(Constants.ACT_TYPE_SUBPROCESS.equalsIgnoreCase(type) && 
				Constants.ACT_SUBPROCESS_ASYN.equalsIgnoreCase(event.getActivityXml().getSpInvokePattern())) {
			//子流程异步执行
			EventUtil.publishActivityFinishEvent(event, event.getProcessInstance(), activityInst);
		}
	}
}
