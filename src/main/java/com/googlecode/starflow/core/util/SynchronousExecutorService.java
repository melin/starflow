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

package com.googlecode.starflow.core.util;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A synchronous {@link java.util.concurrent.ExecutorService} which always invokes
 * the task in the caller thread (just a thread pool facade).
 * <p/>
 * There is no task queue, and no thread pool. The task will thus always be executed
 * by the caller thread in a fully synchronous method invocation.
 * <p/>
 * This implementation is very simple and does not support waiting for tasks to complete during shutdown.
 *
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class SynchronousExecutorService extends AbstractExecutorService {

    private volatile boolean shutdown;

    public void shutdown() {
        shutdown = true;
    }

    public List<Runnable> shutdownNow() {
        return null;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public boolean isTerminated() {
        return shutdown;
    }

    public boolean awaitTermination(long time, TimeUnit unit) throws InterruptedException {
        return true;
    }

    public void execute(Runnable runnable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
    	SyncFuture<T> future = new SyncFuture<T>();
    	T object;
		try {
			object = callable.call();
			future.set(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	return future;
    }
    
	private static class SyncFuture<T> implements Future<T> {
    	private T result = null;

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			throw new UnsupportedOperationException();
		}

		@Override
		public T get() throws InterruptedException, ExecutionException {
			return result;
		}
		
		public void set(T result) {
			this.result = result;
		}

		@Override
		public T get(long timeout, TimeUnit unit)
				throws InterruptedException, ExecutionException,
				TimeoutException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isCancelled() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isDone() {
			throw new UnsupportedOperationException();
		}
    }
}