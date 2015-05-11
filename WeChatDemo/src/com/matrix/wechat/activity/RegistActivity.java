package com.matrix.wechat.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.matrix.wechat.R;
import com.matrix.wechat.model.User;
import com.matrix.wechat.web.service.PersonalInfoService;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;

public class RegistActivity extends Activity {

	private Button back_to_loginButton;
	private Button registButton;
	private EditText usernameText;
	private EditText passwordText;
	private EditText passwordAgainText;

	private String username;
	private String password;
	private String passwordAgain;

	private PersonalInfoService userService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);

		back_to_loginButton = (Button) findViewById(R.id.back_to_login_btn);
		registButton = (Button) findViewById(R.id.regist_register_btn);

		back_to_loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RegistActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});

		registButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userService = PersonalInfoFactory.getInstance();

				usernameText = (EditText) findViewById(R.id.regist_user_edit);
				passwordText = (EditText) findViewById(R.id.regist_passwd_edit);
				passwordAgainText = (EditText) findViewById(R.id.regist_passwd_again_edit);
				username = usernameText.getText().toString().trim();
				password = passwordText.getText().toString().trim();
				passwordAgain = passwordAgainText.getText().toString().trim();
				Log.i("Regist", username);
				Log.i("Regist", password);
				Log.i("Regist", passwordAgain);

				new Thread(new Runnable() {
					private User user;

					@Override
					public void run() {
						try{
						user = userService.getUserByUsername(username);
						}catch(Exception e){
							return;
						}
						if ("".equals(password) || "".equals(passwordAgain)
								|| "".equals(username)) {
							Message msg = new Message();
							msg.what = 1;
							mHandler.sendMessage(msg);
						} else if (password.equals(passwordAgain) != true) {
							Message msg = new Message();
							msg.what = 2;
							mHandler.sendMessage(msg);
						} else if (null != user) {
							Message msg = new Message();
							msg.what = 3;
							mHandler.sendMessage(msg);
						} else {
							
							Boolean isScuess=userService.createUser(username, password);
							Log.i("Regist",String.valueOf(isScuess));
							
							
							
							
							Intent intent =new Intent();
							intent.setClass(RegistActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();

						}
					}

				}).start();

			}

		});

	}

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				new AlertDialog.Builder(RegistActivity.this)
						.setIcon(
								getResources().getDrawable(
										R.drawable.login_error_icon))
						.setTitle("Regist error")
						.setMessage("Username or password should not be empty!").create().show();
				break;
				
			case 2:
				new AlertDialog.Builder(RegistActivity.this)
				.setIcon(
						getResources().getDrawable(
								R.drawable.login_error_icon))
				.setTitle("Regist error")
				.setMessage("Password should be the same!").create().show();
				break;
			case 3:				
				new AlertDialog.Builder(RegistActivity.this)
				.setIcon(
						getResources().getDrawable(
								R.drawable.login_error_icon))
				.setTitle("Regist error")
				.setMessage("Username has existed!").create().show();
				break;

			}
		};
	};

}
