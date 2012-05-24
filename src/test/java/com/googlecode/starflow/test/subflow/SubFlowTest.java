package com.googlecode.starflow.test.subflow;

import org.junit.Test;

import com.googlecode.starflow.engine.model.ProcessInstance;
import com.googlecode.starflow.test.AbstractFlowTest;

public class SubFlowTest extends AbstractFlowTest {
	
	@Test
	public void testFlow() {
		//部署流程
		procDefService.deployProcessFile("classpath:flow/subflow/MainFlow.xml");
		procDefService.deployProcessFile("classpath:flow/subflow/SubFlow.xml");
		
		long start = System.currentTimeMillis();
		
		//启动流程
		ProcessInstance processInstance = procInstService.createProcess("test.MainFlow", "100001");
		//创建流程
		procInstService.startProcess(processInstance.getProcessInstId());
		
		workItemService.finishWorkItem(1L, "100001");
		workItemService.finishWorkItem(2L, "100001");
		
		long end = System.currentTimeMillis();
		System.out.println("总用时：" + (end - start) + "毫秒");
		//总用时：13641毫秒
	}
}
