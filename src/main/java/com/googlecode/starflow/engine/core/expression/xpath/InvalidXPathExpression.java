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

import com.googlecode.starflow.engine.core.expression.RuntimeExpressionException;

/**
 * An exception thrown if am XPath expression could not be parsed or evaluated
 *
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class InvalidXPathExpression extends RuntimeExpressionException {
    private static final long serialVersionUID = 9171451033826915273L;

    private final String xpath;

    public InvalidXPathExpression(String xpath, Exception e) {
        super("Invalid xpath: " + xpath + ". Reason: " + e, e);
        this.xpath = xpath;
    }

    public String getXpath() {
        return xpath;
    }
}
