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

package com.googlecode.starflow.engine;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import com.googlecode.starflow.core.resource.Environment;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class Configuration {
	private final static AtomicBoolean initialized = new AtomicBoolean(false);
	private static Properties _properties;
	private static Configuration configuration;
	
	private Configuration() {
	}
	
	private static void checkInitialized() {
        if (configuration == null && initialized.compareAndSet(false, true)) {
        	configuration = new Configuration();
        	_properties = Environment.getProperties();
        }
    }
	
	public static Configuration getInstance() {
		checkInitialized();
		return configuration;
	}

	/**
	 * Get all properties
	 */
	public Properties getProperties() {
		return _properties;
	}

	/**
	 * Specify a completely new set of properties
	 */
	public Configuration setProperties(Properties properties) {
		_properties = properties;
		return this;
	}

	/**
	 * Set the given properties
	 */
	public Configuration addProperties(Properties extraProperties) {
		_properties.putAll( extraProperties );
		return this;
	}

	/**
	 * Adds the incoming properties to the internap properties structure,
	 * as long as the internal structure does not already contain an
	 * entry for the given key.
	 *
	 * @param properties
	 * @return this
	 */
	@SuppressWarnings("rawtypes")
	public Configuration mergeProperties(Properties properties) {
		Iterator itr = properties.entrySet().iterator();
		while ( itr.hasNext() ) {
			final Map.Entry entry = ( Map.Entry ) itr.next();
			if ( _properties.containsKey( entry.getKey() ) ) {
				continue;
			}
			_properties.setProperty( ( String ) entry.getKey(), ( String ) entry.getValue() );
		}
		return this;
	}

	/**
	 * Set a property
	 */
	public Configuration setProperty(String propertyName, String value) {
		_properties.setProperty( propertyName, value );
		return this;
	}

	/**
	 * Get a property
	 */
	public String getProperty(String propertyName) {
		return _properties.getProperty( propertyName );
	}
	
	
	public boolean getBoolean(String propertyName) {
		String value = _properties.getProperty( propertyName );
		if("false".equals(value))
			return false;
		else
			return true;
	}
	
	public int getInt(String propertyName) {
		String value = _properties.getProperty( propertyName );
		return Integer.parseInt(value);
	}
	
	public long getLong(String propertyName) {
		String value = _properties.getProperty( propertyName );
		return Long.parseLong(value);
	}

}
