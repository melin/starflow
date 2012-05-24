package com.googlecode.starflow.test;

import org.junit.Test;

import com.googlecode.starflow.engine.model.ProcessInstance;

/**
 * 演示环节启动策略：待激活
 * 
 * @author bsli123@gmail.com
 *
 */
public class ActivateRuleWaitExampleTest extends AbstractFlowTest {
	
	@Test
	public void testFlow() {
		//部署流程
		procDefService.deployProcessFile("classpath:flow/ActivateRuleWaitExample.xml");
		
		long start = System.currentTimeMillis();
		long j = 1;
			
		//启动流程
		ProcessInstance processInstance = procInstService.createProcess("flow.ActivateRuleWaitExample", "100001");
		//创建流程
		procInstService.startProcess(processInstance.getProcessInstId());
		
		//激活人工环节
		activityInstService.activateActivity(2L);
		workItemService.finishWorkItem(j++, "100001");
		
		long end = System.currentTimeMillis();
		System.out.println("总用时：" + (end - start) + "毫秒");
	}
}
