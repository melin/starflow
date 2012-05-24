/**
 * Copyright (c) 2012,USTC E-BUSINESS TECHNOLOGY CO.LTD All Rights Reserved.
 */

package com.googlecode.starflow.core;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.googlecode.starflow.core.util.PrimaryKeyUtil;

/**
 * @author bsli@starit.com.cn
 * @date 2012-3-20 下午8:55:03
 */
public class KeyTest {
	private JdbcTemplate jdbcTemplate = null;
	private ExecutorService executorService;
	final static CountDownLatch end = new CountDownLatch(10);
	
	public static void main(String[] args) {
		KeyTest keyTest = new KeyTest();
		keyTest.init();
		keyTest.test();
	}
	
	public void init() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"keytest.xml");

		jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
		jdbcTemplate.update("UPDATE WF_PRIMARYKEY SET code = 0 WHERE name = 'test' ");
		
		executorService = Executors.newFixedThreadPool(100);
	}
	
	public void test() {
		for(int i=0; i<100; i++) {
			executorService.execute(new Task(jdbcTemplate));
		}
		
		try {
			end.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static class Task implements Runnable {
		private JdbcTemplate _jdbcTemplate = null;
		
		public Task(JdbcTemplate jdbcTemplate) {
			_jdbcTemplate = jdbcTemplate;
		}

		public void run() {
			for(int j=10; j<1000; j ++) {
				_jdbcTemplate.update("insert into keytest values(?)", 
						PrimaryKeyUtil.getPrimaryKey("test"));
			}
			end.countDown();
		}
		
	}
}
