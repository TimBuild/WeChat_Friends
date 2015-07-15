package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.matrix.wechat.R;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.NetworkUtil;
import com.matrix.wechat.web.service.PersonalInfoService;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static com.matrix.wechat.global.Variables.*;
import static com.matrix.wechat.global.Constants.OWN_HEAD_IMAGE;

public class LoginActivity extends Activity{

	private EditText mUser; // 帐号编辑框
	private EditText mPassword; // 密码编辑框
	private Button go_to_regist;
	private PersonalInfoService userService;

	private long userid = 0;
	private String username = "";
	private String nickname = "";
	private String picture = "";
	private String password = "";
	private int status = 0;
	public static boolean mWorking = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		System.out.println("onCreateonCreateonCreateonCreateonCreate");
/*		
		if(!NetworkUtil.isNetworkConnected(Login.this)) {
			Toast.makeText(Login.this, "Please open your net!", Toast.LENGTH_LONG).show();
			this.finish();
			System.exit(0);
			return;
		}*/

		CacheUtil.context = getApplicationContext();
		
		loadVariables(CacheUtil.context);
		if(getIsLogined()){
			User user = getUser();
			CacheUtil.updateCachedUser(user, CacheUtil.context);
			OWN_HEAD_IMAGE = BitmapUtil.getBitmap(user.getPicture());
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, MainWeixin.class);
			startActivity(intent);
		}
		
		mUser = (EditText) findViewById(R.id.login_user_edit);
		mPassword = (EditText) findViewById(R.id.login_passwd_edit);

		go_to_regist = (Button) findViewById(R.id.login_register_btn);
		go_to_regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegistActivity.class);
				startActivity(intent);
				finish();
			}

		});

	}

	public void login_mainweixin (View v){
			if(!NetworkUtil.isNetworkConnected(LoginActivity.this)){
				Toast.makeText(LoginActivity.this, "Net work does not avaible", Toast.LENGTH_SHORT).show();
				return;
			}
		CacheUtil.clean();

		userService = PersonalInfoFactory.getInstance();
		new Thread(new Runnable() {

			private User user;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				user = userService.getUserByUsername(mUser.getText()
						.toString());
		

				if (null != user) {
					username = user.getUsername();
					password = user.getPassword();
					userid = user.getUserid();
					nickname = user.getNickname();
					picture = user.getPicture();
					status = user.getStatus();

					Log.i("Login", username);
					Log.i("Login", password);
					Log.i("Login", nickname);
					Log.i("Login", picture + "touxiang");

				}

				if ("".equals(mUser.getText().toString())
						|| "".equals(mPassword.getText().toString())) {
					Message msg = new Message();
					msg.what = 1;
					mHandler.sendMessage(msg);

				} else if (1 == status) {
					Message msg = new Message();
					msg.what = 4;
					mHandler.sendMessage(msg);
					return;
				}

				if (username.equals(mUser.getText().toString())
						&& password.equals(mPassword.getText().toString())) {

					Boolean tesetBoolean = userService.logout(userid, "IN");
					Log.i("Login", String.valueOf(tesetBoolean));

					// 实例化SharedPreferences对象（第一步) 
					SharedPreferences mySharedPreferences = getSharedPreferences(
							"user", Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// 用putString的方法保存数据
					editor.putLong("userid", userid);
					editor.putString("username", username);
					editor.putString("nickname", nickname);
					editor.putString("password", password);
					editor.putString("picture", picture);
					editor.putInt("Status", 1);
					
					// 提交当前数据
					editor.commit();
					List<String> tags = new ArrayList<String>();
					tags.add(username);
					PushManager.setTags(LoginActivity.this, tags);
					PushManager.startWork(LoginActivity.this,
							PushConstants.LOGIN_TYPE_API_KEY,
							"z8w50f2md7yf9e5tG5QyfTPy");
					mWorking = true;
					Message msg = new Message();
					msg.what = 3;
					mHandler.sendMessage(msg);
					
					JPushInterface.onResume(getApplicationContext());

					setIsLogined(true);
					user.setStatus(1);
					setUser(user);
					saveVariables(CacheUtil.context);
					
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainWeixin.class);
					startActivity(intent);
					
				}

				else {
					Message msg = new Message();
					msg.what = 2;
					mHandler.sendMessage(msg);
				}

			}

		}).start();

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				new AlertDialog.Builder(LoginActivity.this)
						.setIcon(
								getResources().getDrawable(
										R.drawable.login_error_icon))
						.setTitle("Login error")
						.setMessage("Username or password should NOT be empty!")
						.create().show();
				break;

			case 2:
				new AlertDialog.Builder(LoginActivity.this)
						.setIcon(
								getResources().getDrawable(
										R.drawable.login_error_icon))
						.setTitle("Login error")
						.setMessage("Account does not exists or wrong password!")
						.create().show();
				break;

			case 3:
				mPassword.setText("");
				break;

			case 4:
				new AlertDialog.Builder(LoginActivity.this)
						.setIcon(
								getResources().getDrawable(
										R.drawable.login_error_icon))
						.setTitle("Login error")
						.setMessage("This accout has been logged in!").create()
						.show();
				break;

			}
		};
	};

	@Override
	protected void onResume() {
		super.onResume();
//		JPushInterface.onResume(getApplicationContext());

//			new AsyncTask<Void, Void, Boolean>() {
//
//			@Override
//			protected Boolean doInBackground(Void... params) {
//				boolean result = PersonalInfoFactory.getInstance().logout(
//						CacheUtil.getUser(LoginActivity.this).getUserid(), "OUT");
//				User user = CacheUtil.getUser(LoginActivity.this);
//				user.setStatus(0);
//				CacheUtil.updateCachedUser(user, LoginActivity.this);
//				return result;
//			}
//
//			@Override
//			protected void onPostExecute(Boolean result) {
//				super.onPostExecute(result);
//			}
//		}.execute();
		if (mWorking)
			PushManager.stopWork(LoginActivity.this);
	};

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		new AsyncTask<Void, Void, Boolean>() {
//
//			@Override
//			protected Boolean doInBackground(Void... params) {
//				if(!getIsLogined()){
//					PersonalInfoFactory.getInstance().logout(
//							CacheUtil.getUser(LoginActivity.this).getUserid(), "OUT");
//					setStatus(0);
//					saveVariables(CacheUtil.context);
//					CacheUtil.updateCachedUser(getUser(), LoginActivity.this);
//				}
//				return true;
//			}
//
//			@Override
//			protected void onPostExecute(Boolean result) {
//				super.onPostExecute(result);
//			}
//		}.execute();
		if (mWorking) {
			PushManager.stopWork(LoginActivity.this);
			mWorking = false;
		}
	};

}
