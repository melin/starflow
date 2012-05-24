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

package com.googlecode.starflow.engine.core.expression;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.starflow.core.util.ScriptEngineHolder;
import com.googlecode.starflow.engine.core.expression.xpath.XPathBuilder;
import com.googlecode.starflow.engine.model.elements.TransitionElement;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class SimpleExpressionHandler extends AbstractExpressionHandler {
	private static final String XPATH_PREFIX = "xp:";
	private static Map<String, String> compTypes = new HashMap<String, String>();
	
	static {
		compTypes.put("EQ", "==");
		compTypes.put("GT", ">");
		compTypes.put("LT", "<");
		compTypes.put("GE", ">=");
		compTypes.put("LE", "<=");
		compTypes.put("NE", "!=");
	}

	@Override
	public String buildExpression(TransitionElement transition, Map<String, Object> conditions) {
		String compType = compTypes.get(transition.getCompType());
		
		//xpath表达式解析
		String leftValue = transition.getLeftValue();
		if(leftValue.startsWith(XPATH_PREFIX)) {
			leftValue = parserXPath(leftValue, conditions);
		}
		
		//spring el需要在变量前添加#
		if(ScriptEngineHolder.isPresentAviator) 
			return "\"" + leftValue + "\" " + compType + " \"" + transition.getRightValue() +"\""; 
		else
			return "#" + leftValue + " " + compType + " \"" + transition.getRightValue() +"\"";
	}

	/**
	 * 解析XPath
	 * 
	 * @param value xp:变量:/Users/User/name
	 * @param map
	 * @return
	 */
	private String parserXPath(String value, Map<String, Object> conditions) {
		int index = value.indexOf(":", XPATH_PREFIX.length());
		String name =value.substring(XPATH_PREFIX.length(), index);
		String xml = (String)conditions.get(name);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Xpath 路径：{}, xml内容{}", value, xml);
		}
		
		XPathBuilder builder = XPathBuilder.xpath(value.substring(index+1));
		String result = builder.evaluate(xml);
		
		if(logger.isDebugEnabled()) {
			logger.debug("Xpath 解析结果：{}", result.trim());
		}
		
		return result.trim();
	}
}
