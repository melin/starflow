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

package com.googlecode.starflow.core.script.spel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.script.ScriptEngine;

import org.junit.Test;

import com.googlecode.starflow.core.script.spel.SpelScriptEngine;
import com.googlecode.starflow.core.script.spel.SpelScriptEngineFactory;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-18 上午11:03:34
 * @version 
 */
public class SpelScriptEnfineFactoryBehaviour {
	@Test
	public void shouldReturnSpelScriptEngine() {
		SpelScriptEngineFactory spelScriptEngineFactory = new SpelScriptEngineFactory();
		ScriptEngine scriptEngine = spelScriptEngineFactory.getScriptEngine();
		assertTrue(scriptEngine instanceof SpelScriptEngine);
		assertEquals("SpringExpression", (String) spelScriptEngineFactory.getParameter(ScriptEngine.NAME));
	}
}
