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

package com.googlecode.starflow.engine.core;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public interface Constants {
	
	/**
	 * 子流程标志
	 */
	public static String FLOW_IS_SUBFLOW = "Y";
	
	/**
	 * 非子流程标志
	 */
	public static String FLOW_ISNOT_SUBFLOW = "N";
	
	//-------------------------------------------------
	/**
	 * 全部分支
	 */
	public static String SPLIT_ALL = "AND";
	
	/**
	 * 多路分支
	 */
	public static String SPLIT_MULTI = "OR";
	
	/**
	 * 单一分支
	 */
	public static String SPLIT_SINGLE = "XOR";
	
	/**
	 * 全部聚合
	 */
	public static String JOIN_ALL = "AND";
	
	/**
	 * 多路聚合
	 */
	public static String JOIN_MULTI = "OR";
	
	/**
	 * 单一聚合
	 */
	public static String JOIN_SINGLE = "XOR";
	
	//------------------------------------------------
	
	/**
	 * 开始环节
	 */
	public static String ACT_TYPE_START = "start";
	
	/**
	 * 结束环节
	 */
	public static String ACT_TYPE_END = "finish";
	
	/**
	 * 人工环节
	 */
	public static String ACT_TYPE_MANUL = "manual";
	
	/**
	 * 自动环节 
	 */
	public static String ACT_TYPE_TOOLAPP = "toolApp";
	
	/**
	 * 子流程环节
	 */
	public static String ACT_TYPE_SUBPROCESS = "subProcess";
	
	//------------------------------------------------
	
	/**
	 * 单一工作项
	 */
	public static String WORKITEM_SINGLE = "single";
	
	/**
	 * 多工作项
	 */
	public static String WORKITEM_MULTI = "multi";
	
	/**
	 * 工作项类型
	 */
	public static String WORKITEM_MAN_TYPE = "man";
	
	//------------------------------------------------
	
	/**
	 * 参与者类型 -- 个人
	 */
	public static String PARTICIPANT_TYPE_PERSON = "person";
	
	/**
	 * 参与者类型 -- 机构
	 */
	public static String PARTICIPANT_TYPE_ORG = "organization";
		
	/**
	 * 参与者类型 -- 角色
	 */
	public static String PARTICIPANT_TYPE_ROLE = "role";
	
	/**
	 * 指定固定参与者(机构与角色)
	 */
	public static String PARTICIPANT_ORG_ROLE = "org-role";
	
	/**
	 * 环节参与者与流程实例启动者一样
	 */
	public static String PARTICIPANT_PROCESS_STARTER = "process-starter";
	
	/**
	 * 参与者（活动执行者）
	 */
	public static String PARTICIPANT_ACT_EXECUTER = "act-executer";
	
	/**
	 * 参与者（业务逻辑）
	 */
	public static String PARTICIPANT_ACT_LOGIC = "act-logic";
	
	/**
	 * 参与者（相关数据区）
	 */
	public static String PARTICIPANT_ACT_RELDATA = "relevantdata";
	
	//--------------------------自由流三种策略--------------------------------
	
	/**
	 * 在流程范围内任意自由
	 */
	public static String Free_Act_strategy_One = "freeWithinProcess";
	
	/**
	 * 在指定活动列表范围内自由
	 */
	public static String Free_Act_strategy_two = "freeWithinActivityList";
	
	/**
	 * 在后继活动范围内自由
	 */
	public static String Free_Act_strategy_three = "freeWithinNextActivites";
	
	//---------------------------自动环节----------------------------------
	
	/**
	 * 自动环节同步调用业务逻辑
	 */
	public static String ACT_AUTO_CALL_SYN = "synchronous";
	
	/**
	 * 自动环节异步调用业务逻辑
	 */
	public static String ACT_AUTO_CALL_ASYN = "asynchronous";
	
	/**
	 * 自动环节自动结束环节
	 */
	public static String ACT_ATUO_FINISH_TOOLAPP = "toolApp";
	
	/**
	 * 自动环节人工结束环节
	 */
	public static String ACT_ATUO_FINISH_MAN = "manual";
	
	//----------------------------多工作项----------------------------------
	
	/**
	 * 工作项完成工作规则：全部
	 */
	public static String ACT_WI_FINISHRULE_ALL = "all";
	
	/**
	 * 工作项完成工作规则：百分比
	 */
	public static String ACT_WI_FINISHRULE_PERCENT = "specifyPercent";
	
	/**
	 * 工作项完成工作规则：百分比
	 */
	public static String ACT_WI_FINISHRULE_NUM = "specifyNum";
	
	/**
	 * 多工作项分配策略：按参与者设置个数领取工作项
	 */
	public static String ACT_WI_NUM_PARTICIPANT = "participant-number";
	
	/**
	 * 多工作项分配策略：按操作员个数分配工作项
	 */
	public static String ACT_WI_NUM_OPERATOR = "operator-number";

	/**
	 * 环节激活规则：直接运行
	 */
	public static String ACT_ACTIVATE_RULE_RUN = "directRunning";
	/**
	 * 环节激活规则：待激活
	 */
	public static String ACT_ACTIVATE_RULE_WAIT = "waitingActivition";
	/**
	 * 环节激活规则：待激活
	 */
	public static String ACT_ACTIVATE_RULE_LOGIC = "startStrategybyApp";
	
	/**
	 * 环节重启规则：最初参与者
	 */
	public static String ACT_RESTART_ORIGINAL = "originalParticipant";
	/**
	 * 环节重启规则：最终参与者
	 */
	public static String ACT_RESTART_FINAL = "finalParticipant";
	
	//---------------------------自动环节----------------------------------
	/**
	 * 子流程同步调用
	 */
	public static String ACT_SUBPROCESS_SYN = "synchronous";
	
	/**
	 * 子流程异步调用
	 */
	public static String ACT_SUBPROCESS_ASYN = "asynchronous";
	
	/**
	 * 自动环节事务类型：join
	 */
	public static String ACT_TRANSACTION_JOIN = "join";
	
	/**
	 * 自动环节事务类型：suspend
	 */
	public static String ACT_TRANSACTION_SUSPEND = "suspend";
	
	/**
	 * 环节异常策略：回滚异常
	 */
	public static String ACT_EXCEPTIONSTRATEGY_ROLLBACK= "rollback";
	
	/**
	 * 环节异常策略：忽略异常
	 */
	public static String ACT_EXCEPTIONSTRATEGY_IGNORE= "ignore";
	
	/**
	 * 环节异常策略：进入异常状态，等待人工干预
	 */
	public static String ACT_EXCEPTIONSTRATEGY_INTERRUPT= "interrupt";
	
	/**
	 * 环节异常策略：自动执行单步回退，活动终止
	 */
	public static String ACT_EXCEPTIONSTRATEGY_STEPROLLBACK= "stepRollback";
	
	/**
	 * 环节异常策略：自动执行规则逻辑
	 */
	public static String ACT_EXCEPTIONSTRATEGY_APPLICATION= "application";
}
