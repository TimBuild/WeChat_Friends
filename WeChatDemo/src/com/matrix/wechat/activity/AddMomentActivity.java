package com.matrix.wechat.activity;

import com.matrix.wechat.R;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.web.service.FriendsZoneService;
import com.matrix.wechat.web.service.factory.FriendsZoneFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMomentActivity extends Activity{

	private EditText et_moment_content=null;
	private Button bt_send_moment=null;
	
	
	private static final String TAG = "AddMomentActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_moment);
		
		et_moment_content=(EditText) findViewById(R.id.add_moment_content);
		bt_send_moment=(Button) findViewById(R.id.add_moment_send);
		bt_send_moment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AddMoment().execute("share_text");				
			}
		});		
	}
	
	private class AddMoment extends AsyncTask<String, Void, Integer>{

		private FriendsZoneService fZoneService;
		
		@Override
		protected Integer doInBackground(String... params) {
			fZoneService = FriendsZoneFactory.getInstance();
			Log.d(TAG, fZoneService.toString());
			
			String moment_content=et_moment_content.getText().toString();
			
			int result;
			result=fZoneService.share(CacheUtil.getUser(CacheUtil.context).getUserid(), moment_content);
			
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {			
			super.onPostExecute(result);
			
			if(result==-1){
				Toast.makeText(AddMomentActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(AddMomentActivity.this, "发布成功"+result, Toast.LENGTH_SHORT).show();
				et_moment_content.setText("");
				Intent intent=new Intent();
				intent.setClass(AddMomentActivity.this, FriendZoneActivity.class);
				startActivity(intent);
			}
		}	
	}
}
