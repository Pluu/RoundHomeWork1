/*
 * @(#)DummyServer.java
 *
 * Copyright (c) 2015 KW iTech, Inc.
 * All rights reserved.
 */
package com.study.chapter1.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpStatus;

import com.study.chapter1.ui.UIHandler;

import android.util.Log;

/**
 * Class desciption here.
 * 
 * @author jong-yeol Park (jypark@kwinternational.com)
 * @version 1.0
 */
public class DummyServer {
	public static final int PROCESSING_DELAY_TIME = 3000;
	public static final int TIME_OUT = 5000;

	public void execute(final String key, final OPCode opCode, final ResponseHandler response) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<String> future = executor.submit( new MyTask(key) );

		try {
			String value = future.get(TIME_OUT, TimeUnit.MILLISECONDS);
			Log.i("", "-----> run: " + key + ", reuslt=" + value);

			if (response != null) {
				response.onSuccess(HttpStatus.SC_OK, value);
			}
		} catch (TimeoutException te) {
			future.cancel(true);
			te.printStackTrace();
			if (response != null) {
				response.onFailure(te, HttpStatus.SC_GATEWAY_TIMEOUT, "TimeoutException..");
			}
		} catch (Exception e) {
			future.cancel(true);
			e.printStackTrace();
			if (response != null) {
				response.onFailure(e, -9000, "Exception..");
			}
		}

		executor.shutdownNow();
		if (response != null) {
			response.onFinish();
		}
	}

	class MyTask implements Callable<String> {
		private String result = "";

		public MyTask(String result) {
			this.result = result;
		}

		@Override
		public String call() throws Exception {
			Thread.sleep(PROCESSING_DELAY_TIME);
			return result;
		}
	}

}
