/*
 * Copyright 2002-2007 the original author or authors.
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

package com.googlecode.starflow.test;

import org.junit.Test;

import com.googlecode.starflow.engine.model.ProcessInstance;

/**
 * 串行流程模式，没有分支。
 * 开始->申请->审核->结束
 * 
 * @author bsli123@gmail.com
 *
 */
public class StartActivityExampleTest extends AbstractFlowTest {
	@Test
	public void testFlow() {
		//部署流程
		procDefService.deployProcessFile("classpath:flow/StartActivityExample.xml");

		long start = System.currentTimeMillis();
			
			//启动流程
		ProcessInstance processInstance = procInstService.createAndStartProcess("flow.StartActivityExample", "100001");
		activityInstService.startActivityInst(processInstance.getProcessInstId(), "Act_3");
		
		long end = System.currentTimeMillis();
		System.out.println("总用时：" + (end - start) + "毫秒");
		//总用时：13641毫秒
	}
}
