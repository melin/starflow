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

package com.googlecode.starflow.engine.core.activity;

import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.elements.ActivityElement;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface ActivityType { 
	/**
	 * 创建环节实例
	 * 
	 * @param event
	 * @param activityXml
	 * @return
	 */
	public ActivityInst createActivity(AbstractFlowEvent event, ActivityElement activityXml);
	
	/**
	 * 如果当前环节设置为多工作项模式，结束当前环节时，判断是够可以结束当前环节
	 * false 不可以结束当前环节
	 * tru 可以结束当前环节
	 * 
	 * @param event
	 * @param activityXml
	 * @return 
	 */
	public boolean isCompleteActivity(AbstractFlowEvent event, ActivityElement activityXml);
}
