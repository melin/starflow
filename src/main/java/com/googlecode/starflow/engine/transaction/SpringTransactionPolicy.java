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

package com.googlecode.starflow.engine.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.DefaultTransactionDefinition;


/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-7
 * @version 
 */
public class SpringTransactionPolicy implements TransactedPolicy {
	protected final Logger logger = LoggerFactory.getLogger(SpringTransactionPolicy.class);
	
	private PlatformTransactionManager transactionManager;
	
	public SpringTransactionPolicy(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

	@Override
	public TransactionStatus begin() {
		if(transactionManager != null) {
			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus txStatus = transactionManager.getTransaction(definition);
			return txStatus;
		} else {
			return null;
		}
	}
	
	@Override
	public TransactionStatus begin(int propagationBehavior) {
		if(transactionManager != null) {
			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
			definition.setPropagationBehavior(propagationBehavior);
			TransactionStatus txStatus = transactionManager.getTransaction(definition);
			return txStatus;
		} else {
			return null;
		}
	}

	@Override
	public void commit(TransactionStatus txStatus) {
		if(transactionManager !=null && txStatus != null) {
			transactionManager.commit(txStatus);
		}
	}

	
	@Override
	public void rollbackOnException(TransactionStatus txStatus, Throwable ex) throws TransactionException {
		logger.debug("Initiating transaction rollback on application exception", ex);
		try {
			if(transactionManager !=null && txStatus != null) {
				this.transactionManager.rollback(txStatus);
			}
		} catch (TransactionSystemException ex2) {
			logger.error("Application exception overridden by rollback exception", ex);
			ex2.initApplicationException(ex);
			throw ex2;
		} catch (RuntimeException ex2) {
			logger.error("Application exception overridden by rollback exception", ex);
			throw ex2;
		} catch (Error err) {
			logger.error("Application exception overridden by rollback error", ex);
			throw err;
		}
	}
}
