package com.googlecode.starflow.test;

import org.junit.Test;

import com.googlecode.starflow.engine.model.ProcessInstance;

/**
 * 串行流程模式，没有分支。自动环节自动结束，同步调用自动环节逻辑
 * 开始->处理->自动归档->结束
 * 
 * @author bsli123@gmail.com
 *
 */
public class ToolAppAEExampleTest extends AbstractFlowTest {
	@Test
	public void testFlow() {
		//部署流程
		procDefService.deployProcessFile("classpath:flow/ToolAppAEExample.xml");
		
		long start = System.currentTimeMillis();
			
		//启动流程
		ProcessInstance processInstance = procInstService.createProcess("flow.ToolAppAEExample", "100001");
		//创建流程
		procInstService.startProcess(processInstance.getProcessInstId());
		
		workItemService.finishWorkItem(1l, "100001");
		
		long end = System.currentTimeMillis();
		System.out.println("总用时：" + (end - start) + "毫秒");
	}
}
