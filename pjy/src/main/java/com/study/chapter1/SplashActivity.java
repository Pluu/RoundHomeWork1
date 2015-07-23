package com.study.chapter1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.study.chapter1.service.LoginService;
import com.study.chapter1.service.ResponseHandler;
import com.study.chapter1.ui.UIHandler;

public class SplashActivity extends Activity {

	public static final int SPLASH_DELAY_TIME = 1500;

	private Activity mActivity = this;

	private LoginService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		service = new LoginService(this);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				UIHandler.postUiThreadDelayed(new Runnable() {
					@Override
					public void run() {
						checkAppVersion();
					}
				}, 100);
			}
		}, SPLASH_DELAY_TIME);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void checkAppVersion() {
		service.appVersion(new ResponseHandler() {
			@Override
			public boolean onSuccess(int statusCode, String content) {
				if (MyApp.getVersionName().equals(content)) {
					login();
				} else {
					showUpdateAlert();
				}
				return false;
			}

			@Override
			public boolean onFailure(Throwable error, int statusCode, String content) {
				showFinishAlert(content);
				return false;
			}

			@Override
			public void onFinish() {

			}

			@Override
			public boolean onUnavailableNetwork() {
				showFinishAlert("Unavailable Network\ncode: 1");
				return false;
			}
		});
	}

	private void login() {
		service.login("test", "pw1234", new ResponseHandler() {
			@Override
			public boolean onSuccess(int statusCode, String content) {
				Log.d("", "success = " + content);

				if ("0".equals(content)) {
					showFinishAlert("서버 점검중 입니다.");
				} else {
					Intent intent = new Intent(mActivity, MainActivity.class);
					intent.putExtra(LoginService.MEMBER_RESULT, content);
					startActivity(intent);
					finish();
				}
				return false;
			}

			@Override
			public boolean onFailure(Throwable error, int statusCode, String content) {
				showFinishAlert(content);
				return false;
			}

			@Override
			public void onFinish() {
				Log.d("", "next");
			}

			@Override
			public boolean onUnavailableNetwork() {
				showFinishAlert("Unavailable Network\ncode: 2");
				return false;
			}
		});
	}

	private void showFinishAlert(final String msg) {
		UIHandler.postUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
				builder.setMessage(msg);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK button
						dialog.dismiss();
						finish();
					}
				});

				AlertDialog dialog = builder.create();
				dialog.show();

			}
		});

	}

	private void showUpdateAlert() {
		UIHandler.postUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
				builder.setCancelable(false);
				builder.setTitle("alert");
				builder.setMessage("Update.. app..");
				builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						// intent.setData(Uri.parse("market://details?id=" + package ));
						intent.setData(Uri.parse("market://search?q=네이버"));
						startActivity(intent);
						finish();
					}
				});

				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK button
						dialog.dismiss();
						finish();
					}
				});

				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

	}

}