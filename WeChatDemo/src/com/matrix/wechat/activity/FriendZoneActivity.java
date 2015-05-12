package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.FriendZoneAdapter;
import com.matrix.wechat.model.Moment;
import com.matrix.wechat.widget.SquareImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class FriendZoneActivity extends Activity{

	private ListView mListView = null;
	private SquareImageView iv_myMonent=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_zone);	
		setUpView();
		setData();
	}
	
	private void setUpView(){
		mListView = (ListView) findViewById(R.id.friend_zone);
		iv_myMonent=(SquareImageView) findViewById(R.id.friend_zone_icon);
		iv_myMonent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(FriendZoneActivity.this, MyMomentActivity.class);
				startActivity(intent);				
			}
		});
	}
	
	private void setData(){
		List<Moment> monentList=new ArrayList<Moment>();
		Moment moment1=new Moment();
		moment1.setUserName("test1");
		moment1.setContent_text("12355测试，今天天气不好");
		moment1.setLocation("武进区");
		moment1.setDate("1小时前");
		monentList.add(moment1);
		
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
