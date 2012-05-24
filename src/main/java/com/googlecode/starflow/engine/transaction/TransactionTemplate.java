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

import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * 
 * @author libinsong1204@gmail.com
 * @date 2011-1-7
 * @version
 */
public class TransactionTemplate {
	private TransactedPolicy transactedPolicy;
	
	public TransactionTemplate(TransactedPolicy transactedPolicy) {
		this.transactedPolicy = transactedPolicy;
	}

	public <T> T execute(TransactionCallback<T> action)
			throws TransactionException {
		TransactionStatus status = transactedPolicy.begin();
		T result = null;
		try {
			result = action.doInTransaction(status);
		} catch (RuntimeException ex) {
			transactedPolicy.rollbackOnException(status, ex);
			throw ex;
		} catch (Error err) {
			transactedPolicy.rollbackOnException(status, err);
			throw err;
		}
		transactedPolicy.commit(status);
		return result;
	}
	
	public <T> T execute(int propagationBehavior, TransactionCallback<T> action)
			throws TransactionException {
		TransactionStatus status = transactedPolicy.begin(propagationBehavior);
		T result = null;
		try {
			result = action.doInTransaction(status);
		} catch (RuntimeException ex) {
			transactedPolicy.rollbackOnException(status, ex);
			throw ex;
		} catch (Error err) {
			transactedPolicy.rollbackOnException(status, err);
			throw err;
		}
		transactedPolicy.commit(status);
		return result;
	}
}
