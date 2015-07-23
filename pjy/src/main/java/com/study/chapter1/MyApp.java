/*
 * @(#)MyApp.java
 *
 * Copyright (c) 2015 KW iTech, Inc.
 * All rights reserved.
 */
package com.study.chapter1;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Class desciption here.
 * 
 * @author jong-yeol Park (jypark@kwinternational.com)
 * @version 1.0
 */
public class MyApp extends Application {
	private static Context mContext = null;

	@Override
	public void onCreate() {
		super.onCreate();
		this.mContext = this;
	}

	public static String getVersionName() {
		PackageInfo pi = null;
		try {
			pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			return pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int getVersionCode() {
		PackageInfo pi = null;
		try {
			pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			return pi.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	public static boolean isNetworkAvailable() {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) mContext
			        .getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
			if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
				return activeNetwork.getType() == 1 || activeNetwork.getType() == 0;
			}
		} catch (SecurityException e) {
			Log.e("", e.getMessage());
			return false;
		}
		return false;
	}

}
