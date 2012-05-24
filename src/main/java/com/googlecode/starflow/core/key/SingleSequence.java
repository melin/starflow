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

import java.util.concurrent.locks.ReentrantLock;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class SingleSequence {
	private final ReentrantLock lock = new ReentrantLock(false);
	
	protected long currVal = 0L;

	protected long maxVal = 0L;

	private final int cacheNum;

	private final UniqueTableApp app;
	
	private final TransactionTemplate transactionTemplate;

	public SingleSequence(int cacheNum, UniqueTableApp app, TransactionTemplate transactionTemplate) {
		this.cacheNum = cacheNum;
		this.app = app;
		this.transactionTemplate = transactionTemplate;
	}

	public long getNextVal(String name) {
		try {
			lock.lock();
			if (this.currVal < this.maxVal) {
				return (this.currVal++);
			}
			CacheValue cache = getNewValFromDB(name);
			this.currVal = cache.getMinVal();
			this.maxVal = cache.getMaxVal();
			return (this.currVal++);
		} finally {
			lock.unlock();
		}
	}

	private CacheValue getNewValFromDB(final String name) {
		return transactionTemplate.execute(new TransactionCallback<CacheValue>() {

			@Override
			public CacheValue doInTransaction(TransactionStatus status) {
				return app.getCacheValue(cacheNum, name);
			}
		});
	}
}