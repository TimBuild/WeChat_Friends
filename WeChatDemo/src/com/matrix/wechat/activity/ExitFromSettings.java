package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.matrix.wechat.R;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;

public class ExitFromSettings extends Activity {
	// private MyDialog dialog;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exit_dialog_from_settings);
		// dialog=new MyDialog(this);
		layout = (LinearLayout) findViewById(R.id.exit_layout2);
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
		this.finish();
		return true;
	}

	public void exitbutton1(View v) {
		this.finish();
	}

	public void exitbutton0(View v) {
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				boolean result = PersonalInfoFactory.getInstance().logout(
						CacheUtil.getUser(ExitFromSettings.this).getUserid(),
						"OUT");
				User user = CacheUtil.getUser(ExitFromSettings.this);
				user.setStatus(0);
				CacheUtil.updateCachedUser(user, ExitFromSettings.this);
				return result;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result == true) {
					finish();
					MainWeixin.instance.finish();// 关闭Main 这个Activity
					User user = CacheUtil.getUser(ExitFromSettings.this);
					List<String> tags = new ArrayList<String>();
					tags.add(user.getUsername());
					PushManager.delTags(ExitFromSettings.this, tags);
					PushManager.stopWork(ExitFromSettings.this);
				} else {
					Toast.makeText(getApplicationContext(), "Logout failed",
							Toast.LENGTH_LONG).show();
				}
			}
		}.execute();
	}

}
