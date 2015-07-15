package com.matrix.wechat.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.matrix.wechat.R;
import com.matrix.wechat.logic.LoginAction;
import com.matrix.wechat.web.service.PersonalInfoService;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;

import static com.matrix.wechat.global.Variables.*;

public class ExitActivity extends Activity {
	// private MyDialog dialog;
	private LinearLayout layout;
	private PersonalInfoService userService;
	private Long userid = -1L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exit);
		// dialog=new MyDialog(this);
		layout = (LinearLayout) findViewById(R.id.exit_layout);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
				// Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void exitbutton1(View v) {
		this.finish();
	}

	public void exitbutton0(View v) {
		userService = PersonalInfoFactory.getInstance();

		SharedPreferences sharedPreferences = getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		userid = sharedPreferences.getLong("userid", -1);
		Log.i("Login", String.valueOf(userid));

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Boolean tesetBoolean = userService.logout(userid, "OUT");
				Log.i("Exit", String.valueOf(tesetBoolean));
				if (ChatActivity.mDataArrays != null) {
					ChatActivity.mDataArrays.clear();
					if (ChatActivity.mAdapter != null) {
						ChatActivity.mAdapter.notifyDataSetChanged();
					}
				}
			}
		}).start();
		
		setIsLogined(false);
		LoginAction.logout();
		this.finish();
		MainWeixin.instance.finish();// 关闭Main 这个Activity

	}

}
