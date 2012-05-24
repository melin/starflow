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

package com.googlecode.starflow.engine.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import com.googlecode.starflow.core.key.Keys;
import com.googlecode.starflow.core.util.PrimaryKeyUtil;
import com.googlecode.starflow.engine.ProcessEngine;
import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.StarFlowState;
import com.googlecode.starflow.engine.model.ProcessDefine;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.model.elements.OperationElement;
import com.googlecode.starflow.engine.model.elements.TransitionElement;
import com.googlecode.starflow.engine.repository.IProcessDefineRepository;
import com.googlecode.starflow.engine.service.IProcessDefineService;
import com.googlecode.starflow.engine.transaction.TransactionCallback;
import com.googlecode.starflow.engine.transaction.TransactionCallbackWithoutResult;
import com.googlecode.starflow.engine.transaction.TransactionTemplate;
import com.googlecode.starflow.engine.xml.Dom4jProcDefParser;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ProcessDefineService implements IProcessDefineService {
	
	private IProcessDefineRepository procDefRep;
	private TransactionTemplate transactionTemplate;
	 

	public ProcessDefineService(ProcessEngine processEngine) {
		Assert.notNull(processEngine);
		
		this.procDefRep = processEngine.getApplicationContext().getBean(IProcessDefineRepository.class);
		this.transactionTemplate = processEngine.getTransactionTemplate();
	}
	
	public void insertProcessDefine(final ProcessDefine processDefine) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				procDefRep.inertProcessDefine(processDefine);
			}
		});
	}
	
	public void updateProcessDefine(final ProcessDefine processDefine) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				procDefRep.updateProcessDefine(processDefine);
			}
		});
	}
	
	public ProcessDefine findProcessDefine(long processDefId) {
		return procDefRep.findProcessDefine(processDefId);
	}
	
	public List<ProcessDefine> findProcessDefines(String processDefName) {
		return procDefRep.findProcessDefines(processDefName);
	}
	
	public Boolean isUniqueProcessDefine(final String processDefName, final String versionSign) {
		return transactionTemplate.execute(new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				List<ProcessDefine> list = procDefRep.findProcessDefines(processDefName);
				for(ProcessDefine processDefine : list) {
					if(processDefine.getVersionSign().equals(versionSign)) {
						return false;
					}
				}
				return true;
			}
			
		});
	}

	public void deployProcessXML(final String processDefContent) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				ProcessDefine processDefine = new ProcessDefine();
				long id = PrimaryKeyUtil.getPrimaryKey(Keys.PROCESSDEFID);
				processDefine.setProcessDefId(id);
				
				processDefine.setProcessDefContent(processDefContent);
				Dom4jProcDefParser.parserProcessInfo(processDefine);
				processDefine.setCreateTime(new Date());
				processDefine.setCurrentState(StarFlowState.PROCESS_DEF_PUBLISH);
				
				procDefRep.inertProcessDefine(processDefine);
			}
		});
	}
	
	public void deployProcessFile(String resourceLocation) {
		try {
			File file = ResourceUtils.getFile(resourceLocation);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file) {
				@Override
				public int read() throws IOException {
					return 0;
				}
			}, "UTF-8"));
			
			StringBuilder sb = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			deployProcessXML(sb.toString());
		} catch (FileNotFoundException e1) {
			throw new ProcessEngineException("流程文件没有查找到", e1);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}
	
	public void deleteProcessDefine(final long processDefId) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				procDefRep.deleteProcessDefine(processDefId);
			}
		});
	}
	
	public void publishProcessDefine(final String processDefName, final long processDefId) {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			public void doInTransactionWithoutResult(TransactionStatus status) {
				procDefRep.updateProcessDefineUnPublishStatus(processDefName);
				procDefRep.updateProcessDefinePublishStatus(processDefId);
			}
		});
	}
	
	public Map<String, String> getProcessProperties(final Long processDefId) {
		return transactionTemplate.execute(new TransactionCallback<Map<String, String>>() {

			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {
				ProcessDefine processDefine = procDefRep.findProcessDefine(processDefId);
				return processDefine.getProcessElement().getProperties();
			}
		});
	}
	
	public Map<String, String> getActivityProperties(final Long processDefId, final String activityDefId) {
		return transactionTemplate.execute(new TransactionCallback<Map<String, String>>() {

			@Override
			public Map<String, String> doInTransaction(TransactionStatus status) {
				ProcessDefine processDefine = procDefRep.findProcessDefine(processDefId);
				ActivityElement activityElement = processDefine.getProcessElement().getActivitys().get(activityDefId);
				return activityElement.getProperties();
			}
		});
	}
	
	public List<OperationElement> getActivityOperations(final Long processDefId, final String activityDefId) {
		return transactionTemplate.execute(new TransactionCallback<List<OperationElement>>() {

			@Override
			public List<OperationElement> doInTransaction(TransactionStatus status) {
				ProcessDefine processDefine = procDefRep.findProcessDefine(processDefId);
				ActivityElement activityElement = processDefine.getProcessElement().getActivitys().get(activityDefId);
				return activityElement.getOperations();
			}
		});
	}
	
	public String getActivityAction(final Long processDefId, final String activityDefId) {
		return transactionTemplate.execute(new TransactionCallback<String>() {

			@Override
			public String doInTransaction(TransactionStatus status) {
				ProcessDefine processDefine = procDefRep.findProcessDefine(processDefId);
				ActivityElement activityElement = processDefine.getProcessElement().getActivitys().get(activityDefId);
				return activityElement.getAction();
			}
		});
	}
	
	public List<ActivityElement> findBeforeActivities(final Long processDefId, final String activityDefId) {
		return transactionTemplate.execute(new TransactionCallback<List<ActivityElement>>() {

			@Override
			public List<ActivityElement> doInTransaction(TransactionStatus status) {
				ProcessDefine processDefine = procDefRep.findProcessDefine(processDefId);
				ActivityElement activityElement = processDefine.getProcessElement().getActivitys().get(activityDefId);
				
				List<TransitionElement> beforeTrans = activityElement.getBeforeTrans();

				List<ActivityElement> list = new ArrayList<ActivityElement>();
				for(TransitionElement transitionXml :  beforeTrans) {
					list.add(processDefine.getProcessElement().getActivitys().get(transitionXml.getFrom()));
				}
				
				return list;
			}
		});
	}
	
	public List<ActivityElement> findAfterActivities(final Long processDefId, final String activityDefId) {
		return transactionTemplate.execute(new TransactionCallback<List<ActivityElement>>() {

			@Override
			public List<ActivityElement> doInTransaction(TransactionStatus status) {
				ProcessDefine processDefine = procDefRep.findProcessDefine(processDefId);
				ActivityElement activityElement = processDefine.getProcessElement().getActivitys().get(activityDefId);
				
				List<TransitionElement> afterTrans = activityElement.getAfterTrans();

				List<ActivityElement> list = new ArrayList<ActivityElement>();
				for(TransitionElement transitionXml :  afterTrans) {
					list.add(processDefine.getProcessElement().getActivitys().get(transitionXml.getTo()));
				}
				
				return list;
			}
		});
	}
}
