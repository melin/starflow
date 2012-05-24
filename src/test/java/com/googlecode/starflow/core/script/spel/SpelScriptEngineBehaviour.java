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

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.junit.Test;

import com.googlecode.starflow.core.script.spel.SpelScriptEngine;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-18 上午11:03:34
 * @version 
 */
public class SpelScriptEngineBehaviour {
	@Test
	public void shouldNavigateThroughProperties() throws ScriptException {
		SpelScriptEngine scriptEngineImpl = new SpelScriptEngine();
		ScriptContext scriptContext = new SimpleScriptContext();
		scriptContext.setAttribute(SpelScriptEngine.ROOT_OBJECT,
				new Inventor("Mike Tesla"), ScriptContext.ENGINE_SCOPE);
		String name = (String) scriptEngineImpl.eval("#root.name",
				scriptContext);
		assertEquals("Mike Tesla", name);
	}

	@Test
	public void shouldExecuteLogicalOperations() throws ScriptException {
		SpelScriptEngine scriptEngineImpl = new SpelScriptEngine();
		Boolean trueValue = (Boolean) scriptEngineImpl.eval("true or false");
		assertTrue(trueValue);
	}

	@Test
	public void shouldExecuteMathematicalOperations() throws ScriptException {
		SpelScriptEngine scriptEngineImpl = new SpelScriptEngine();
		Integer two = (Integer) scriptEngineImpl.eval("1+1");
		assertTrue(two == 2);
	}

	@Test
	public void shouldEvalVariables() throws ScriptException {
		SpelScriptEngine scriptEngineImpl = new SpelScriptEngine();
		ScriptContext scriptContext = new SimpleScriptContext();
		scriptContext.setAttribute("name", "Mike Tesla",
				ScriptContext.ENGINE_SCOPE);
		String name = (String) scriptEngineImpl.eval("#name", scriptContext);
		assertEquals("Mike Tesla", name);
	}
	
	@Test
	public void shouldEvalVariables1() throws ScriptException {
		SpelScriptEngine scriptEngineImpl = new SpelScriptEngine();
		ScriptContext scriptContext = new SimpleScriptContext();
		scriptContext.setAttribute("name", "melin",
				ScriptContext.ENGINE_SCOPE);
		Boolean result = (Boolean) scriptEngineImpl.eval("#name == 'melin'", scriptContext);
		assertTrue(result);
	}

	@Test
	public void shouldUseTypes() throws ScriptException {
		SpelScriptEngine scriptEngineImpl = new SpelScriptEngine();
		Calendar calendar = (Calendar) scriptEngineImpl
				.eval("T(java.util.Calendar).getInstance()");
		assertTrue(calendar instanceof Calendar);
	}

	public static class Inventor {
		private String name;

		public Inventor(String inventor) {
			this.name = inventor;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
