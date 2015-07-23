/*
 * @(#)DummyServer.java
 *
 * Copyright (c) 2015 KW iTech, Inc.
 * All rights reserved.
 */
package com.study.chapter1.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

import com.study.chapter1.Constants;
import com.study.chapter1.MyApp;
import com.study.chapter1.ui.UIHandler;

/**
 * Class desciption here.
 * 
 * @author jong-yeol Park (jypark@kwinternational.com)
 * @version 1.0
 */
public class LoginService {
	public static final String MEMBER_RESULT = "member_result";

	private Activity mActivity;
	private DummyServer server;

	public LoginService(Activity act) {
		this.mActivity = act;
		server = new DummyServer();
	}

	public void appVersion(ResponseHandler response) {
		if (MyApp.isNetworkAvailable() == false) {
			if (response != null) {
				response.onUnavailableNetwork();
			}
		} else {
			MyTask task = new MyTask(response);
			task.execute(OPCode.APP_VER);
		}
	}

	public void login(String id, String pw, ResponseHandler response) {
		// check available Network
		if (MyApp.isNetworkAvailable() == false) {
			if (response != null) {
				response.onUnavailableNetwork();
			}
		} else {
			MyTask task = new MyTask(response);
			task.execute(OPCode.LOG_IN);
		}
	}

	public class MyTask extends AsyncTask<OPCode, Void, Void> {

		private ProgressDialog dialog;
		private ResponseHandler response;

		public MyTask(ResponseHandler response) {
			this.response = response;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			UIHandler.postUiThread(new Runnable() {
				@Override
				public void run() {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
					dialog = ProgressDialog.show(mActivity, "Alert", "Loading..");
				}
			});
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			UIHandler.postUiThread(new Runnable() {
				@Override
				public void run() {
					if (dialog != null) {
						dialog.dismiss();
					}
					dialog = null;
				}
			});
		}

		@Override
		protected Void doInBackground(OPCode... params) {
			OPCode opCode = params[0];
			if (opCode == OPCode.LOG_IN) {
				server.execute(Constants.LOGIN_REULST, OPCode.LOG_IN, response);
			} else if (opCode == OPCode.APP_VER) {
				server.execute(Constants.APP_VERSION, OPCode.APP_VER, response);
			}
			return null;
		}

	}

}
