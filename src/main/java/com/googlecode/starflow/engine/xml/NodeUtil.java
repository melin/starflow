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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.util.StringUtils;

import com.googlecode.starflow.engine.model.Participant;
import com.googlecode.starflow.engine.model.elements.EventElement;
import com.googlecode.starflow.engine.model.elements.FreeActElement;
import com.googlecode.starflow.engine.model.elements.OperationElement;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class NodeUtil {
	/**
	 * 获取element的nodeName属性值
	 * 
	 * @param element
	 * @param nodeName
	 * @return String
	 */
	public static String getNodeAttrValue(Element element, String nodeName) {
		String value = element.attributeValue(nodeName);
		return value;
	}
	
	/**
	 * 获取element的path节点的值，返回String类型值
	 * 
	 * @param element
	 * @param path
	 * @return String
	 */
	public static String getNodeStringValue(Element element, String path) {
		Node node = element.selectSingleNode(path);
		return node.getText();
	}
	
	/**
	 * 获取element的path节点的值，返回Boolean类型值
	 * 
	 * @param element
	 * @param path
	 * @return
	 */
	public static boolean getNodeBooleanValue(Element element, String path) {
		String value = element.selectSingleNode(path).getText();
		if("false".equalsIgnoreCase(value))
			return false;
		else
			return true;
	}
	
	/**
	 * 获取element的path节点的值，返回int类型值
	 * 
	 * @param element
	 * @param path
	 * @return
	 */
	public static int getNodeIntValue(Element element, String path) {
		Node node = element.selectSingleNode(path);
		if(node != null) {
			if(StringUtils.hasText(node.getText()))
				return Integer.parseInt(node.getText());
			else
				return 0;
		} else
			return 0;
	}
	
	/**
	 * 获取element的path节点的值，返回long类型值
	 * 
	 * @param element
	 * @param path
	 * @return
	 */
	public static long getNodeLongValue(Element element, String path) {
		Node node = element.selectSingleNode(path);
		if(node != null) {
			if(StringUtils.hasText(node.getText()))
				return Long.parseLong(node.getText());
			else
				return 0l;
		} else
			return 0l;
	}
	
	/**
	 * 获取element的path节点的值，返回double类型值
	 * 
	 * @param element
	 * @param path
	 * @return
	 */
	public static double getNodeDoubleValue(Element element, String path) {
		Node node = element.selectSingleNode(path);
		if(node != null) {
			if("".equals(node.getText()))
				return 0d;
			else
				return Double.parseDouble(node.getText());
		}
		else
			return 0d;
	}
	
	/**
	 * 获取环节操作信息
	 */
	@SuppressWarnings("rawtypes")
	public static List<OperationElement> getOperations(Element actEl) {
		List<OperationElement> operationXmls = new LinkedList<OperationElement>();
		List list = actEl.selectNodes(StarFlowNames.ACT_OPERATION);
		Iterator iter=list.iterator();
        while(iter.hasNext()){
            Element el = (Element)iter.next();
            OperationElement opXml = new OperationElement();
            opXml.setId(el.attributeValue(StarFlowNames.ACT_OPERATION_ID));
            opXml.setName(el.attributeValue(StarFlowNames.ACT_OPERATION_NAME));
            opXml.setCode(el.attributeValue(StarFlowNames.ACT_OPERATION_CODE));
            opXml.setAction(el.attributeValue(StarFlowNames.ACT_OPERATION_ACTION));
            operationXmls.add(opXml);
        }
        return operationXmls;
	}
	
	/**
	 * 获取触发事件信息
	 */
	@SuppressWarnings("rawtypes")
	public static List<EventElement> getTriggerEvents(Element actEl) {
		List<EventElement> events = new LinkedList<EventElement>();
		List list = actEl.selectNodes("TriggerEvents/event");
		Iterator iter=list.iterator();
        while(iter.hasNext()){
            Element el = (Element)iter.next();
            EventElement event = new EventElement();
            event.setEventType(el.attributeValue("eventType"));
            event.setAction(el.attributeValue("action"));
            event.setInvokePattern(el.attributeValue("invokePattern"));
            event.setTransactionType(el.attributeValue("transactionType"));
            event.setExceptionStrategy(el.attributeValue("exceptionStrategy"));
            events.add(event);
        }
        return events;
	}
	
	/**
	 * 在指定活动列表范围内自由---指定的下一步环节
	 */
	@SuppressWarnings("rawtypes")
	public static List<FreeActElement> getActFreeActs(Element actEl) {
		List<FreeActElement> events = new CopyOnWriteArrayList<FreeActElement>();
		List list = actEl.selectNodes(StarFlowNames.ACT_FREE_ACT);
		Iterator iter=list.iterator();
        while(iter.hasNext()){
            Element el = (Element)iter.next();
            FreeActElement e = new FreeActElement();
            e.setId(el.attributeValue(StarFlowNames.ACT_FREE_ACT_ID));
            e.setName(el.attributeValue(StarFlowNames.ACT_FREE_ACT_NAME));
            e.setType(el.attributeValue(StarFlowNames.ACT_FREE_ACT_TYPE));
            events.add(e);
        }
        return events;
	}
	
	/**
	 * 获取环节参与者
	 */
	@SuppressWarnings("rawtypes")
	public static List<Participant> getActParticipants(Element actEl) {
		List<Participant> participants = new LinkedList<Participant>();
		List list = actEl.selectNodes(StarFlowNames.ACT_CHILD_PARTICIPANT);
		Iterator iter=list.iterator();
        while(iter.hasNext()){
            Element el = (Element)iter.next();
            Participant p = new Participant();
            p.setParticipant(el.attributeValue(StarFlowNames.ACT_CHILD_PARTICIPANT_ID));
            p.setParticipant2(el.attributeValue(StarFlowNames.ACT_CHILD_PARTICIPANT_NAME));
            p.setParticType(el.attributeValue(StarFlowNames.ACT_CHILD_PARTICIPANT_TYPE));
            participants.add(p);
        }
        return participants;
	}
	
	/**
	 * 获取环节或流程扩展属性
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> getExtProperties(Element el) {
		Map<String, String> map = new HashMap<String, String>();
		
		List list = el.selectNodes(StarFlowNames.FLOW_EXT_PROPERTY);
		Iterator iter=list.iterator();
        while(iter.hasNext()){
            Element e = (Element)iter.next();
            map.put(e.attributeValue("key"), e.attributeValue("value"));
        }
        return map;
	}
	
	//--------------------------------------------------------------------------------
	/**
	 * 获取位置left
	 * 
	 * @param element
	 * @param path
	 * @return
	 */
	public static String getElementLeft(Element tranEl) {
		Node position = tranEl.selectSingleNode("position");
		return ((Element)position).attributeValue("left");
	}
	
	/**
	 * 获取位置top
	 * 
	 * @param element
	 * @return
	 */
	public static String getElementTop(Element element) {
		Node position = element.selectSingleNode("position");
		return ((Element)position).attributeValue("top");
	}
	
	/**
	 * 获取位置point
	 * 
	 * @param element
	 * @return
	 */
	public static String getElementPoint(Element element) {
		Node position = element.selectSingleNode("position");
		return ((Element)position).attributeValue("point");
	}
}
