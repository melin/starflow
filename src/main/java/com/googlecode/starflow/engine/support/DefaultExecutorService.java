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

package com.googlecode.starflow.engine.support;

import java.util.concurrent.Callable;

import com.googlecode.starflow.core.util.ExecutorServiceHelper;
import com.googlecode.starflow.core.util.SynchronousExecutorService;
import com.googlecode.starflow.engine.ExecutorService;
import com.googlecode.starflow.engine.core.Constants;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class DefaultExecutorService implements ExecutorService {
	private final java.util.concurrent.ExecutorService syncExecutor = new SynchronousExecutorService(); 
	private java.util.concurrent.ExecutorService asyncExecutor;
	
	public DefaultExecutorService() {
		asyncExecutor = ExecutorServiceHelper.newThreadPool(null, "", 10, 20, 1024);
	}

	public DefaultExecutorService(
			java.util.concurrent.ExecutorService asyncExecutor) {
		this.asyncExecutor = asyncExecutor;
	}

	@Override
	public Object execute(Callable<Object> tasker, String invokePattern) throws Exception {
		if(Constants.ACT_AUTO_CALL_SYN.equalsIgnoreCase(invokePattern)) {
			return syncExecutor.submit(tasker).get();
		} else {
			asyncExecutor.submit(tasker);
			return null;
		}
	}
}
