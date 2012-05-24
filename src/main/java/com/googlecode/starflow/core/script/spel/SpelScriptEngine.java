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

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-18 上午11:03:34
 * @version 
 */
public class SpelScriptEngine extends AbstractScriptEngine implements
		Compilable {
	public static final String ROOT_OBJECT = "root";
	private volatile SpelScriptEngineFactory factory;

	@Override
	public Object eval(String script, ScriptContext context)
			throws ScriptException {
		Expression expression = parse(script);
		return evalExpression(expression, context);
	}

	private Expression parse(String script) {
		ExpressionParser expressionParser = getExpressionParser();
		return expressionParser.parseExpression(script);
	}

	private Object evalExpression(Expression expression,
			ScriptContext scriptContext) {
		StandardEvaluationContext standardEvaluationContext = getStandardEvaluationContext(scriptContext);
		return expression.getValue(standardEvaluationContext);
	}

	private StandardEvaluationContext getStandardEvaluationContext(
			ScriptContext scriptContext) {
		StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext(
				scriptContext.getAttribute(ROOT_OBJECT));
		standardEvaluationContext.setVariables(getVariables(scriptContext));
		return standardEvaluationContext;
	}

	private Map<String, Object> getVariables(ScriptContext scriptContext) {
		Map<String, Object> variables = new HashMap<String, Object>();
		if (scriptContext.getBindings(ScriptContext.GLOBAL_SCOPE) != null) {
			variables.putAll(scriptContext
					.getBindings(ScriptContext.GLOBAL_SCOPE));
		}
		if (scriptContext.getBindings(ScriptContext.ENGINE_SCOPE) != null) {
			variables.putAll(scriptContext
					.getBindings(ScriptContext.ENGINE_SCOPE));
		}
		return variables;
	}

	private ExpressionParser getExpressionParser() {
		return new SpelExpressionParser();
	}

	@Override
	public Object eval(Reader reader, ScriptContext context)
			throws ScriptException {
		return eval(readFully(reader), context);
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public ScriptEngineFactory getFactory() {
		if (factory == null) {
			synchronized (this) {
				if (factory == null) {
					factory = new SpelScriptEngineFactory();
				}
			}
		}
		return factory;
	}

	private class SpelCompiledScript extends CompiledScript {
		private final Expression expression;

		public SpelCompiledScript(Expression expression) {
			this.expression = expression;
		}

		@Override
		public Object eval(ScriptContext context) throws ScriptException {
			return evalExpression(expression, context);
		}

		@Override
		public ScriptEngine getEngine() {
			return SpelScriptEngine.this;
		}
	}

	@Override
	public CompiledScript compile(String script) throws ScriptException {
		return new SpelCompiledScript(parse(script));
	}

	@Override
	public CompiledScript compile(Reader script) throws ScriptException {
		return compile(readFully(script));
	}

	private static final String readFully(Reader reader) throws ScriptException {
		char[] arr = new char[8 * 1024];
		StringBuilder buf = new StringBuilder();
		int numChars;
		try {
			while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
				buf.append(arr, 0, numChars);
			}
		} catch (IOException exp) {
			throw new ScriptException(exp);
		}
		return buf.toString();
	}
}