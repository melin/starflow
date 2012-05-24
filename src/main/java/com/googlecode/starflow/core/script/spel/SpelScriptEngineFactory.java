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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-18 上午11:03:34
 * @version 
 */
public class SpelScriptEngineFactory implements ScriptEngineFactory {
	private static final String VERSION = "1.0";
	private static final String SHORT_NAME = "SpringExpression";
	private static final String LANGUAGE_NAME = "SpringExpression";
	private static final String LANGUAGE_VERSION = "3.1";

	@Override
	public String getEngineName() {
		return "Spring EL Scripting Language";
	}

	@Override
	public String getEngineVersion() {
		return VERSION;
	}

	@Override
	public List<String> getExtensions() {
		return EXTENSIONS;
	}

	@Override
	public List<String> getMimeTypes() {
		return MIME_TYPES;
	}

	@Override
	public List<String> getNames() {
		return NAMES;
	}

	@Override
	public String getLanguageName() {
		return LANGUAGE_NAME;
	}

	@Override
	public String getLanguageVersion() {
		return LANGUAGE_VERSION;
	}

	@Override
	public Object getParameter(String key) {
		if (ScriptEngine.NAME.equals(key)) {
			return SHORT_NAME;
		} else if (ScriptEngine.ENGINE.equals(key)) {
			return getEngineName();
		} else if (ScriptEngine.ENGINE_VERSION.equals(key)) {
			return VERSION;
		} else if (ScriptEngine.LANGUAGE.equals(key)) {
			return LANGUAGE_NAME;
		} else if (ScriptEngine.LANGUAGE_VERSION.equals(key)) {
			return LANGUAGE_VERSION;
		} else if ("THREADING".equals(key)) {
			return "MULTITHREADED";
		} else {
			throw new IllegalArgumentException("Invalid key");
		}
	}

	@Override
	public String getMethodCallSyntax(String obj, String method, String... args) {
		String ret = "T(" + obj.getClass() + ")." + method + "(";
		int len = args.length;
		if (len == 0) {
			ret += ")";
			return ret;
		}
		for (int i = 0; i < len; i++) {
			ret += args[i];
			if (i != len - 1) {
				ret += ",";
			} else {
				ret += ")";
			}
		}
		return ret;
	}

	@Override
	public String getOutputStatement(String toDisplay) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getProgram(String... statements) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new SpelScriptEngine();
	}

	private static final List<String> NAMES;
	private static final List<String> EXTENSIONS;
	private static final List<String> MIME_TYPES;
	static {
		List<String> n = new ArrayList<String>(2);
		n.add(SHORT_NAME);
		n.add(LANGUAGE_NAME);
		NAMES = Collections.unmodifiableList(n);
		n = new ArrayList<String>(1);
		n.add("spEL");
		EXTENSIONS = Collections.unmodifiableList(n);
		n = new ArrayList<String>(1);
		n.add("application/x-spEL");
		MIME_TYPES = Collections.unmodifiableList(n);
	}
}