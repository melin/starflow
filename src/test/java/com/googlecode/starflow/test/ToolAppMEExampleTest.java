package com.googlecode.starflow.test;

import org.junit.Test;

import com.googlecode.starflow.engine.model.ProcessInstance;

/**
 * 串行流程模式，没有分支。自动环节自动结束，异步调用自动环节
 * 开始->处理->自动归档->结束
 * 
 * @author bsli123@gmail.com
 *
 */
public class ToolAppMEExampleTest extends AbstractFlowTest {
	@Test
	public void testFlow() {
		procDefService.deployProcessFile("classpath:flow/ToolAppMEExample.xml");

		long start = System.currentTimeMillis();
			
		//启动流程
		ProcessInstance processInstance = procInstService.createProcess("flow.ToolAppMEExample", "100001");
		//创建流程
		procInstService.startProcess(processInstance.getProcessInstId());
		
		workItemService.finishWorkItem(1l, "100001");
		
		//手动结束【自动归档】环节
		activityInstService.activateActivity(3l);
		//activityInstService.finishActivity(3l);
		
		long end = System.currentTimeMillis();
		System.out.println("总用时：" + (end - start) + "毫秒");
	}
}
