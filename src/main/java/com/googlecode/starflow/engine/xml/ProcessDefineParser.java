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

package com.googlecode.starflow.engine.xml;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.googlecode.starflow.engine.core.Constants;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.model.elements.ProcessElement;
import com.googlecode.starflow.engine.model.elements.TransitionElement;

/**
 * 把流程定义信息解析为java对象
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ProcessDefineParser {
	public static Document createDocument(String xml) { 
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(xml));
		} catch (Exception e) {
			throw new StarFlowParserException("流程定义信息不正确", e);
		}
		return document;
	}
	
	public static ProcessElement createProcessXml(String xml) {
		ProcessElement processXml = new ProcessElement();
		Document document = createDocument(xml);
		queryProcessXmlInfo(processXml, document);
		processXml.setTransitions(queryTransitionXmlInfo(document));
		processXml.setActivitys(queryActivityXmlInfo(processXml, document));
		return processXml;
	}
	
	private static void queryProcessXmlInfo(ProcessElement processXml, Document document) {
		Element rootElement = document.getRootElement();
		String name = rootElement.attributeValue(StarFlowNames.FLOW_ATTR_NAME);
		String chname = rootElement.attributeValue(StarFlowNames.FLOW_ATTR_CHNAME);
		String version = rootElement.attributeValue(StarFlowNames.FLOW_ATTR_VERSION);
		String xpath = "/ProcessDefine/ProcessProperty/".concat(StarFlowNames.FLOW_CHILD_DESC);
		String description = rootElement.selectSingleNode(xpath).getText();
		
		xpath = "/ProcessDefine/ProcessProperty/".concat(StarFlowNames.FLOW_CHILD_LIMITTIME);
		String limitTime = rootElement.selectSingleNode(xpath).getText();
		
		processXml.setName(name);
		
		if(chname != null)
			processXml.setChname(chname);
		else
			processXml.setChname(name);
			
		processXml.setVersion(version);
		processXml.setDescription(description);
		processXml.setLimitTime(Long.parseLong(limitTime));
		
		Element node = (Element)rootElement.selectSingleNode("/ProcessDefine/ProcessProperty");
		processXml.setEvents(NodeUtil.getTriggerEvents(node));
		processXml.setProperties(NodeUtil.getExtProperties(node));
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, ActivityElement> queryActivityXmlInfo(ProcessElement processXml, Document document) {
		Map<String, ActivityElement> aMap = new ConcurrentHashMap<String, ActivityElement>();
		
		List<Element> actEls = null;
		String _xpath = "/ProcessDefine/Activitys/Activity";
		actEls = document.selectNodes(_xpath);
		
		for(Element actEl : actEls) {
			ActivityElement activityXml = new ActivityElement();
			String id = NodeUtil.getNodeAttrValue(actEl, StarFlowNames.ACT_ATTR_ID);
			activityXml.setId(id);
			String type = NodeUtil.getNodeAttrValue(actEl, StarFlowNames.ACT_ATTR_TYPE);
			activityXml.setType(type);
			activityXml.setName(NodeUtil.getNodeAttrValue(actEl, StarFlowNames.ACT_ATTR_NAME));
			activityXml.setDescription(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_DESC));
			
			if(Constants.ACT_TYPE_START.equalsIgnoreCase(type)) {//开始
				activityXml.setSplitMode(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_SPLIT));
			} else if(Constants.ACT_TYPE_END.equalsIgnoreCase(type)) {//结束
				activityXml.setJoinMode(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_JOIN));
				activityXml.setActivateRuleType(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_ACTIVATE_RULE_TYPE));
				activityXml.setStartStrategybyAppAction(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_STARTSTRATEGYBYAPPACTION));
			} else if(Constants.ACT_TYPE_TOOLAPP.equalsIgnoreCase(type)) {//自动
				activityXml.setFinishType(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_AUTO_FINSISH_TYPE));
				activityXml.setInvokePattern(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_AUTO_INVOKE_PATTERN));
				activityXml.setTransactionType(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_AUTO_TRANSACTION_TYPE));
				activityXml.setExceptionStrategy(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_EXCEPTION_STRATEGY));
				activityXml.setExceptionAction(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_EXCEPTION_ACTION));
				activityXml.setExecuteAction(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_AUTO_EXEC_ACTION));
				activityXml.setJoinMode(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_JOIN));
				activityXml.setSplitMode(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_SPLIT));
				activityXml.setActivateRuleType(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_ACTIVATE_RULE_TYPE));
				activityXml.setStartStrategybyAppAction(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_STARTSTRATEGYBYAPPACTION));
				activityXml.setEvents(NodeUtil.getTriggerEvents(actEl));
				activityXml.setProperties(NodeUtil.getExtProperties(actEl));
			} else if(Constants.ACT_TYPE_SUBPROCESS.equalsIgnoreCase(type)) {//子流程
				activityXml.setSubProcess(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_SUBPROCESS));
				activityXml.setSpInvokePattern(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_SP_INVOKE_PATTERN));
				activityXml.setJoinMode(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_JOIN));
				activityXml.setSplitMode(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_SPLIT));
				activityXml.setActivateRuleType(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_ACTIVATE_RULE_TYPE));
				activityXml.setStartStrategybyAppAction(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_STARTSTRATEGYBYAPPACTION));
				activityXml.setEvents(NodeUtil.getTriggerEvents(actEl));
			} else if(Constants.ACT_TYPE_MANUL.equalsIgnoreCase(type)) {//人工环节
				activityXml.setParticipantType(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_PARTICI_MODE));
				activityXml.setIsAllowAppointParticipants(NodeUtil.getNodeBooleanValue(actEl, StarFlowNames.ACT_CHILD_PARTICI_IS_ALLOW_APPOINT));
				activityXml.setParticiLogic(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_PARTICI_LOGIC));
				activityXml.setParticiSpecActID(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_PARTICI_ACTID));
				activityXml.setIsFreeActivity(NodeUtil.getNodeBooleanValue(actEl, StarFlowNames.ACT_FREE_ISFREEACT));
				activityXml.setFreeRangeStrategy(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_FREE_RANGESTRATEGY));
				activityXml.setIsOnlyLimitedManualActivity(NodeUtil.getNodeBooleanValue(actEl, StarFlowNames.ACT_FREE_ISONLYLIMITEDMANUALACT));
				activityXml.setWiMode(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_WI_MODE));
				activityXml.setIsSequentialExecute(NodeUtil.getNodeBooleanValue(actEl, StarFlowNames.ACT_CHILD_WI_IS_SEQ_EXEC));
				activityXml.setWorkitemNumStrategy(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_WI_WORKITEMNUMSTRATEGY));
				activityXml.setFinishRule(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_WI_FINISHRULE));
				activityXml.setFinishRequiredPercent(NodeUtil.getNodeDoubleValue(actEl, StarFlowNames.ACT_CHILD_WI_FINISHREQUIREDPERCENT));
				activityXml.setFinishRquiredNum(NodeUtil.getNodeIntValue(actEl, StarFlowNames.ACT_CHILD_WI_FINISHRQUIREDNUM));
				activityXml.setIsAutoCancel(NodeUtil.getNodeBooleanValue(actEl, StarFlowNames.ACT_CHILD_WI_IS_AUTO_CANCEL));
				activityXml.setJoinMode(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_JOIN));
				activityXml.setSplitMode(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_SPLIT));
				activityXml.setLimitTime(NodeUtil.getNodeLongValue(actEl, StarFlowNames.ACT_CHILD_LIMITTIME));
				activityXml.setActivateRuleType(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_ACTIVATE_RULE_TYPE));
				activityXml.setStartStrategybyAppAction(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_STARTSTRATEGYBYAPPACTION));
				activityXml.setResetParticipant(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_RESET_PARTICIPANT));
				
				activityXml.setAction(NodeUtil.getNodeStringValue(actEl, StarFlowNames.ACT_CHILD_ACTION));
				activityXml.setOperations(NodeUtil.getOperations(actEl));
				
				activityXml.setEvents(NodeUtil.getTriggerEvents(actEl));
				activityXml.setProperties(NodeUtil.getExtProperties(actEl));
				activityXml.setParticipants(NodeUtil.getActParticipants(actEl));
				activityXml.setFreeActs(NodeUtil.getActFreeActs(actEl));
				
			}
			
			for(TransitionElement transitionXml : processXml.getTransitions()) {
				if(transitionXml.getTo().equalsIgnoreCase(id))
					activityXml.getBeforeTrans().add(transitionXml);
				if(transitionXml.getFrom().equalsIgnoreCase(id))
					activityXml.getAfterTrans().add(transitionXml);
			}
			
			activityXml.setLeft(NodeUtil.getElementLeft(actEl));
			activityXml.setTop(NodeUtil.getElementTop(actEl));
			aMap.put(id, activityXml);
		}
		
		return aMap;
	}
	
	@SuppressWarnings("unchecked")
	private static List<TransitionElement> queryTransitionXmlInfo(Document document) {
		List<TransitionElement> transitions = new CopyOnWriteArrayList<TransitionElement>();
		List<Element> tranEls = null;
		String _xpath = "/ProcessDefine/Transitions/Transition";
		tranEls = document.selectNodes(_xpath);
		
		for(Element tranEl : tranEls) {
			TransitionElement transitionXml = new TransitionElement();
			transitionXml.setId(NodeUtil.getNodeAttrValue(tranEl, StarFlowNames.TRAN_ATTR_ID));
			transitionXml.setName(NodeUtil.getNodeAttrValue(tranEl, StarFlowNames.TRAN_ATTR_NAME));
			transitionXml.setFrom(NodeUtil.getNodeAttrValue(tranEl, StarFlowNames.TRAN_ATTR_FROM));
			transitionXml.setTo(NodeUtil.getNodeAttrValue(tranEl, StarFlowNames.TRAN_ATTR_TO));
			transitionXml.setIsDefault(NodeUtil.getNodeBooleanValue(tranEl, StarFlowNames.TRAN_CHILD_ISDEFAULT));
			transitionXml.setComplexExpressionValue(NodeUtil.getNodeStringValue(tranEl, StarFlowNames.TRAN_CHILD_COMPLEXEXPRESSIONVALUE));
			transitionXml.setIsSimpleExpression(NodeUtil.getNodeBooleanValue(tranEl, StarFlowNames.TRAN_CHILD_ISSIMPLEEXPRESSION));
			transitionXml.setLeftValue(NodeUtil.getNodeStringValue(tranEl, StarFlowNames.TRAN_CHILD_LEFTVALUE));
			transitionXml.setCompType(NodeUtil.getNodeStringValue(tranEl, StarFlowNames.TRAN_CHILD_COMPTYPE));
			transitionXml.setRightValue(NodeUtil.getNodeStringValue(tranEl, StarFlowNames.TRAN_CHILD_RIGHTVALUE));
			transitionXml.setPriority(NodeUtil.getNodeIntValue(tranEl, StarFlowNames.TRAN_CHILD_PRIORITY));
			transitionXml.setPoint(NodeUtil.getElementPoint(tranEl));
			transitions.add(transitionXml);
		}
		return transitions;
	}
}
