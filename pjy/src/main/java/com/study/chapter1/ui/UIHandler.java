/*
 * @(#)UIHandler.java
 *
 * Copyright (c) 2015 KW iTech, Inc.
 * All rights reserved.
 */
package com.study.chapter1.ui;

import android.graphics.drawable.Drawable.Callback;
import android.os.Handler;
import android.os.Looper;

/**
 * Class desciption here.
 * 
 * @author jong-yeol Park (jypark@kwinternational.com)
 * @version 1.0
 */

public class UIHandler extends Handler {

	private static UIHandler instance;

	public static UIHandler getInstance() {
		if (instance == null)
			instance = new UIHandler();
		return instance;
	}

	public UIHandler() {
		this(Looper.getMainLooper());
	}

	public UIHandler(Callback callback) {
		this(Looper.getMainLooper(), callback);
	}

	public UIHandler(Looper looper, Callback callback) {
		super(looper, callback);
	}

	public UIHandler(Looper looper) {
		super(looper);
	}

	public static void postUiThread(Runnable task) {
		getInstance().post(task);
	}

	public static void postUiThreadDelayed(Runnable task, long delayMillis) {
		getInstance().postDelayed(task, delayMillis);
	}

	public static boolean isUIThread(Thread thread) {
		return thread == Looper.getMainLooper().getThread();
	}
}
