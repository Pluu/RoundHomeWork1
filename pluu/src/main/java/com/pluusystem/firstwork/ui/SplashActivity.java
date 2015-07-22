package com.pluusystem.firstwork.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.pluusystem.firstwork.R;
import com.pluusystem.firstwork.base.BaseActivity;
import com.pluusystem.firstwork.intro.INTRO_RESULT;
import com.pluusystem.firstwork.intro.IntroHandler;

public class SplashActivity extends BaseActivity
	implements IntroHandler.OnResultListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new IntroHandler(this, this).start();
	}

	@Override
	public void onIntroResult(INTRO_RESULT result) {
		switch (result) {
			case MARKET:
				alertMarket();
				break;
			case LOGIN:
				moveActivityNextFinish(MainActivity.class);
				break;
			case NOT_LOGIN:
				moveActivityNextFinish(JoinActivity.class);
				break;
			case NETWORK_DISABLE:
				showAlertDialog(R.string.notify_network_disable,
								android.R.string.ok, getFinishListener());
				break;
			case SERVER_SLEEP:
				showAlertDialog(R.string.notify_server_sleep,
								android.R.string.ok, getFinishListener());
				break;
			default:
				finish();
		}
	}

	private void alertMarket() {
		showAlertDialog(R.string.notify_update,
						R.string.action_update, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
					marketLaunch.setData(Uri.parse("market://details?id=" + getPackageName()));
					startActivity(marketLaunch);
					finish();
				}
			}, android.R.string.cancel, getFinishListener());
	}

	private void moveActivityNextFinish(Class<?> target) {
		if (target != null) {
			startActivity(new Intent(this, target));
		}
		finish();
	}

	private DialogInterface.OnClickListener getFinishListener() {
		return new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		};
	}

}
