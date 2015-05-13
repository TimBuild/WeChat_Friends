package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.FriendZoneAdapter;
import com.matrix.wechat.customview.FriendsListView;
import com.matrix.wechat.customview.FriendsListView.OnRefreshListener;
import com.matrix.wechat.customview.FriendsListView.onLoadListener;
import com.matrix.wechat.model.Moment;
import com.matrix.wechat.widget.SquareImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FriendZoneActivity extends Activity implements OnRefreshListener,onLoadListener{

	private FriendsListView mListView;
	private RelativeLayout frl_header_hidden;
	private FriendZoneAdapter mfriendZoneAdapter;
	private SquareImageView iv_mymoment;
	
	private int friend_start = 0;
	private int friend_count = FriendsListView.pageSize;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_zone);	
		mListView = (FriendsListView) findViewById(R.id.friend_zone);
		mfriendZoneAdapter = new FriendZoneAdapter(this);
		mListView.setonRefreshListener(this);
		mListView.setOnLoadListener(this);
		mListView.setAdapter(mfriendZoneAdapter);
		
		iv_mymoment=(SquareImageView) findViewById(R.id.friend_zone_icon);
		iv_mymoment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(FriendZoneActivity.this, MyMomentActivity.class);
				startActivity(intent);				
			}
		});
		loadData(FriendsListView.REFRESH);
		
	}
	private void loadData(int refresh) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	

	
	
	/*private Handler friendHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case :
				break;

			default:
				break;
			}
		};
	};*/

	@Override
	public void onLoad() {
		loadData(FriendsListView.LOAD);
	}

	@Override
	public void onRefresh() {
		loadData(FriendsListView.REFRESH);
		
	}

}
