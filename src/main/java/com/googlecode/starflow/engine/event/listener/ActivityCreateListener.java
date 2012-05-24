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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.core.ExpressionHandlerFactory;
import com.googlecode.starflow.engine.core.RelaDataManagerBuilder;
import com.googlecode.starflow.engine.core.SplitModeFactory;
import com.googlecode.starflow.engine.core.data.RelaDataManager;
import com.googlecode.starflow.engine.core.expression.IExpressionHandler;
import com.googlecode.starflow.engine.core.split.SplitMode;
import com.googlecode.starflow.engine.event.ActivityCreateEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.model.elements.ProcessElement;
import com.googlecode.starflow.engine.model.elements.TransitionElement;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ActivityCreateListener extends AbstractProcessListener {
	//private static final Log logger = LogFactory.getLog(ActivityStartListener.class);
	
	@Override
	public void activityCreate(ActivityCreateEvent event) {
		ActivityInst activityInst = event.getActivityInst();
		
		String mode = Constants.SPLIT_SINGLE;
		
		List<ActivityElement> nextNodes = new ArrayList<ActivityElement>();

		ActivityElement activityElement = event.getProcessElement().getActivitys().get(activityInst.getActivityDefId());
		mode = activityElement.getSplitMode();;
		nextNodes = findFreeActs(event, event.getProcessElement(), activityElement); //自由流已经指定要启动的环节
		
		//如果不是自由流，需要查找后续环节
		if(nextNodes == null || nextNodes.size() == 0) {
			List<TransitionElement> transitions = activityElement.getAfterTrans();
			
			if(transitions.isEmpty())
				throw new ProcessEngineException("环节【" + activityInst.getActivityInstName() + "】不是结束环节，且没有输出环节不能正常运行");
			
			nextNodes = findNextActs(event, event.getProcessElement(), transitions, activityElement, mode);
		}
		
		//查找当前环节的分支策略
		SplitMode split = SplitModeFactory.buildSplitStrategy(mode);
		//List<ActivityInst> activityInsts = 
		split.createNextActInsts(event, nextNodes);
	}

	/**
	 * @param event
	 * @param document 流程定义document
	 * @param transitions
	 * @param srcEl 
	 * @param mode 环节分支模式
	 * @return
	 */
	private List<ActivityElement> findNextActs(ActivityCreateEvent event,
			ProcessElement processXml, List<TransitionElement> transitions, ActivityElement activityXml, String mode) {
		List<ActivityElement> nextNodes = new ArrayList<ActivityElement>();
		
		if(Constants.SPLIT_SINGLE.equalsIgnoreCase(mode)) {//单一分支
			// (1) 满足条件的连接线所指的活动被触发；
			// (2) 如果有若干个连接线上的条件都满足，那么比较连接线上的优先级，优先级高的那条连接线所指的活动将被触发；
			// (3) 如果连接线上的条件都不满足，那么取“默认值”的那条连接线所指的活动将被触发。
			TransitionElement defaultTransition = findTransitonsForJexl(event, processXml,
					transitions, nextNodes, true);
			
			if(nextNodes.size() ==0 ) {
				String _to = defaultTransition.getTo();
				ActivityElement _e = processXml.getActivitys().get(_to);
				nextNodes.add(_e);
			}
		} else if(Constants.SPLIT_MULTI.equalsIgnoreCase(mode)) {//多路分支
			// (1) 如果连接线上取“默认值”，那么由此连接线所指的后继活动会被触发；
			// (2) 如果连接线上的条件满足，那么由此连接线所指的后继活动会被触发。
			TransitionElement defaultTransition = findTransitonsForJexl(event, processXml,
					transitions, nextNodes, false);
			
			if(defaultTransition != null) {
				String _to = defaultTransition.getTo();
				ActivityElement _e = processXml.getActivitys().get(_to);
				nextNodes.add(_e);
			}
		} else if(Constants.SPLIT_ALL.equalsIgnoreCase(mode)) {//全部分支
			//表示该活动结束后它的所有后继活动将同时被触发。
			for(TransitionElement tranEl : transitions) {
				String _to = tranEl.getTo();
				ActivityElement _e = processXml.getActivitys().get(_to);
				nextNodes.add(_e);
			}
		}
		return nextNodes;
	}
	
	/**
	 * 
	 * @param document
	 * @param transitions
	 * @param nextNodes
	 * @param isSort
	 * @return
	 */
	private TransitionElement findTransitonsForJexl(ActivityCreateEvent event, ProcessElement processXml,
			List<TransitionElement> transitions, List<ActivityElement> nextNodes, boolean isSort) {
		//准备Transition 表达式变量参数
		RelaDataManager relaDataManager = RelaDataManagerBuilder.buildRelaDataManager();
		long processInstId = event.getProcessInstance().getProcessInstId();
		String activityDefId = event.getPreActivityXml().getId();
		Map<String , Object> conditions = relaDataManager.getExpressConditions(processInstId, activityDefId);
		
		List<TransitionElement> tranEls = new ArrayList<TransitionElement>();
		
		TransitionElement defaultTransition = null;
		for(TransitionElement transitionXml : transitions) { //循所有的分支，寻找满足条件的分支
			boolean isDefault = transitionXml.getIsDefault();
			if(isDefault) {
				defaultTransition = transitionXml;
				continue;
			}
			
			IExpressionHandler expressionHandler = 
				ExpressionHandlerFactory.buildExpressionHandler(transitionXml.getIsSimpleExpression());
			
			if(expressionHandler.execute(transitionXml, conditions))
				tranEls.add(transitionXml);
		}
		
		//当为单一分支的时候，连线按照优先级降序排列。
		if(isSort && tranEls.size()>1) {
			Collections.sort(tranEls, new Comparator<TransitionElement>() {
				public int compare(TransitionElement o1, TransitionElement o2) {
					int priority1 = o1.getPriority();
					int priority2 = o2.getPriority();
					if(priority1 == priority2)
						return 0;
					else if(priority1 < priority2)
						return 1;
					else
						return -1;
				}
			});
		}
		
		for(TransitionElement tranEl : tranEls) {
			String _to = tranEl.getTo();
			ActivityElement _e = processXml.getActivitys().get(_to);
			nextNodes.add(_e);
		}
		return defaultTransition;
	}

	/**
	 * 查找自由指定环节，如果没有指定自由环节，则跳过走默认线。
	 * 
	 * @param event
	 * @param document
	 * @return
	 */
	private List<ActivityElement> findFreeActs(ActivityCreateEvent event, ProcessElement processXml, ActivityElement activityXml) {
		ActivityInst activityInst = ((ActivityCreateEvent)event).getActivityInst();
		if(!Constants.ACT_TYPE_MANUL.equalsIgnoreCase(activityInst.getActivityType()))
			return null;
		
		boolean isFreeActivity = activityXml.getIsFreeActivity();
		if(!isFreeActivity)
			return null;
		
		RelaDataManager relaDataManager = RelaDataManagerBuilder.buildRelaDataManager();
		long processInstId = event.getProcessInstance().getProcessInstId();
		String activityDefId = event.getPreActivityXml().getId();
		List<String> actDefIds =  (List<String>)relaDataManager.getNextFreeActs(processInstId, activityDefId);
		List<ActivityElement> nextNodes = new ArrayList<ActivityElement>();
		for(String actDefId : actDefIds) {
			ActivityElement _e = processXml.getActivitys().get(actDefId);
			nextNodes.add(_e);
		}
		
		return nextNodes;
	}
	
}
