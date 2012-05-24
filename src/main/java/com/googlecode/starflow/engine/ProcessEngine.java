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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

import com.googlecode.starflow.core.resource.Environment;
import com.googlecode.starflow.engine.service.IActivityInstService;
import com.googlecode.starflow.engine.service.IFreeFlowService;
import com.googlecode.starflow.engine.service.IProcessDefineService;
import com.googlecode.starflow.engine.service.IProcessInstanceService;
import com.googlecode.starflow.engine.service.IWorkItemService;
import com.googlecode.starflow.engine.service.impl.ActivityInstService;
import com.googlecode.starflow.engine.service.impl.FreeFlowService;
import com.googlecode.starflow.engine.service.impl.ProcessDefineService;
import com.googlecode.starflow.engine.service.impl.ProcessInstanceService;
import com.googlecode.starflow.engine.service.impl.WorkItemService;
import com.googlecode.starflow.engine.support.DefaultExecutorService;
import com.googlecode.starflow.engine.transaction.SpringTransactionPolicy;
import com.googlecode.starflow.engine.transaction.TransactionTemplate;
import com.googlecode.starflow.service.filter.ProcessFilter;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ProcessEngine {
	private Logger logger = LoggerFactory.getLogger(ProcessEngine.class);
	
	public static final String TRANSACTION_MANAGER_NAME = "transactionManager";
	private IProcessDefineService processDefineService;
	private IProcessInstanceService processInstanceService; 
	private IActivityInstService activityInstService;
	private IFreeFlowService freeFlowService;
	private IWorkItemService workItemService;

	private ApplicationContext applicationContext;
	private final TransactionTemplate transactionTemplate;
	
	private Configuration configuration;
	
	/**
	 * 执行自动环节Action
	 */
	private ExecutorService executorService = new DefaultExecutorService();
	
	/**
	 * 提供给流程和环节运行的外部接口，可以直接new的方式添加进来，也可以spring配置的方式
	 */
	private List<ProcessFilter> processFilters = new CopyOnWriteArrayList<ProcessFilter>();
	
	protected ProcessEngine(ApplicationContext applicationContext, Configuration configuration) {
		Assert.notNull(applicationContext);
		this.applicationContext = applicationContext;
		this.configuration = configuration;
		
		//获取事务Bean
		PlatformTransactionManager transactionManager = null;
		if(this.applicationContext.containsBean(TRANSACTION_MANAGER_NAME) && 
				configuration.getBoolean(Environment.IS_USE_TRANSACTION)) {
			transactionManager = (PlatformTransactionManager)this.applicationContext.getBean(TRANSACTION_MANAGER_NAME);
		} else {
			logger.warn("Process Engine no Transaction");
		}
		SpringTransactionPolicy transactionPolicy = new SpringTransactionPolicy(transactionManager);
		transactionTemplate = new TransactionTemplate(transactionPolicy);
		
		this.processDefineService = new ProcessDefineService(this);
		this.processInstanceService = new ProcessInstanceService(this);
		this.activityInstService = new ActivityInstService(this);
		this.freeFlowService = new FreeFlowService(this);
		this.workItemService = new WorkItemService(this);
		
		//spring方式获取
		Map<String, ProcessFilter> map = this.applicationContext.getBeansOfType(ProcessFilter.class);
		Iterator<String> iterator = map.keySet().iterator();
		while(iterator.hasNext()) {
			processFilters.add(map.get(iterator.next()));
		}
	}
		
	public void addFilter(ProcessFilter filter) {
		processFilters.add(filter);
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public List<ProcessFilter> getProcessFilters() {
		return processFilters;
	}
	
	public IProcessDefineService getProcessDefineService() {
		return processDefineService;
	}

	public IProcessInstanceService getProcessInstanceService() {
		return processInstanceService;
	}

	public IActivityInstService getActivityInstService() {
		return activityInstService;
	}
	
	public IFreeFlowService getFreeFlowService() {
		return freeFlowService;
	}

	public IWorkItemService getWorkItemService() {
		return workItemService;
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
}
