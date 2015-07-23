package com.study.chapter1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.study.chapter1.service.LoginService;

public class MainActivity extends Activity {

	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.textView);
		readIntent();
	}

	private void readIntent() {
		Intent intent = getIntent();
		String result = intent.getStringExtra(LoginService.MEMBER_RESULT);
		Log.d("", "result=" + result);
		if ("1".equals(result)) {
			textView.setText("Non-member user");
		} else {
			textView.setText("Member user");
		}
	}
}
