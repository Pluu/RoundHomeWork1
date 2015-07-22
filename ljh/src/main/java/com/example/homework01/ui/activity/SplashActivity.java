package com.example.homework01.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.*;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.homework01.R;
import com.example.homework01.constant.AppConstant;
import com.example.homework01.ui.dialog.ProgressDialog;

public class SplashActivity extends AppCompatActivity {

    private int result = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!checkInternetConnection()) {
            showOneButtonDialog("인터넷이 연결되어 있지 않습니다.");
            return;
        }

        if (!checkAppVersion()) {
            showTwoButtonDialog("업데이트가 필요합니다", "업데이트", "취소");
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressDialog.showLoading(SplashActivity.this);
            }
        }, 1500);

        serverRequest();
    }

    private boolean isTimeOut = false;

    private void serverRequest() {
        final Handler timeOut = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    timeOut.postDelayed(timeOutRunnable, 5 * 1000);
                    Thread.sleep(10 * 1000);
                    //Thread.sleep((long) 4.5 * 1000);
                    timeOut.removeCallbacks(timeOutRunnable);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!isTimeOut) {
                    serverResponse();
                }
            }
        }).start();
    }

    Runnable timeOutRunnable = new Runnable() {
        @Override
        public void run() {
            isTimeOut = true;
            result = 1;
            serverResponse();
        }
    };

    private void serverResponse() {
        ProgressDialog.hideLoading();
        Intent i;
        switch (result) {
            case 0:
                showOneButtonDialog("서버가 점검 중입니다.");
                break;
            case 1:
                i = new Intent(this, JoinActiviy.class);
                startActivity(i);
                finish();
                break;
            case 2:
                i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    private void showTwoButtonDialog(String msg, final String but1, final String but2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg).setCancelable(false).setPositiveButton(but1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (but1.equals("업데이트")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/"));
                    startActivity(intent);
                }
            }
        }).setNegativeButton(but2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (but2.equals("취소")) {
                    finish();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showOneButtonDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg).setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean checkInternetConnection() {
        boolean isConnection = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();

        for (NetworkInfo tempNetworkInfo : networkInfos) {
            if (tempNetworkInfo.isConnected()) {
                isConnection = true;
                break;
            }
        }
        return isConnection;
    }

    private boolean checkAppVersion() {
        boolean isOk = false;

        if (AppConstant.APP_VERSION.equals(getAppVersion())) {
            isOk = true;
        }

        return isOk;
    }

    private String getAppVersion() {
        String version = "";
        try {
            PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = i.versionName;
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
