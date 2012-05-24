/**
 * Copyright (c) 2012,USTC E-BUSINESS TECHNOLOGY CO.LTD All Rights Reserved.
 */

package com.googlecode.starflow.engine.service;

import java.util.List;

import com.googlecode.starflow.engine.model.Participant;

/**
 * @author bsli123@hotmail.com
 * @date 2012-5-10 下午4:51:14
 */
public interface IDelegateService {
	public void delegateWorkItem(long workItemId, List<Participant> participants);
	
	public void rejectDelegateWorkItem(long workItemId);
	
	public void withdrawDelegateWorkItem(long workItemId);
}
