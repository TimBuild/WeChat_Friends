package com.matrix.wechat.activity;

import static com.matrix.wechat.global.Constants.API_GET_REQUEST_LIST;
import static com.matrix.wechat.global.Constants.API_GET_USER_BY_USERID;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.FriendRequestListAdapter;
import com.matrix.wechat.model.FriendRequest;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.NetworkUtil;
import com.matrix.wechat.web.Request;

public class FriendRequestActivity extends Activity {
	public static Activity instance = null;
	public static List<FriendRequest> friendRequests = null;
	public static FriendRequestListAdapter adapter;
	
	private ListView requests_LV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_request);

		instance = this;
		
		friendRequests = new ArrayList<FriendRequest>();
		adapter = new FriendRequestListAdapter(this, friendRequests);
		
		requests_LV = (ListView) findViewById(R.id.requests_LV);
		requests_LV.setAdapter(adapter);
		
		requests_LV.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				FriendRequest request = adapter.friendRequests.get(arg2);
				Log.i("info", request.toString());
				new Request(instance, API_GET_USER_BY_USERID, true).execute(request.getUserid(), request);
				
			}
		});
		
		if(!NetworkUtil.isNetworkConnected(this)) {
			Toast.makeText(this, "network anomaly", Toast.LENGTH_LONG).show();
			this.finish();
			return;
		}
		new Request(this, API_GET_REQUEST_LIST, true).execute(CacheUtil.getUser(this).getUserid());
		
	}	
}
