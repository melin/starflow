package com.googlecode.starflow.test;

import org.junit.Test;

import com.googlecode.starflow.engine.model.ProcessInstance;

/**
 * 串行流程模式，没有分支。测试自动环节的事务处理功能。
 * 开始->人工环节->自动环节->结束
 * 
 * @author bsli123@gmail.com
 *
 */
public class ToolAppTransactionExampleTest extends AbstractFlowTest {
	@Test
	public void testFlow() {
		//部署流程
		procDefService.deployProcessFile("classpath:flow/ToolAppTransactionExample.xml");
		
		long start = System.currentTimeMillis();
			
		//启动流程
		ProcessInstance processInstance = procInstService.createProcess("flow.ToolAppTransactionExample", "100001");
		//创建流程
		procInstService.startProcess(processInstance.getProcessInstId());
		
		workItemService.finishWorkItem(1l, "100001");
		
		long end = System.currentTimeMillis();
		System.out.println("总用时：" + (end - start) + "毫秒");
	}
}
