/*
 * @(#)DummyServer.java
 *
 * Copyright (c) 2015 KW iTech, Inc.
 * All rights reserved.
 */
package com.study.chapter1.service;

/** 
 * Class desciption here.
 *
 * @author jong-yeol Park (jypark@kwinternational.com)
 * @version 1.0
 */
public interface ResponseHandler {
	/** onSuccess */
	public boolean onSuccess(int statusCode, String content);

	/** onFailed, false is show error msg alert */
	public boolean onFailure( Throwable error, int statusCode, String content);

	/** onFinish */
	public void onFinish();

	/** network is disable */
	public boolean onUnavailableNetwork();

}

