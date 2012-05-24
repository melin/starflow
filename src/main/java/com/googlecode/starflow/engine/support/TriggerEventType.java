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

/**
 * 流程和环节触发事件类型
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface TriggerEventType {
	//流程启动前
	public String BEFORE_START_PROC = "before-start-proc";
	
	//流程启动后
	public String AFTER_START_PROC = "after-start-proc";
	
	//流程完成前
	public String BEFORE_COMPLETE_PROC = "before-complete-proc";
	
	//流程结束后
	public String AFTER_COMPLETE_PROC = "after-complete-proc";
	
	//活动启动前
	public String BEFORE_START_ACT = "before-start-act";
	
	//活动启动后
	public String AFTER_START_ACT = "after-start-act";
	
	//活动完成前
	public String BEFORE_COMPLETE_ACT = "before-complete-act";
	
	//活动完成后
	public String AFTER_COMPLETE_ACT = "after-complete-act";
	
	//工作项创建后
	public String AFTER_CREATE_WI = "after-create-wi";
	
	//工作项领取后
	public String AFTER_GET_WI = "after-get-wi";
	
	//工作项取消领取后
	public String AFTER_CANEL_WI = "after-canel-wi";
	
	//工作项完成前
	public String BEFORE_COMPLETE_WI = "before-complete-wi";
	
	//工作项完成后
	public String AFTER_COMPLETE_WI = "after-complete-wi";
}
