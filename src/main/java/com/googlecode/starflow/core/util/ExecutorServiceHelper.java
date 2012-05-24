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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helper for {@link java.util.concurrent.ExecutorService} to construct executors using a thread factory that
 * create thread names with Camel prefix.
 * <p/>
 * This helper should <b>NOT</b> be used by end users of Camel, as you should use
 * {@link org.apache.camel.spi.ExecutorServiceStrategy} which you obtain from {@link org.apache.camel.CamelContext}
 * to create thread pools.
 * <p/>
 * This helper should only be used internally in Camel.
 *
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public final class ExecutorServiceHelper {

    public static final String DEFAULT_PATTERN = "StarFlow Thread ${counter} - ${name}";
    private static AtomicInteger threadCounter = new AtomicInteger();

    private ExecutorServiceHelper() {
    }

    private static int nextThreadCounter() {
        return threadCounter.getAndIncrement();
    }

    /**
     * Creates a new thread name with the given prefix
     *
     * @param pattern the pattern
     * @param name    the name
     * @return the thread name, which is unique
     */
    public static String getThreadName(String pattern, String name) {
        if (pattern == null) {
            pattern = DEFAULT_PATTERN;
        }

        // the name could potential have a $ sign we want to keep
        if (name.indexOf("$") > -1) {
            name = name.replaceAll("\\$", "CAMEL_REPLACE_ME");
        }

        // we support ${longName} and ${name} as name placeholders
        String longName = name;
        String shortName = name.contains("?") ? before(name, "?") : name;

        String answer = pattern.replaceFirst("\\$\\{counter\\}", "" + nextThreadCounter());
        answer = answer.replaceFirst("\\$\\{longName\\}", longName);
        answer = answer.replaceFirst("\\$\\{name\\}", shortName);
        if (answer.indexOf("$") > -1 || answer.indexOf("${") > -1 || answer.indexOf("}") > -1) {
            throw new IllegalArgumentException("Pattern is invalid: " + pattern);
        }

        if (answer.indexOf("CAMEL_REPLACE_ME") > -1) {
            answer = answer.replaceAll("CAMEL_REPLACE_ME", "\\$");
        }

        return answer;
    }
    
    private static String before(String text, String before) {
        if (!text.contains(before)) {
            return null;
        }
        return text.substring(0, text.indexOf(before));
    }

    /**
     * Creates a new scheduled thread pool which can schedule threads.
     *
     * @param poolSize the core pool size
     * @param pattern  pattern of the thread name
     * @param name     ${name} in the pattern name
     * @param daemon   whether the threads is daemon or not
     * @return the created pool
     */
    public static ScheduledExecutorService newScheduledThreadPool(final int poolSize, final String pattern, final String name, final boolean daemon) {
        return Executors.newScheduledThreadPool(poolSize, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread answer = new Thread(r, getThreadName(pattern, name));
                answer.setDaemon(daemon);
                return answer;
            }
        });
    }

    /**
     * Creates a new fixed thread pool.
     * <p/>
     * Beware that the task queue is unbounded
     *
     * @param poolSize the fixed pool size
     * @param pattern  pattern of the thread name
     * @param name     ${name} in the pattern name
     * @param daemon   whether the threads is daemon or not
     * @return the created pool
     */
    public static ExecutorService newFixedThreadPool(final int poolSize, final String pattern, final String name, final boolean daemon) {
        return Executors.newFixedThreadPool(poolSize, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread answer = new Thread(r, getThreadName(pattern, name));
                answer.setDaemon(daemon);
                return answer;
            }
        });
    }

    /**
     * Creates a new single thread pool (usually for background tasks)
     *
     * @param pattern pattern of the thread name
     * @param name    ${name} in the pattern name
     * @param daemon  whether the threads is daemon or not
     * @return the created pool
     */
    public static ExecutorService newSingleThreadExecutor(final String pattern, final String name, final boolean daemon) {
        return Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread answer = new Thread(r, getThreadName(pattern, name));
                answer.setDaemon(daemon);
                return answer;
            }
        });
    }

    /**
     * Creates a new synchronous executor service which always executes the task in the call thread
     * (its just a thread pool facade)
     *
     * @return the created pool
     * @see org.apache.camel.util.concurrent.SynchronousExecutorService
     */
    public static ExecutorService newSynchronousThreadPool() {
        return new SynchronousExecutorService();
    }

    /**
     * Creates a new custom thread pool using 60 seconds as keep alive and with an unbounded queue.
     *
     * @param pattern      pattern of the thread name
     * @param name         ${name} in the pattern name
     * @param corePoolSize the core size
     * @param maxPoolSize  the maximum pool size
     * @return the created pool
     */
    public static ExecutorService newThreadPool(final String pattern, final String name, int corePoolSize, int maxPoolSize) {
        return ExecutorServiceHelper.newThreadPool(pattern, name, corePoolSize, maxPoolSize, 60,
                TimeUnit.SECONDS, -1, new ThreadPoolExecutor.CallerRunsPolicy(), true);
    }

    /**
     * Creates a new custom thread pool using 60 seconds as keep alive and with bounded queue.
     *
     * @param pattern      pattern of the thread name
     * @param name         ${name} in the pattern name
     * @param corePoolSize the core size
     * @param maxPoolSize  the maximum pool size
     * @param maxQueueSize the maximum number of tasks in the queue, use <tt>Integer.MAX_VALUE</tt> or <tt>-1</tt> to indicate unbounded
     * @return the created pool
     */
    public static ExecutorService newThreadPool(final String pattern, final String name, int corePoolSize, int maxPoolSize, int maxQueueSize) {
        return ExecutorServiceHelper.newThreadPool(pattern, name, corePoolSize, maxPoolSize, 60,
                TimeUnit.SECONDS, maxQueueSize, new ThreadPoolExecutor.CallerRunsPolicy(), true);
    }

    /**
     * Creates a new custom thread pool
     *
     * @param pattern                  pattern of the thread name
     * @param name                     ${name} in the pattern name
     * @param corePoolSize             the core size
     * @param maxPoolSize              the maximum pool size
     * @param keepAliveTime            keep alive time
     * @param timeUnit                 keep alive time unit
     * @param maxQueueSize             the maximum number of tasks in the queue, use <tt>Integer.MAX_VALUE</tt> or <tt>-1</tt> to indicate unbounded
     * @param rejectedExecutionHandler the handler for tasks which cannot be executed by the thread pool.
     *                                 If <tt>null</tt> is provided then {@link java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy CallerRunsPolicy} is used.
     * @param daemon                   whether the threads is daemon or not
     * @return the created pool
     * @throws IllegalArgumentException if parameters is not valid
     */
    public static ExecutorService newThreadPool(final String pattern, final String name, int corePoolSize, int maxPoolSize,
                                                long keepAliveTime, TimeUnit timeUnit, int maxQueueSize,
                                                RejectedExecutionHandler rejectedExecutionHandler, final boolean daemon) {

        // validate max >= core
        if (maxPoolSize < corePoolSize) {
            throw new IllegalArgumentException("MaxPoolSize must be >= corePoolSize, was " + maxPoolSize + " >= " + corePoolSize);
        }

        BlockingQueue<Runnable> queue;
        if (corePoolSize == 0 && maxQueueSize <= 0) {
            // use a synchronous queue
            queue = new SynchronousQueue<Runnable>();
            // and force 1 as pool size to be able to create the thread pool by the JDK
            corePoolSize = 1;
            maxPoolSize = 1;
        } else if (maxQueueSize <= 0) {
            // unbounded task queue
            queue = new LinkedBlockingQueue<Runnable>();
        } else {
            // bounded task queue
            queue = new LinkedBlockingQueue<Runnable>(maxQueueSize);
        }
        ThreadPoolExecutor answer = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, queue);
        answer.setThreadFactory(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread answer = new Thread(r, getThreadName(pattern, name));
                answer.setDaemon(daemon);
                return answer;
            }
        });
        if (rejectedExecutionHandler == null) {
            rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        }
        answer.setRejectedExecutionHandler(rejectedExecutionHandler);
        return answer;
    }
}
