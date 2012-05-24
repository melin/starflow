package com.googlecode.starflow.core.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.util.ClassUtils;

import com.googlecode.starflow.engine.ProcessEngine;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-18 上午11:03:34
 * @version 
 */
abstract public class ScriptEngineHolder {
	private final static ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
	
	public static boolean isPresentAviator = false;
	private static ScriptEngine engine = null;
	
	static{
		isPresentAviator = ClassUtils.isPresent("com.googlecode.aviator.AviatorEvaluator", ProcessEngine.class.getClassLoader());
		String engineByName = "SpringExpression";
		if(isPresentAviator)
			engineByName = "Aviator";
		
		engine = SCRIPT_ENGINE_MANAGER.getEngineByName(engineByName);
	}
	
	public static ScriptEngine getScriptEngine() {
		return engine;
	}
	
	public static ScriptEngine getScriptEngine(String engineByName) {
		return SCRIPT_ENGINE_MANAGER.getEngineByName(engineByName);
	}
}
