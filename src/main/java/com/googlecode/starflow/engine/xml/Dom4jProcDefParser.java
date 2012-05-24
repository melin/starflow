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

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.googlecode.starflow.engine.model.ProcessDefine;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class Dom4jProcDefParser {
	
	/**
	 * 获取流程信息
	 * 
	 * @param processDefine
	 * @return
	 */
	public static ProcessDefine parserProcessInfo(ProcessDefine processDefine) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(processDefine.getProcessDefContent()));
			Element rootElement = document.getRootElement();
			String _name = rootElement.attributeValue(StarFlowNames.FLOW_ATTR_NAME);
			String _chname = rootElement.attributeValue(StarFlowNames.FLOW_ATTR_CHNAME);
			String _version = rootElement.attributeValue(StarFlowNames.FLOW_ATTR_VERSION);
			String _xpath = "/ProcessDefine/ProcessProperty/".concat(StarFlowNames.FLOW_CHILD_DESC);
			String _description = rootElement.selectSingleNode(_xpath).getText();
			
			_xpath = "/ProcessDefine/ProcessProperty/".concat(StarFlowNames.FLOW_CHILD_LIMITTIME);
			String _limitTime = rootElement.selectSingleNode(_xpath).getText();
			
			processDefine.setProcessDefName(_name);
			
			if(_chname != null)
				processDefine.setProcessCHName(_chname);
			else
				processDefine.setProcessCHName(_name);
				
			processDefine.setVersionSign(_version);
			processDefine.setDescription(_description);
			processDefine.setLimitTime(Long.parseLong(_limitTime));
		} catch (Exception e) {
			throw new StarFlowParserException("流程定义信息不正确", e);
		}
		return processDefine;
	}
	
	/**
	 * 根据环节定义ID。获取当前环节节点
	 * 
	 * @param document
	 * @param activityDefId
	 * @return
	 */
	public static Element parserActivityInfo(Document document, String activityDefId) {
		String _xpath = "/ProcessDefine/Activitys/Activity[@id='" + activityDefId + "']";
		Element element = (Element)document.selectSingleNode(_xpath);
		return element;
	}
	
	/**
	 * 根据环节定义ID。获取当前环节节点
	 * 
	 * @param document
	 * @param activityDefId
	 * @return
	 */
	public static Element parserActivityInfo(String processDefContent, String activityDefId) {
		SAXReader reader = new SAXReader();
		Document document = null;
		Element element = null;
		try {
			document = reader.read(new StringReader(processDefContent));
			element = parserActivityInfo(document, activityDefId);
		} catch (Exception e) {
			throw new StarFlowParserException("流程定义信息不正确", e);
		}
		return element;
	}
	
	/**
	 * 获取当前环节的所有分支环节
	 * 
	 * @param document
	 * @param activityDefId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> parserTransitionInfoFromTo(Document document, String activityDefId) {
		List<Element> list = null;
		String _xpath = "/ProcessDefine/Transitions/Transition[@from='" + activityDefId + "']";
		list = document.selectNodes(_xpath);
		return list;
	}
	
	/**
	 * 获取当前环节的所有前驱环节
	 * 
	 * @param document
	 * @param activityDefId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> parserTransitionInfoToFrom(Document document, String activityDefId) {
		List<Element> list = null;
		String _xpath = "/ProcessDefine/Transitions/Transition[@to='" + activityDefId + "']";
		list = document.selectNodes(_xpath);
		return list;
	}
	
	/**
	 * 获取当前环节的所有分支环节
	 * 
	 * @param document
	 * @param activityDefId
	 * @return
	 */
	public static List<Element> parserTransitionInfo(String processDefContent, String activityDefId) {
		SAXReader reader = new SAXReader();
		Document document = null;
		List<Element> list = null;
		try {
			document = reader.read(new StringReader(processDefContent));
			list = parserTransitionInfoFromTo(document, activityDefId);
		} catch (Exception e) {
			throw new StarFlowParserException("流程定义信息不正确", e);
		}
		return list;
	}
}
