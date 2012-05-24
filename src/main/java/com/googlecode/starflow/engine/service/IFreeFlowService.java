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

package com.googlecode.starflow.engine.service;

import java.util.List;

import com.googlecode.starflow.engine.model.elements.FreeActElement;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface IFreeFlowService {
	
	/**
	 * 判断环节定义ID所对应的活动是否是自由活动
	 * 
	 * @param processDefId
	 * @param activityDefId
	 * @return
	 */
	public boolean isFreeActivity(long processDefId, String activityDefId);
	
	/**
	 * 根据定义的设置，获取某自由活动可能的后继活动
	 * 
	 * @param processDefId
	 * @param activityDefId
	 * @return 
	 */
	public List<FreeActElement> queryPossibleNextActsOfFreeActivity(long processDefId, String activityDefId);

}
