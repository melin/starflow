package com.googlecode.starflow.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.googlecode.starflow.engine.core.RelaDataManagerBuilder;
import com.googlecode.starflow.engine.core.data.RelaDataManager;
import com.googlecode.starflow.engine.model.ProcessInstance;

/**
 * 流程开始环节进行分支，使用复杂表达式计算，来判断走哪条分支。
 * 		 固话处理班	 
 * 开始->        ->归档->结束
 *       电话处理班
 * 
 * @author bsli123@gmail.com
 *
 */
public class StartActSplitXpathExampleTest extends AbstractFlowTest {
	@Test
	public void testFlow() {
		//部署流程
		procDefService.deployProcessFile("classpath:flow/StartActSplitXpathExample.xml");
		
		long start = System.currentTimeMillis();
		
		//启动流程
		ProcessInstance processInstance = procInstService.createProcess("flow.StartActSplitXpathExample", "100001");
		
		Map<String, Object> conditions = new HashMap<String, Object>();
		//宽带处理班
		conditions.put("flag", "<flag>ADSL</flag>"); 
		RelaDataManager relaDataManager = RelaDataManagerBuilder.buildRelaDataManager();
		long processInstId = processInstance.getProcessInstId();
		String activityDefId = "act_start";
		relaDataManager.setExpressConditions(processInstId, activityDefId, conditions);
		
		//创建流程
		procInstService.startProcess(processInstance.getProcessInstId());
		
		workItemService.finishWorkItem(1l, "100001");
		workItemService.finishWorkItem(2l, "100001");
		
		long end = System.currentTimeMillis();
		System.out.println("总用时：" + (end - start) + "毫秒");
		//总用时：13641毫秒
	}
}
