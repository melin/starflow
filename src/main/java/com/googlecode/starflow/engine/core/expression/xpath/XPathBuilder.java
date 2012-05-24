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

package com.googlecode.starflow.engine.core.expression.xpath;

import java.io.StringReader;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

public class XPathBuilder {
	private static Logger logger = LoggerFactory.getLogger(XPathBuilder.class); 
	private final Queue<XPathExpression> pool = new ConcurrentLinkedQueue<XPathExpression>();

	public static final String SOAP_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
	
	private DefaultNamespaceContext namespaceContext;
	private XPathFactory xpathFactory;
	private final String text;
	private String objectModelUri;
	
	public XPathBuilder(String text) {
        this.text = text;
    }

    public static XPathBuilder xpath(String text) {
        return new XPathBuilder(text);
    }
	
	/**
     * Evaluates the given xpath using the provided body as a String return type.
     *
     * @param context the camel context
     * @param body the body
     * @return result of the evaluation
     */
    public String evaluate(String xml) {
        String answer = evaluateAs(xml);
        return answer;
    }
    
    /**
     * Evaluates the expression as the given result type
     */
    protected String evaluateAs(String xml) {
        // pool a pre compiled expression from pool
        XPathExpression xpathExpression = pool.poll();
        if (xpathExpression == null) {
        	logger.trace("Creating new XPathExpression as none was available from pool");
            // no avail in pool then create one
            try {
                xpathExpression = createXPathExpression();
            } catch (XPathExpressionException e) {
            	e.printStackTrace();
            	throw new InvalidXPathExpression(getText(), e);
            } catch (Exception e) {
                throw new InvalidXPathExpression("Cannot create xpath expression", e);
            }
        } else {
        	logger.trace("Acquired XPathExpression from pool");
        }
        try {
            return doInEvaluateAs(xpathExpression, xml);
        } finally {
            // release it back to the pool
            pool.add(xpathExpression);
            logger.trace("Released XPathExpression back to pool");
        }
    }
    
    protected String doInEvaluateAs(XPathExpression xpathExpression, String xml) {
        String answer = null;

        try {
            Object document = getDocument(xml);
            if (document instanceof InputSource) {
                InputSource inputSource = (InputSource) document;
                answer = xpathExpression.evaluate(inputSource);
            }
        } catch (XPathExpressionException e) {
            throw new InvalidXPathExpression(getText(), e);
        }

        if (logger.isTraceEnabled()) {
        	logger.trace("Done evaluating xml: " + xml + " with result: " + answer);
        }
        return answer;
    }
    
    protected XPathExpression createXPathExpression() throws XPathExpressionException, XPathFactoryConfigurationException {
        XPath xPath = getXPathFactory().newXPath();

        xPath.setNamespaceContext(getNamespaceContext());
        return xPath.compile(text);
    }
    
    /**
     * Registers the namespace prefix and URI with the builder so that the
     * prefix can be used in XPath expressions
     *
     * @param prefix is the namespace prefix that can be used in the XPath
     *                expressions
     * @param uri is the namespace URI to which the prefix refers
     * @return the current builder
     */
    public XPathBuilder namespace(String prefix, String uri) {
        getNamespaceContext().add(prefix, uri);
        return this;
    }
    
    public DefaultNamespaceContext getNamespaceContext() {
        if (namespaceContext == null) {
            try {
                DefaultNamespaceContext defaultNamespaceContext = new DefaultNamespaceContext(getXPathFactory());
                populateDefaultNamespaces(defaultNamespaceContext);
                namespaceContext = defaultNamespaceContext;
            } catch (XPathFactoryConfigurationException e) {
                throw new InvalidXPathExpression(null, e);
            }
        }
        return namespaceContext;
    }

    public void setNamespaceContext(DefaultNamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
    }
    
    @SuppressWarnings("rawtypes")
	public XPathFactory getXPathFactory() throws XPathFactoryConfigurationException {
        if (xpathFactory == null) {
            if (objectModelUri != null) {
            	logger.info("Using objectModelUri " + objectModelUri + " when creating XPathFactory");
                xpathFactory = XPathFactory.newInstance(objectModelUri);
                return xpathFactory;
            }

            // read system property and see if there is a factory set
            Properties properties = System.getProperties();
            for (Map.Entry prop : properties.entrySet()) {
                String key = (String) prop.getKey();
                if (key.startsWith(XPathFactory.DEFAULT_PROPERTY_NAME)) {
                    String uri = after(key, ":");
                    if (uri != null) {
                    	logger.info("Using system property " + key + " with value: " + prop.getValue() + " when creating XPathFactory");
                        xpathFactory = XPathFactory.newInstance(uri);
                        return xpathFactory;
                    }
                }
            }

            if (xpathFactory == null) {
            	logger.debug("Creating default XPathFactory");
                xpathFactory = XPathFactory.newInstance();
            }
        }
        return xpathFactory;
    }
    
    public String after(String text, String after) {
        if (!text.contains(after)) {
            return null;
        }
        return text.substring(text.indexOf(after) + after.length());
    }

    public void setXPathFactory(XPathFactory xpathFactory) {
        this.xpathFactory = xpathFactory;
    }
    
    public String getText() {
        return text;
    }
    
    /**
     * Strategy method to extract the document from the exchange.
     */
    protected Object getDocument(String xml) {
        Object answer = new InputSource(new StringReader(xml));
        return answer;
    }
    
    /**
     * Lets populate a number of standard prefixes if they are not already there
     */
    protected void populateDefaultNamespaces(DefaultNamespaceContext context) {
        setNamespaceIfNotPresent(context, "soap", SOAP_NAMESPACE);
    }
    
    protected void setNamespaceIfNotPresent(DefaultNamespaceContext context, String prefix, String uri) {
        if (context != null) {
            String current = context.getNamespaceURI(prefix);
            if (current == null) {
                context.add(prefix, uri);
            }
        }
    }
}
