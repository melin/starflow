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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.starflow.engine.ProcessEngineException;

/**
 * A simple class to centralize logic needed to locate config files on the system.
 *
 * @author Steve
 */
public final class ConfigHelper {
	private static final Logger log = LoggerFactory.getLogger(ConfigHelper.class);

	/** Try to locate a local URL representing the incoming path.  The first attempt
	 * assumes that the incoming path is an actual URL string (file://, etc).  If this
	 * does not work, then the next attempts try to locate this UURL as a java system
	 * resource.
	 *
	 * @param path The path representing the config location.
	 * @return An appropriate URL or null.
	 */
	public static final URL locateConfig(final String path) {
		try {
			return new URL(path);
		}
		catch(MalformedURLException e) {
			return findAsResource(path);
		}
	}

	/** 
	 * Try to locate a local URL representing the incoming path.  
	 * This method <b>only</b> attempts to locate this URL as a 
	 * java system resource.
	 *
	 * @param path The path representing the config location.
	 * @return An appropriate URL or null.
	 */
	public static final URL findAsResource(final String path) {
		URL url = null;

		// First, try to locate this resource through the current
		// context classloader.
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		if (contextClassLoader!=null) {
			url = contextClassLoader.getResource(path);
		}
		if (url != null)
			return url;

		// Next, try to locate this resource through this class's classloader
		url = ConfigHelper.class.getClassLoader().getResource(path);
		if (url != null)
			return url;

		// Next, try to locate this resource through the system classloader
		url = ClassLoader.getSystemClassLoader().getResource(path);

		// Anywhere else we should look?
		return url;
	}

	/** Open an InputStream to the URL represented by the incoming path.  First makes a call
	 * to {@link #locateConfig(java.lang.String)} in order to find an appropriate URL.
	 * {@link java.net.URL#openStream()} is then called to obtain the stream.
	 *
	 * @param path The path representing the config location.
	 * @return An input stream to the requested config resource.
	 * @throws HibernateException Unable to open stream to that resource.
	 */
	public static final InputStream getConfigStream(final String path) throws ProcessEngineException {
		final URL url = ConfigHelper.locateConfig(path);

		if (url == null) {
			String msg = "Unable to locate config file: " + path;
			log.error( msg );
			throw new ProcessEngineException(msg);
		}

		try {
			return url.openStream();
        }
		catch(IOException e) {
	        throw new ProcessEngineException("Unable to open config file: " + path, e);
        }
	}

	/** Open an Reader to the URL represented by the incoming path.  First makes a call
	 * to {@link #locateConfig(java.lang.String)} in order to find an appropriate URL.
	 * {@link java.net.URL#openStream()} is then called to obtain a stream, which is then
	 * wrapped in a Reader.
	 *
	 * @param path The path representing the config location.
	 * @return An input stream to the requested config resource.
	 * @throws HibernateException Unable to open reader to that resource.
	 */
	public static final Reader getConfigStreamReader(final String path) throws ProcessEngineException {
		return new InputStreamReader( getConfigStream(path) );
	}

	/** Loads a properties instance based on the data at the incoming config location.
	 *
	 * @param path The path representing the config location.
	 * @return The loaded properties instance.
	 * @throws HibernateException Unable to load properties from that resource.
	 */
	public static final Properties getConfigProperties(String path) throws ProcessEngineException {
		try {
			Properties properties = new Properties();
			properties.load( getConfigStream(path) );
			return properties;
		}
		catch(IOException e) {
			throw new ProcessEngineException("Unable to load properties from specified config file: " + path, e);
		}
	}
	
	private ConfigHelper() {}

	public static InputStream getResourceAsStream(String resource) {
		String stripped = resource.startsWith("/") ?
				resource.substring(1) : resource;

		InputStream stream = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader!=null) {
			stream = classLoader.getResourceAsStream( stripped );
		}
		if ( stream == null ) {
			stream = Environment.class.getResourceAsStream( resource );
		}
		if ( stream == null ) {
			stream = Environment.class.getClassLoader().getResourceAsStream( stripped );
		}
		if ( stream == null ) {
			throw new ProcessEngineException( resource + " not found" );
		}
		return stream;
	}


	public static InputStream getUserResourceAsStream(String resource) {
		boolean hasLeadingSlash = resource.startsWith( "/" );
		String stripped = hasLeadingSlash ? resource.substring(1) : resource;

		InputStream stream = null;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if ( classLoader != null ) {
			stream = classLoader.getResourceAsStream( resource );
			if ( stream == null && hasLeadingSlash ) {
				stream = classLoader.getResourceAsStream( stripped );
			}
		}

		if ( stream == null ) {
			stream = Environment.class.getClassLoader().getResourceAsStream( resource );
		}
		if ( stream == null && hasLeadingSlash ) {
			stream = Environment.class.getClassLoader().getResourceAsStream( stripped );
		}

		if ( stream == null ) {
			throw new ProcessEngineException( resource + " not found" );
		}

		return stream;
	}

}
