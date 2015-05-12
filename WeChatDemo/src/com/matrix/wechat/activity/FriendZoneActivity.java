package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.FriendZoneAdapter;
import com.matrix.wechat.customview.FriendsListView;
import com.matrix.wechat.customview.FriendsListView.OnRefreshListener;
import com.matrix.wechat.customview.FriendsListView.onLoadListener;
import com.matrix.wechat.model.Moment;

import android.app.Activity;
import android.os.Bundle;


import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FriendZoneActivity extends Activity{
public class FriendZoneActivity extends Activity implements OnRefreshListener,onLoadListener{

	private ListView mListView = null;
	private FriendsListView mListView;
	private RelativeLayout frl_header_hidden;
	private FriendZoneAdapter mfriendZoneAdapter;
	
	private int friend_start = 0;
	private int friend_count = FriendsListView.pageSize;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_zone);	
		mListView = (ListView) findViewById(R.id.friend_zone);
		setData();
		mListView = (FriendsListView) findViewById(R.id.friend_zone);
//		setData();
		mfriendZoneAdapter = new FriendZoneAdapter(this);
		mListView.setonRefreshListener(this);
		mListView.setOnLoadListener(this);
		mListView.setAdapter(mfriendZoneAdapter);
		
		loadData(FriendsListView.REFRESH);
		
	}
	
	private void setData(){
		List<Moment> monentList=new ArrayList<Moment>();
		Moment moment1=new Moment();
		moment1.setUserName("test1");
		moment1.setContent_text("12355测试，今天天气不好");
		moment1.setLocation("武进区");
		moment1.setDate("1小时前");
		monentList.add(moment1);
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
		
		Moment moment2=new Moment();
		moment2.setUserName("test2");
		moment2.setContent_text("12355测试，今天天气不好，5555555");
		moment2.setLocation("钟楼区");
		moment2.setDate("4小时前");
		monentList.add(moment2);
		
		FriendZoneAdapter mfriendZoneAdapter = new FriendZoneAdapter(this);
		mfriendZoneAdapter.setData(monentList);
		mListView.setAdapter(mfriendZoneAdapter);
	}

}
