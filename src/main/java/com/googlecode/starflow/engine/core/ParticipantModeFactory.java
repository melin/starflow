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

import com.googlecode.starflow.engine.ProcessEngineException;
import com.googlecode.starflow.engine.core.participant.ParticipantMode;
import com.googlecode.starflow.engine.core.participant.impl.ActExecuterParticipantMode;
import com.googlecode.starflow.engine.core.participant.impl.ActLogicParticipantMode;
import com.googlecode.starflow.engine.core.participant.impl.OrgRoleParticipantMode;
import com.googlecode.starflow.engine.core.participant.impl.ProcStarterParticipantMode;
import com.googlecode.starflow.engine.core.participant.impl.RelDataParticipantMode;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class ParticipantModeFactory {
	
	public static ParticipantMode buildParticipantMode(String mode) {
		if(Constants.PARTICIPANT_ORG_ROLE.equalsIgnoreCase(mode))
			return new OrgRoleParticipantMode();
		else if(Constants.PARTICIPANT_PROCESS_STARTER.equalsIgnoreCase(mode))
			return new ProcStarterParticipantMode();
		else if(Constants.PARTICIPANT_ACT_EXECUTER.equalsIgnoreCase(mode))
			return new ActExecuterParticipantMode();
		else if(Constants.PARTICIPANT_ACT_LOGIC.equalsIgnoreCase(mode))
			return new ActLogicParticipantMode();
		else if(Constants.PARTICIPANT_ACT_RELDATA.equalsIgnoreCase(mode))
			return new RelDataParticipantMode();
		else
			throw new ProcessEngineException("参与者模式值不正确，Mode="+mode);
	}
}
