package com.example.dingdingkim.studyreport1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends Activity {

	final static String L="study_report_1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_splash);
		
		boolean is_internet_on=check_internet(SplashActivity.this);

		//인터넷 연결됨
		if(is_internet_on){
			//1.5초 기다렸다가 다음 실행
			Handler mHandler = new Handler(); 
			mHandler.postDelayed(new Runnable() {

				public void run() {
					//앱이 최신버전임
					if(Const.APP_VERSION.equals(get_app_version())){
						MyAsyncTask task=new MyAsyncTask(SplashActivity.this);
						task.execute();
					}
					//앱이 최신버전이 아님. 마켓으로 이동한다
					else{
						AlertDialog.Builder alert_confirm = new AlertDialog.Builder(SplashActivity.this);
						alert_confirm.setMessage("앱이 업데이트 되었습니다. 업데이트하시겠습니까?")
						.setCancelable(false)
						.setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								go_to_market();						
							}
						}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								finish();
							}
						}).show();
					}
				} 
			}, 1500);
		}
		//인터넷 연결안됨
		else{
			AlertDialog.Builder alert_confirm = new AlertDialog.Builder(SplashActivity.this);
			alert_confirm.setMessage("인터넷에 연결되지 않았습니다. 잠시후 다시 시도해주세요.")
			.setCancelable(false)
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();
				}
			}).show();
		}
	}

	//마켓으로 이동
	public void go_to_market(){
	    Intent intent = new Intent(Intent.ACTION_VIEW);
		//앱이 등록된게 없으니까 이동은 하되 앱이 없다고 나오겠지
	    intent.setData(Uri.parse("market://details?id=" + getPackageName()));
	    startActivity(intent);
	}
	
	//앱 버전 가져오기
	public String get_app_version(){
		String version="";
		try {
		   PackageInfo i=getPackageManager().getPackageInfo(getPackageName(), 0);
		   version = i.versionName;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return version;
	}
	
	//인터넷 연결 체크
	public static boolean check_internet(Context context){
		ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo state_3g=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);//3g
		NetworkInfo state_wifi=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);//wifi
		NetworkInfo state_lte_4g=manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);//lte
		
		boolean blte_4g=false;
		
		if(state_lte_4g!=null)
			blte_4g=state_lte_4g.isConnected();
		if(state_3g!=null){
			if(state_3g.isConnected()||state_wifi.isConnected()||blte_4g)
				return true;
		}else{
			if(state_wifi.isConnected()||blte_4g)
				return true;
		}
		
		return false;
	}
	
	public static class MyAsyncTask extends AsyncTask<Object, Void, Void>{
		ProgressDialog progressDialog;
		Activity activity;
		int server_result;
		
		MyAsyncTask(Activity activity){
			this.activity=activity;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			progressDialog = ProgressDialog.show(activity, null, "Loading...");
		}
		
		@Override
		protected Void doInBackground(Object... params) {
			try {
				Thread.sleep(3000);

				Random rand_result=new Random();
				
				server_result=rand_result.nextInt(3);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			progressDialog.dismiss();
			
			//서버점검
			if(server_result==0){
				AlertDialog.Builder alert_confirm = new AlertDialog.Builder(activity);
				alert_confirm.setMessage("서버점검중입니다. 잠시후 다시 시도해주세요.")
				.setCancelable(false)
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						activity.finish();
					}
				}).show();
			}
			//비회원
			else if(server_result==1){
				activity.startActivity(new Intent(activity, JoinActivity.class));
				activity.finish();
			}
			//회원
			else if(server_result==2){
				activity.startActivity(new Intent(activity, MainActivity.class));				
				activity.finish();
			}
		}
	}
}