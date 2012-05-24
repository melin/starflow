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

package com.googlecode.starflow.core.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.starflow.engine.ProcessEngineException;


/**
 *
 * @author Gavin King
 */
public final class Environment {
	public static final String PARIMARY_KEY_CACHE_NUM = "com.googlecode.starflow.core.key.cache.num";
	
	public static final String IS_USE_WEBSERVICE = "com.googlecode.starflow.use.webservice";
	
	public static final String IS_USE_TRANSACTION = "com.googlecode.starflow.use.transaction";
	
	public static final String RELA_DATA_MANAGER_TYPE = "com.googlecode.starflow.relaData.type";

	private static final Properties GLOBAL_PROPERTIES;

	private static final Logger log = LoggerFactory.getLogger(Environment.class);

	static {
		GLOBAL_PROPERTIES = new Properties();

		try {
			InputStream stream = ConfigHelper.getResourceAsStream("/starflow.properties");
			try {
				GLOBAL_PROPERTIES.load(stream);
			}
			catch (Exception e) {
				log.error("problem loading properties from hibernate.properties");
			}
			finally {
				try{
					stream.close();
				}
				catch (IOException ioe){
					log.error("could not close stream on hibernate.properties", ioe);
				}
			}
		}
		catch (ProcessEngineException he) {
			log.info("starflow.properties not found");
		}

		try {
			GLOBAL_PROPERTIES.putAll( System.getProperties() );
		}
		catch (SecurityException se) {
			log.warn("could not copy system properties, system properties will be ignored");
		}

	}

	/**
	 * Disallow instantiation
	 */
	private Environment() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return <tt>System</tt> properties, extended by any properties specified
	 * in <tt>hibernate.properties</tt>.
	 * @return Properties
	 */
	public static Properties getProperties() {
		Properties copy = new Properties();
		copy.putAll(GLOBAL_PROPERTIES);
		return copy;
	}
}
