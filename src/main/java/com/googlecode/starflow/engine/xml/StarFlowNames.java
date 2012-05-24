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

package com.googlecode.starflow.engine.xml;

public interface StarFlowNames {
	public static String FLOW_ATTR_NAME = "name";
	public static String FLOW_ATTR_VERSION = "version";
	public static String FLOW_CHILD_DESC = "description";
	public static String FLOW_CHILD_LIMITTIME = "limitTime";
	public static String FLOW_ATTR_CHNAME = "chname";
	
	public static String ACT_START_ID = "act_start";
	public static String ACT_END_ID = "act_end";
	
	public static String ACT_ATTR_TYPE = "type";
	public static String ACT_ATTR_ID = "id";
	public static String ACT_ATTR_NAME = "name";
	
	public static String ACT_CHILD_DESC = "description";
	public static String ACT_CHILD_SPLIT = "splitMode";
	public static String ACT_CHILD_JOIN = "joinMode";
	public static String ACT_CHILD_LIMITTIME = "limitTime";
	public static String ACT_CHILD_SUBPROCESS = "subProcess";
	public static String ACT_CHILD_SP_INVOKE_PATTERN = "invokePattern";
	public static String ACT_CHILD_PARTICI_MODE = "participantType";
	public static String ACT_CHILD_PARTICI_IS_ALLOW_APPOINT = "isAllowAppointParticipants";
	public static String ACT_CHILD_PARTICIPANT = "Participants/participant";
	public static String ACT_CHILD_PARTICIPANT_ID = "id";
	public static String ACT_CHILD_PARTICIPANT_NAME = "name";
	public static String ACT_CHILD_PARTICIPANT_TYPE = "type";
	public static String ACT_CHILD_PARTICI_ACTID = "particiSpecActID";
	public static String ACT_CHILD_PARTICI_LOGIC = "particiLogic";
	
	//工作项
	public static String ACT_CHILD_WI_MODE = "wiMode";
	public static String ACT_CHILD_WI_IS_SEQ_EXEC = "isSequentialExecute";
	public static String ACT_CHILD_WI_FINISHRULE = "finishRule";
	public static String ACT_CHILD_WI_WORKITEMNUMSTRATEGY = "workitemNumStrategy";
	public static String ACT_CHILD_WI_FINISHREQUIREDPERCENT = "finishRequiredPercent";
	public static String ACT_CHILD_WI_FINISHRQUIREDNUM = "finishRquiredNum";
	public static String ACT_CHILD_WI_IS_AUTO_CANCEL = "isAutoCancel";
	
	//流程启动策略
	public static String ACT_CHILD_ACTIVATE_RULE_TYPE = "activateRuleType";
	public static String ACT_CHILD_STARTSTRATEGYBYAPPACTION = "startStrategybyAppAction";
	public static String ACT_CHILD_RESET_PARTICIPANT = "resetParticipant";
	
	//环节操作
	public static String ACT_CHILD_ACTION = "action";
	public static String ACT_OPERATION = "Operations/operation";
	public static String ACT_OPERATION_ID = "id";
	public static String ACT_OPERATION_CODE = "code";
	public static String ACT_OPERATION_NAME = "name";
	public static String ACT_OPERATION_ACTION = "action";
	
	//自由流相关子节点
	public static String ACT_FREE_ISFREEACT = "isFreeActivity";
	public static String ACT_FREE_ISONLYLIMITEDMANUALACT = "isOnlyLimitedManualActivity";
	public static String ACT_FREE_RANGESTRATEGY = "freeRangeStrategy";
	public static String ACT_FREE_ACT = "FreeActivities/freeActivity";
	public static String ACT_FREE_ACT_ID = "id";
	public static String ACT_FREE_ACT_NAME = "name";
	public static String ACT_FREE_ACT_TYPE = "type";
	
	//自动环节
	public static String ACT_AUTO_FINSISH_TYPE = "finishType";
	public static String ACT_AUTO_INVOKE_PATTERN = "invokePattern";
	public static String ACT_AUTO_TRANSACTION_TYPE = "transactionType";
	public static String ACT_AUTO_EXEC_ACTION = "executeAction";
	public static String ACT_EXCEPTION_STRATEGY = "exceptionStrategy";
	public static String ACT_EXCEPTION_ACTION = "exceptionAction";
	
	//扩展属性
	public static String FLOW_EXT_PROPERTY = "ExtendNodes/extendNode";
	
	public static String TRAN_ATTR_ID = "id";
	public static String TRAN_ATTR_NAME = "name";
	public static String TRAN_ATTR_TO = "to";
	public static String TRAN_ATTR_FROM = "from";
	public static String TRAN_CHILD_ISSIMPLEEXPRESSION = "isSimpleExpression";
	public static String TRAN_CHILD_LEFTVALUE = "leftValue";
	public static String TRAN_CHILD_COMPTYPE = "compType";
	public static String TRAN_CHILD_RIGHTVALUE = "rightValue";
	public static String TRAN_CHILD_COMPLEXEXPRESSIONVALUE = "complexExpressionValue";
	public static String TRAN_CHILD_ISDEFAULT = "isDefault";
	public static String TRAN_CHILD_PRIORITY = "priority";
	
}
