package com.example.homework01.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import com.example.homework01.R;

/**
 * Created by whdcn_000 on 2015-01-20.
 */
public class ProgressDialog extends Activity {

    private static Dialog mLoadingDialog = null;

    public static void showLoading(Context context) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new Dialog(context, R.style.CustomDialog);
            ProgressBar pb = new ProgressBar(context);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mLoadingDialog.addContentView(pb, params);
            mLoadingDialog.setCancelable(false);                // back키 잠금
        }

        mLoadingDialog.show();
    }

    public static boolean isShowing() {
        boolean isShow = false;
        if (mLoadingDialog != null) {
            isShow = true;
        }

        return isShow;
    }

    public static void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
