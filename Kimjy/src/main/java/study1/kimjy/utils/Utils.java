package study1.kimjy.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;


public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    public static final String GOOGLE_MARKET_URL = "market://details?id=";

    public static boolean compareVersion(Context context, String strVer) {
        boolean ret = false;
        String version = "";
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            return ret;
        }
        int curVer = Integer.parseInt(version.replace(".", ""));
        int messageVer = Integer.parseInt(strVer.replace(".", ""));

        Log.w(TAG, "compareVersion: strVer 버전===>" + strVer);
        Log.w(TAG, "compareVersion: version 버전===>" + version);
        if (curVer < messageVer) {
            ret = false;
        } else if (curVer > messageVer) {
            ret = true;
        } else if ((curVer) == messageVer) {
            ret = true;
        }
        return ret;
    }

    public static void reqMarketAppInstall(Context context, Uri uri) {
        if (context == null || uri == null)
            return;

        try {
            Intent market = new Intent(Intent.ACTION_VIEW, uri);
            market.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(market);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void killProcess(Context context) {
        ((Activity) context).moveTaskToBack(true);
        ((Activity) context).finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static boolean isAvailableNetwork(Context context) {
        NetworkInfo ni = null;
        boolean isMobileConn, isWifiConn, isWimaxConn;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        isWifiConn = (ni.isAvailable() & ni.isConnected());

        try {
            ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            isMobileConn = (ni.isAvailable() & ni.isConnected());
        } catch (NullPointerException e) {
            isMobileConn = false;
        }

        try {
            ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
            isWimaxConn = (ni.isAvailable() & ni.isConnected());
        } catch (NullPointerException e) {
            isWimaxConn = false;
        }

        return isMobileConn || isWifiConn || isWimaxConn;
    }

}
