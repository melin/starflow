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

package com.googlecode.starflow.engine.model.elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.googlecode.starflow.engine.model.Participant;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ActivityElement implements Serializable {
	private String id;
	private String type;
	private String name;
	private String participantType;
	private Boolean isAllowAppointParticipants;
	private String particiLogic;
	private String particiSpecActID;
	private Boolean isFreeActivity;
	private String freeRangeStrategy;
	private Boolean isOnlyLimitedManualActivity;
	private String wiMode;
	private Boolean isSequentialExecute;
	private String workitemNumStrategy;
	private String finishRule;
	private double finishRequiredPercent;
	private int finishRquiredNum;
	private Boolean isAutoCancel;
	private long limitTime;
	private String splitMode;
	private String joinMode;
	
	//自动环节
	private String finishType;
	private String invokePattern;
	private String transactionType;
	private String exceptionStrategy;
	private String exceptionAction;
	private String executeAction;
	
	//子流程
	private String subProcess;
	private String spInvokePattern;
	
	private List<EventElement> events = new ArrayList<EventElement>();
	private List<FreeActElement> freeActs;
	private List<Participant> participants;
	private List<OperationElement> operations;
	private String action;
	private String description;
	
	//启动策略
	private String activateRuleType;
	private String startStrategybyAppAction;
	private String resetParticipant;
	
	//环节扩展属性
	private Map<String, String> properties;
	
	private String left;
	private String top;
	
	List<TransitionElement> beforeTrans = new CopyOnWriteArrayList<TransitionElement>();
	List<TransitionElement> afterTrans = new CopyOnWriteArrayList<TransitionElement>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParticipantType() {
		return participantType;
	}
	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}
	public String getParticiLogic() {
		return particiLogic;
	}
	public void setParticiLogic(String particiLogic) {
		this.particiLogic = particiLogic;
	}
	public String getParticiSpecActID() {
		return particiSpecActID;
	}
	public void setParticiSpecActID(String particiSpecActID) {
		this.particiSpecActID = particiSpecActID;
	}
	public Boolean getIsFreeActivity() {
		return isFreeActivity;
	}
	public void setIsFreeActivity(Boolean isFreeActivity) {
		this.isFreeActivity = isFreeActivity;
	}
	public String getFreeRangeStrategy() {
		return freeRangeStrategy;
	}
	public void setFreeRangeStrategy(String freeRangeStrategy) {
		this.freeRangeStrategy = freeRangeStrategy;
	}
	public Boolean getIsOnlyLimitedManualActivity() {
		return isOnlyLimitedManualActivity;
	}
	public void setIsOnlyLimitedManualActivity(Boolean isOnlyLimitedManualActivity) {
		this.isOnlyLimitedManualActivity = isOnlyLimitedManualActivity;
	}
	public String getWiMode() {
		return wiMode;
	}
	public void setWiMode(String wiMode) {
		this.wiMode = wiMode;
	}
	public String getWorkitemNumStrategy() {
		return workitemNumStrategy;
	}
	public void setWorkitemNumStrategy(String workitemNumStrategy) {
		this.workitemNumStrategy = workitemNumStrategy;
	}
	public String getFinishRule() {
		return finishRule;
	}
	public void setFinishRule(String finishRule) {
		this.finishRule = finishRule;
	}
	public double getFinishRequiredPercent() {
		return finishRequiredPercent;
	}
	public void setFinishRequiredPercent(double finishRequiredPercent) {
		this.finishRequiredPercent = finishRequiredPercent;
	}
	public int getFinishRquiredNum() {
		return finishRquiredNum;
	}
	public void setFinishRquiredNum(int finishRquiredNum) {
		this.finishRquiredNum = finishRquiredNum;
	}
	public long getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(long limitTime) {
		this.limitTime = limitTime;
	}
	public String getSplitMode() {
		return splitMode;
	}
	public void setSplitMode(String splitMode) {
		this.splitMode = splitMode;
	}
	public String getJoinMode() {
		return joinMode;
	}
	public void setJoinMode(String joinMode) {
		this.joinMode = joinMode;
	}
	public String getFinishType() {
		return finishType;
	}
	public void setFinishType(String finishType) {
		this.finishType = finishType;
	}
	public String getExecuteAction() {
		return executeAction;
	}
	public void setExecuteAction(String executeAction) {
		this.executeAction = executeAction;
	}
	public String getSubProcess() {
		return subProcess;
	}
	public void setSubProcess(String subProcess) {
		this.subProcess = subProcess;
	}
	public List<EventElement> getEvents() {
		return events;
	}
	public void setEvents(List<EventElement> events) {
		this.events = events;
	}
	public List<FreeActElement> getFreeActs() {
		return freeActs;
	}
	public void setFreeActs(List<FreeActElement> freeActs) {
		this.freeActs = freeActs;
	}
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public List<TransitionElement> getBeforeTrans() {
		return beforeTrans;
	}
	public void setBeforeTrans(List<TransitionElement> beforeTrans) {
		this.beforeTrans = beforeTrans;
	}
	public List<TransitionElement> getAfterTrans() {
		return afterTrans;
	}
	public void setAfterTrans(List<TransitionElement> afterTrans) {
		this.afterTrans = afterTrans;
	}
	public List<OperationElement> getOperations() {
		return operations;
	}
	public void setOperations(List<OperationElement> operations) {
		this.operations = operations;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getActivateRuleType() {
		return activateRuleType;
	}
	public void setActivateRuleType(String activateRuleType) {
		this.activateRuleType = activateRuleType;
	}
	public String getStartStrategybyAppAction() {
		return startStrategybyAppAction;
	}
	public void setStartStrategybyAppAction(String startStrategybyAppAction) {
		this.startStrategybyAppAction = startStrategybyAppAction;
	}
	public String getResetParticipant() {
		return resetParticipant;
	}
	public void setResetParticipant(String resetParticipant) {
		this.resetParticipant = resetParticipant;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getExceptionStrategy() {
		return exceptionStrategy;
	}
	public void setExceptionStrategy(String exceptionStrategy) {
		this.exceptionStrategy = exceptionStrategy;
	}
	public String getExceptionAction() {
		return exceptionAction;
	}
	public void setExceptionAction(String exceptionAction) {
		this.exceptionAction = exceptionAction;
	}
	public String getInvokePattern() {
		return invokePattern;
	}
	public void setInvokePattern(String invokePattern) {
		this.invokePattern = invokePattern;
	}
	public String getSpInvokePattern() {
		return spInvokePattern;
	}
	public void setSpInvokePattern(String spInvokePattern) {
		this.spInvokePattern = spInvokePattern;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	public Boolean getIsAllowAppointParticipants() {
		return isAllowAppointParticipants;
	}
	public void setIsAllowAppointParticipants(Boolean isAllowAppointParticipants) {
		this.isAllowAppointParticipants = isAllowAppointParticipants;
	}
	public Boolean getIsSequentialExecute() {
		return isSequentialExecute;
	}
	public void setIsSequentialExecute(Boolean isSequentialExecute) {
		this.isSequentialExecute = isSequentialExecute;
	}
	public Boolean getIsAutoCancel() {
		return isAutoCancel;
	}
	public void setIsAutoCancel(Boolean isAutoCancel) {
		this.isAutoCancel = isAutoCancel;
	}
}
