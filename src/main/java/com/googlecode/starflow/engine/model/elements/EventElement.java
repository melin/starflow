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

package com.googlecode.starflow.engine.model.elements;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class EventElement {
	public String eventType;
	public String action;
	public String invokePattern;
	public String transactionType;
	public String exceptionStrategy;
	
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getInvokePattern() {
		return invokePattern;
	}
	public void setInvokePattern(String invokePattern) {
		this.invokePattern = invokePattern;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getExceptionStrategy() {
		return exceptionStrategy;
	}
	public void setExceptionStrategy(String exceptionStrategy) {
		this.exceptionStrategy = exceptionStrategy;
	}
}
