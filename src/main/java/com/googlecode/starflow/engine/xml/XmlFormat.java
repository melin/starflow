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

package com.googlecode.starflow.engine.xml;

import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 
 * @author libinsong1204@gmail.com
 * @version 1.0
 */
public class XmlFormat {
	/**
	 * 用dom4j格式化XML字符串为'pretty'格式.等价于getPrettyStiring(doc,"gb2312");
	 * 
	 * @param xml
	 *            要格式化的XML字符串.
	 * @return 格式化以后的XML字符串.
	 * @see XmlFormat#getPrettyStiring(String, String)
	 */
	public static String getPrettyString(String xml) {
		try {
			return getPrettyString(xml, "GB2312");
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("Pretty Xml 出现错误");
		}
	}

	/**
	 * 用dom4j格式化XML字符串为'pretty'格式.
	 * 
	 * @param xml
	 *            要格式化的XML字符串.
	 * @param encoding
	 *            编码方式.
	 * @return 格式化以后的XML字符串.如果输入为null或空字符串,则不做任何处理.
	 */
	public static String getPrettyString(String xml, String encoding)
			throws DocumentException {
		if (xml == null || xml.trim().length() < 1) {
			return xml;
		}

		Document doc = DocumentHelper.parseText(xml);
		return getPrettyString(doc, encoding);
	}

	/**
	 * 用dom4j的'pretty'格式格式化dom为字符串.
	 * 
	 * @param doc
	 *            要格式化的dom.
	 * @param encoding
	 *            编码方式.
	 * @return 格式化以后的XML字符串.如果输入为null或空字符串,则不做任何处理.
	 */
	public static String getPrettyString(Document doc, String encoding) {
		StringWriter writer = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		if (encoding == null || "".equals(encoding.trim())) {
			encoding = "GBK";
		}
		format.setEncoding(encoding);
		XMLWriter xmlwriter = new XMLWriter(writer, format);

		try {
			xmlwriter.write(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return writer.toString();
	}
}