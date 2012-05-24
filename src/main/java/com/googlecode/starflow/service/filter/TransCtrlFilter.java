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

package com.googlecode.starflow.service.filter;

import java.util.Date;

import com.googlecode.starflow.core.key.Keys;
import com.googlecode.starflow.core.util.PrimaryKeyUtil;
import com.googlecode.starflow.engine.event.AbstractFlowEvent;
import com.googlecode.starflow.engine.event.ActivityCreateEvent;
import com.googlecode.starflow.engine.model.ActivityInst;
import com.googlecode.starflow.engine.model.TransCtrl;
import com.googlecode.starflow.engine.model.elements.ActivityElement;
import com.googlecode.starflow.engine.repository.IActivityInstRepository;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class TransCtrlFilter extends ProcessFilterAdapter {
	
	@Override
	public void activityCreate(AbstractFlowEvent event, ActivityInst destActInst) {
		if(event instanceof ActivityCreateEvent) {
			IActivityInstRepository actInstRep = event.getActInstRep(); 
			ActivityInst srcActInst = ((ActivityCreateEvent)event).getActivityInst();
			
			//调用startActivityInst方法直接启动环节
			if(srcActInst == null)
				srcActInst = destActInst;
			
			TransCtrl transCtrl = new TransCtrl();
			transCtrl.setSrcActDefId(srcActInst.getActivityDefId());
			transCtrl.setSrcActDefName(srcActInst.getActivityInstName());
			transCtrl.setSrcActType(srcActInst.getActivityType());
			transCtrl.setDestActDefId(destActInst.getActivityDefId());
			transCtrl.setDestActDefName(destActInst.getActivityInstName());
			transCtrl.setDestActType(destActInst.getActivityType());
			transCtrl.setTransTime(new Date());
			transCtrl.setProcessInstId(destActInst.getProcessInstId());
			transCtrl.setIsUse("N");
			transCtrl.setIsStartDestAct("N");
			
			long transCtrlId = PrimaryKeyUtil.getPrimaryKey(Keys.TRANSCTRLID);
			transCtrl.setTransCtrlId(transCtrlId);
			
			actInstRep.insertTransCtrl(transCtrl);
		}
	}
	
	public void saveTransCtrlCanNotAct(AbstractFlowEvent event, ActivityElement activityXml) {
		if(event instanceof ActivityCreateEvent) {
			IActivityInstRepository actInstRep = event.getActInstRep(); 
			ActivityInst srcActInst = ((ActivityCreateEvent)event).getActivityInst();
			
			TransCtrl transCtrl = new TransCtrl();
			transCtrl.setSrcActDefId(srcActInst.getActivityDefId());
			transCtrl.setSrcActDefName(srcActInst.getActivityInstName());
			transCtrl.setSrcActType(srcActInst.getActivityType());
			transCtrl.setDestActDefId(activityXml.getId());
			transCtrl.setDestActDefName(activityXml.getName());
			transCtrl.setDestActType(activityXml.getType());
			transCtrl.setTransTime(new Date());
			transCtrl.setProcessInstId(srcActInst.getProcessInstId());
			transCtrl.setIsUse("N");
			transCtrl.setIsStartDestAct("Y");
			
			long transCtrlId = PrimaryKeyUtil.getPrimaryKey(Keys.TRANSCTRLID);
			transCtrl.setTransCtrlId(transCtrlId);
			
			actInstRep.insertTransCtrl(transCtrl);
		}
	}
}
