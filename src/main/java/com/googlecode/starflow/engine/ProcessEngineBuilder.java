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

package com.googlecode.starflow.engine;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.starflow.core.util.ApplicationContextHolder;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ProcessEngineBuilder {
	private AtomicBoolean initialized = new AtomicBoolean(false);
	private ApplicationContext applicationContext;
	private ProcessEngine processEngine;
	private Configuration configuration;
	
	public ProcessEngineBuilder() {
		this("applicationContext.xml", Configuration.getInstance());
	}
	
	public ProcessEngineBuilder(Configuration configuration) {
		this("applicationContext.xml", configuration);
	}
	
	public ProcessEngineBuilder(String springConfigFile, Configuration configuration) {
		this(new ClassPathXmlApplicationContext(springConfigFile), configuration);
	}
	
	public ProcessEngineBuilder(ApplicationContext applicationContext) {
		this(applicationContext, Configuration.getInstance());
	}
	
	public ProcessEngineBuilder(ApplicationContext applicationContext, Configuration configuration) {
		this.applicationContext = applicationContext;
		ApplicationContextHolder.setAppContext(this.applicationContext);
		this.configuration = configuration;
	}
	
	public ProcessEngine buildProcessEngine() {
		checkInitialized();
		return this.processEngine;
	}
	
	private void checkInitialized() {
        if (processEngine == null && initialized.compareAndSet(false, true)) {
        	processEngine = new ProcessEngine(this.applicationContext, this.configuration);
        }
    }
}
