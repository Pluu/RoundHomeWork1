package com.pluusystem.firstwork.intro;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.pluulove.common.Const;
import com.pluusystem.firstwork.base.BaseActivity;

/**
 * Created by nohhs on 2015-07-15.
 */
public class IntroHandler extends Handler {

	private final String TAG = IntroHandler.class.getSimpleName();

	public interface OnResultListener {
		void onIntroResult(INTRO_RESULT result);
	}

	public enum CHECK_PROCESS {
		SPLASH,
		INTERNET_CHECK,
		VERSION_CHECK,
		SERVER_API
	}

	private final BaseActivity activity;
	private final OnResultListener listener;

	public IntroHandler(BaseActivity activity, OnResultListener listener) {
		this.activity = activity;
		this.listener = listener;
	}

	public void start() {
		Log.d(TAG, "Start");
		Message msg = createMessage(CHECK_PROCESS.SPLASH);
		sendMessageDelayed(msg, 1500L);
	}

	private Message createMessage(CHECK_PROCESS type) {
		Message msg = new Message();
		msg.obj = type;
		return msg;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		CHECK_PROCESS worker = (CHECK_PROCESS) msg.obj;
		Log.d(TAG, "Check=" + worker);

		switch (worker) {

			case SPLASH:
				viewSplash();
				break;
			case INTERNET_CHECK:
				checkNetwork();
				break;
			case VERSION_CHECK:
				checkVersion();
				break;
			case SERVER_API:
				checkServerAPI();
				break;
		}
	}

	private void notifyResult(final INTRO_RESULT result) {
		Log.d(TAG, "Result=" + result.name());

		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			@Override
			public void run() {
				activity.dismissProgressDialog();
				if (listener != null) {
					listener.onIntroResult(result);
				}
			}
		});
	}

	private void viewSplash() {
		activity.showProgressDialog();
		sendMessage(createMessage(CHECK_PROCESS.INTERNET_CHECK));
	}

	private void checkNetwork() {
		if ((isNetworkOnline(activity))) {
			sendMessage(createMessage(CHECK_PROCESS.VERSION_CHECK));
		} else {
			notifyResult(INTRO_RESULT.NETWORK_DISABLE);
		}
	}

	private boolean isNetworkOnline(Context context) {
		try {
			ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();

			if (networkInfo == null || !networkInfo.isConnected()) {
				return false;
			}

			switch (networkInfo.getType()) {
				case ConnectivityManager.TYPE_WIFI:
				case ConnectivityManager.TYPE_MOBILE:
					NetworkInfo.State state = networkInfo.getState();
					if (NetworkInfo.State.CONNECTED == state
						|| NetworkInfo.State.CONNECTING == state) {
						return true;
					}
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	private void checkVersion() {
		PackageInfo pi = getPackageInfo();
		if (pi == null
			|| TextUtils.isEmpty(pi.versionName)
			|| TextUtils.equals(pi.versionName, Const.APP_VERSION)) {
			sendMessage(createMessage(CHECK_PROCESS.SERVER_API));
		} else {
			notifyResult(INTRO_RESULT.MARKET);
		}
	}

	public PackageInfo getPackageInfo() {
		Context context = activity.getBaseContext();
		final PackageManager pm = context.getPackageManager();
		if (pm == null) {
			return null;
		}

		try {
			return pm.getPackageInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		} catch (RuntimeException e) {
			return null;
		}
	}

	private void checkServerAPI() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				startDelayTask();
			}
		}).start();
	}

	private void startDelayTask() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		INTRO_RESULT result = null;
		try {
			result = executor.invokeAny(Arrays.asList(new DelayTask()),
										5L, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		notifyResult(result != null
						 ? result
						 : INTRO_RESULT.NOT_LOGIN);
	}

	private class DelayTask implements Callable<INTRO_RESULT> {

		@Override
		public INTRO_RESULT call() throws Exception {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return INTRO_RESULT.LOGIN;
//			return INTRO_RESULT.SERVER_SLEEP;
		}
	}

}
