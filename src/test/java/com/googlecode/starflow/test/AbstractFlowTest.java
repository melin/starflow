/**
 * Copyright (c) 2011, SuZhou USTC Star Information Technology CO.LTD
 * All Rights Reserved.
 */

package com.googlecode.starflow.test;

import org.junit.Before;

import com.googlecode.starflow.engine.ProcessEngine;
import com.googlecode.starflow.engine.ProcessEngineBuilder;
import com.googlecode.starflow.engine.service.IActivityInstService;
import com.googlecode.starflow.engine.service.IProcessDefineService;
import com.googlecode.starflow.engine.service.IProcessInstanceService;
import com.googlecode.starflow.engine.service.IWorkItemService;
import com.googlecode.starflow.service.filter.LoggerProcessFilter;
import com.googlecode.starflow.service.filter.TransCtrlFilter;

/**
 *
 *
 * @author   bsli@starit.com.cn
 * @Date	 2011-8-10 上午11:48:39
 */
public abstract class AbstractFlowTest {
	protected IProcessInstanceService procInstService;
	protected IWorkItemService workItemService;
	protected IProcessDefineService procDefService;
	protected IActivityInstService activityInstService;
	
	@Before
	public void init() {
		ProcessEngine processEngine = new ProcessEngineBuilder().buildProcessEngine();
		procDefService = processEngine.getProcessDefineService();
		procInstService = processEngine.getProcessInstanceService();
		workItemService = processEngine.getWorkItemService();
		activityInstService = processEngine.getActivityInstService();
		
		//添加filter
		processEngine.addFilter(new LoggerProcessFilter());
		processEngine.addFilter(new TransCtrlFilter());
	}
}

