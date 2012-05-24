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
 * 环节实例重启测试
 * 
 * @author bsli123@gmail.com
 *
 */
public class RestartActivityExampleTest extends AbstractFlowTest {
	
	@Test
	public void testFlow() {
		//部署流程
		procDefService.deployProcessFile("classpath:flow/RestartActivityExample.xml");
		
		long start = System.currentTimeMillis();
		long j = 1;
		for(int i=0; i<1; i++) {
			
			//启动流程
			ProcessInstance processInstance = procInstService.createProcess("flow.RestartActivityExample", "100001");
			//创建流程
			procInstService.startProcess(processInstance.getProcessInstId());
			
			//List<WorkItem> workItems = workItemService.queryPersonWorkItems("melin");
			workItemService.finishWorkItem(j++, "910150");
			
			activityInstService.restartActivity(1, 2);
			System.out.println("---------------------"+i+"次------------------------------");
		}
		long end = System.currentTimeMillis();
		System.out.println("总用时：" + (end - start) + "毫秒");
	}
}
