package study1.kimjy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import study1.kimjy.R;
import study1.kimjy.common.Const;
import study1.kimjy.custom.CommonDialog;
import study1.kimjy.utils.Utils;


public class SplashActivity extends Activity //implements ServerListener, OnRegisterGCMListener
{
    // private ServerModule mServer;
    private ProgressDialog mLaodingDialog;
    private Context mContext;

    private static final int DELAY_RUN_TIME = 3000;

    private static final int MSG_SHOW_LOADING_DIALOG = 1;
    private static final int MSG_HIDE_LOADING_DIALOG = 2;

    private static final int MSG_CHECK_NETWORK = 10;
    private static final int MSG_CHECK_VERSION = 11;
    private static final int MSG_CHECK_LOGIN = 12;
    private static final int MSG_MOVE_JOIN = 14;
    private static final int MSG_MOVE_MAIN = 15;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_LOADING_DIALOG: {
                    if (mLaodingDialog == null || mLaodingDialog.isShowing() == false) {
                        if (mLaodingDialog == null)
                            mLaodingDialog = new ProgressDialog(SplashActivity.this);
                        mLaodingDialog.show();
                    }
                    break;
                }

                case MSG_HIDE_LOADING_DIALOG: {
                    if (mLaodingDialog != null && mLaodingDialog.isShowing())
                        mLaodingDialog.dismiss();
                    break;
                }

                case MSG_CHECK_NETWORK: {
                    final CommonDialog loadingDialog = new CommonDialog(SplashActivity.this);
                    loadingDialog.setCloseButton(false);
                    loadingDialog.setMessage(R.string.network_message_not_available);
                    loadingDialog.setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                        public void onClick(View view) {
                            Utils.killProcess(SplashActivity.this);
                            loadingDialog.dismiss();
                        }
                    });
                    loadingDialog.show();
                    break;
                }

                case MSG_CHECK_VERSION: {
                    CommonDialog commonDialog = new CommonDialog(SplashActivity.this);
                    commonDialog.setTitle(R.string.version_update_title);
                    commonDialog.setMessage(R.string.version_update_message_normal);
                    commonDialog.setPositiveButton(android.R.string.ok, appInstall);
                    commonDialog.setNegativeButton(android.R.string.cancel, cancelAppInstall);
                    commonDialog.setCloseButton(false);
                    commonDialog.setCancelable(false);
                    commonDialog.show();
                    break;
                }

                case MSG_MOVE_MAIN: {
                    mHandler.sendEmptyMessage(MSG_HIDE_LOADING_DIALOG);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    overridePendingTransition(0, 0);
                    break;
                }

                case MSG_MOVE_JOIN: {
                    mHandler.sendEmptyMessage(MSG_HIDE_LOADING_DIALOG);
                    startActivity(new Intent(SplashActivity.this, JoinActivity.class));
                    finish();
                    overridePendingTransition(0, 0);
                    break;
                }
                default:
                    break;
            }
        }
    };

    // update at play store
    private View.OnClickListener appInstall = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri marketUri = Uri.parse(Utils.GOOGLE_MARKET_URL + mContext.getPackageName());
            Utils.reqMarketAppInstall(SplashActivity.this, marketUri);
            finish();
        }
    };

    // exit
    private View.OnClickListener cancelAppInstall = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Utils.killProcess(SplashActivity.this);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_splash);
        mContext = this;
        mHandler.sendEmptyMessage(MSG_SHOW_LOADING_DIALOG);
        // 1. check network
        check_state_network();
    }

    private void check_state_network() {
        if (!Utils.isAvailableNetwork(mContext)) {
            mHandler.sendEmptyMessage(MSG_CHECK_NETWORK);
        } else {
            // 2. check version
            check_state_version();
        }
    }

    private void check_state_version() {
        if (Utils.compareVersion(mContext, Const.APP_VERSION)) {
            // is a latest version
            // 3.check member
            if (Const.IS_MEMBER)
                mHandler.sendEmptyMessageDelayed(MSG_MOVE_MAIN, DELAY_RUN_TIME);
            else
                mHandler.sendEmptyMessageDelayed(MSG_MOVE_JOIN, DELAY_RUN_TIME);
        } else {
            // is not a latest version
            mHandler.sendEmptyMessage(MSG_CHECK_VERSION);
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.sendEmptyMessage(MSG_HIDE_LOADING_DIALOG);
        super.onDestroy();
    }
}

