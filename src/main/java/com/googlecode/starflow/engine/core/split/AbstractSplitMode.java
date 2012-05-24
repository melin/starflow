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

package com.googlecode.starflow.engine.core.split;

import java.util.List;
import java.util.Map;

import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.core.ExpressionHandlerFactory;
import com.googlecode.starflow.engine.core.RelaDataManagerBuilder;
import com.googlecode.starflow.engine.core.data.RelaDataManager;
import com.googlecode.starflow.engine.core.expression.IExpressionHandler;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.model.elements.TransitionElement;
import com.googlecode.starflow.engine.support.TriggerActivityEventUtil;


/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
abstract public class AbstractSplitMode implements SplitMode {

	/**
	 * 根据设置的聚合模式，判断是否启动该
	 * 
	 * 
	 * @return
	 */
	protected boolean isStartAct(AbstractFlowEvent event, ActivityElement activityXml) {
		String joinMode = activityXml.getJoinMode();
		boolean isStartAct = false;
		
		if(Constants.JOIN_SINGLE.equalsIgnoreCase(joinMode)) {
			//表示当该活动的若干前驱活动中只要有一个满足条件的活动完成，该活动即可被触发。
			isStartAct = true;
		} else if(Constants.JOIN_MULTI.equalsIgnoreCase(joinMode)) {
			// 表示该活动必须等到它的所有满足条件的前驱活动全部完成才可以触发。满足条件的前驱活动包括：
			// (1) 它与该活动的连线是“默认值”。
			// (2) 它与该活动连线上条件为“true”。
			List<TransitionElement> transitions = activityXml.getBeforeTrans();
			int finishCount = event.getActInstRep().findFromTransCtrls(event.getProcessInstance().getProcessInstId(), activityXml.getId());
			int count = findTransitonsForJexl(event, transitions);
			
			if(count == (++finishCount)) {
				isStartAct = true;
			}
			
		} else if (Constants.JOIN_ALL.equalsIgnoreCase(joinMode)) {
			//表示该活动必须等到它的所有前驱活动全部完成才可以触发。
			int count = event.getActInstRep().findFromTransCtrls(event.getProcessInstance().getProcessInstId(), activityXml.getId());
			List<TransitionElement> transitions = activityXml.getBeforeTrans();;
			
			if(++count == transitions.size()) {
				isStartAct = true;
			}
		}
		
		triggerActivityBeforeStartEvent(isStartAct, event, activityXml);
		return isStartAct;
	}
	
	/**
	 * 启动<b>环节启动前<b>事件
	 * 
	 * @param isStartAct
	 * @param event
	 * @param activityXml
	 */
	private void triggerActivityBeforeStartEvent(boolean isStartAct, AbstractFlowEvent event, ActivityElement activityXml) {
		if(isStartAct && activityXml.getEvents() != null) {
			ActivityInst activityInst = new ActivityInst();
			activityInst.setActivityInstId(event.getProcessInstance().getProcessInstId());
			TriggerActivityEventUtil.beforeStart(event.getProcessEngine(), activityXml, activityInst, activityXml.getEvents());
		}
	}
	
	/**
	 * 
	 * @param engineManager
	 * @param transitions
	 * @return
	 */
	private int findTransitonsForJexl(AbstractFlowEvent event, List<TransitionElement> transitions) {
		int count = 0;
		
		RelaDataManager relaDataManager = RelaDataManagerBuilder.buildRelaDataManager();
		long processInstId = event.getProcessInstance().getProcessInstId();
		String activityDefId = event.getPreActivityXml().getId();
		Map<String , Object> conditions = relaDataManager.getExpressConditions(processInstId, activityDefId);
		
		for(TransitionElement transitionXml : transitions) { //循所有的分支，寻找满足条件的分支
			boolean isDefault = transitionXml.getIsDefault();
			if(isDefault) {
				count++;
				continue;
			}
			
			IExpressionHandler expressionHandler = 
				ExpressionHandlerFactory.buildExpressionHandler(transitionXml.getIsSimpleExpression());
			
			if(expressionHandler.execute(transitionXml, conditions))
				count++;
		}
		return count;
	}
}
