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
import com.googlecode.starflow.engine.core.split.AllSplitMode;
import com.googlecode.starflow.engine.core.split.MultiSplitMode;
import com.googlecode.starflow.engine.core.split.SingleSplitMode;
import com.googlecode.starflow.engine.core.split.SplitMode;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class SplitModeFactory {
	
	public static SplitMode buildSplitStrategy(String mode) {
		if(Constants.SPLIT_ALL.equalsIgnoreCase(mode))
			return new AllSplitMode();
		else if(Constants.SPLIT_MULTI.equalsIgnoreCase(mode))
			return new MultiSplitMode();
		else if(Constants.SPLIT_SINGLE.equalsIgnoreCase(mode))
			return new SingleSplitMode();
		else 
			throw new ProcessEngineException("聚合模式值不正确，Mode="+mode);
	}
}
