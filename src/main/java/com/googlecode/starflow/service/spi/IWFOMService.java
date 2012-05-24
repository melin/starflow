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

package com.googlecode.starflow.service.spi;

import java.util.List;

import com.googlecode.starflow.engine.model.Participant;

/**
 * 组织机构接口
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface IWFOMService {
	/**
	 * 获取机构和角色的下面相关人员
	 * 
	 * @param type 结构或角色（organization && role）
	 * @param id 机构和角色的id
	 * @return List<Participant>
	 */
	public List<Participant> getParticipants(String type, String id);
}
