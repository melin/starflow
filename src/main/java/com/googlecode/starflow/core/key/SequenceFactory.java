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

package com.googlecode.starflow.core.key;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class SequenceFactory implements ApplicationContextAware {
	private static Map<String, SingleSequence> singleSequenceMap = new ConcurrentHashMap<String, SingleSequence>();
	private static SequenceFactory factory;
	
	private TransactionTemplate transactionTemplate;
	private UniqueTableApp uniqueTableApp;
	private ApplicationContext context;
	private int cacheKeyNum = 10;
	
	private SequenceFactory() {}
	
	public static SingleSequence getSequence(String name) {
		SingleSequence sequence = (SingleSequence) singleSequenceMap.get(name);
		if (sequence == null) {
			synchronized(SequenceFactory.class) { 
				if (sequence == null) {
					sequence = factory.createSequence(name);
					singleSequenceMap.put(name, sequence);
				}
			}
		}
		return sequence;
	}

	private SingleSequence createSequence(String name) {
		int cacheNum = getCacheKeyNum();
		SingleSequence sequence = new SingleSequence(cacheNum, uniqueTableApp, transactionTemplate);
		return sequence;
	}

	public void setUniqueTableApp(UniqueTableApp uniqueTableApp) {
		this.uniqueTableApp = uniqueTableApp;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
		factory = this.context.getBean(SequenceFactory.class);
	}

	public int getCacheKeyNum() {
		return cacheKeyNum;
	}

	public void setCacheKeyNum(int cacheKeyNum) {
		this.cacheKeyNum = cacheKeyNum;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
}