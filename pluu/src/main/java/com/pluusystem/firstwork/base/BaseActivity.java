package com.pluusystem.firstwork.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by nohhs on 2015-07-15.
 */
public class BaseActivity extends AppCompatActivity {

	private ProgressDialog progressDialog;

	public Dialog showProgressDialog() {
		dismissProgressDialog();
		progressDialog = ProgressDialog.show(this, null, "Loading...", true);
		return progressDialog;
	}

	public void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	protected Dialog showAlertDialog(int resId, int positiveResId, DialogInterface.OnClickListener listener) {
		return showAlertDialog(resId, positiveResId, listener, -1, null);
	}

	protected Dialog showAlertDialog(int resId,
									 int positiveResId,
									 DialogInterface.OnClickListener positiveListener,
									 int negativeResId,
									 DialogInterface.OnClickListener negativeListener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (resId != -1) {
			builder.setMessage(resId);
		}
		if (positiveResId != -1) {
			builder.setPositiveButton(positiveResId, positiveListener);
		}
		if (negativeResId != -1) {
			builder.setNegativeButton(negativeResId, negativeListener);
		}
		return builder.show();
	}

}
