package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ls.LSInput;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.FriendZoneAdapter;
import com.matrix.wechat.customview.FriendsListView;
import com.matrix.wechat.customview.FriendsListView.OnRefreshListener;
import com.matrix.wechat.customview.FriendsListView.onLoadListener;
import com.matrix.wechat.model.Moment;
import com.matrix.wechat.model.Share;
import com.matrix.wechat.model.ShareWithComment;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.web.service.FriendsZoneService;
import com.matrix.wechat.web.service.factory.FriendsZoneFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FriendZoneActivity extends Activity implements OnRefreshListener,onLoadListener{

	private FriendsListView mListView;
	private RelativeLayout frl_header_hidden;
	private FriendZoneAdapter mfriendZoneAdapter;
	
	private int friend_start = 0;
	private int friend_count = FriendsListView.pageSize;
	
	
	private List<Moment> listMoments;
	private static final String TAG = "FriendZoneActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_zone);	
		mListView = (FriendsListView) findViewById(R.id.friend_zone);
		mfriendZoneAdapter = new FriendZoneAdapter(this);
		mListView.setonRefreshListener(this);
		mListView.setOnLoadListener(this);
		mListView.setAdapter(mfriendZoneAdapter);
		
//		loadData(FriendsListView.REFRESH);
		new FriendsZone().execute(FriendsListView.REFRESH);
		
	}
	
	private class FriendsZone extends AsyncTask<Integer, Void, String>{
		
		private FriendsZoneService fZoneService;

		@Override
		protected String doInBackground(Integer... params) {
			
			fZoneService = FriendsZoneFactory.getInstance();
			Log.d(TAG, fZoneService.toString());
			listMoments = new ArrayList<Moment>();
			Share lists = null;
			if(params[0].equals(FriendsListView.REFRESH)){
				Log.d(TAG,"OnRefresh");
				lists = fZoneService.getAllZoneList(CacheUtil.getUser(CacheUtil.context).getUserid());
//				lists.shareWithComment.get(0)
			}
			else if(params[0].equals(FriendsListView.LOAD)){
				
			}
			Log.d(TAG, lists.toString());
			return null;
		}

		
		
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
//		loadData(FriendsListView.LOAD);
		new FriendsZone().execute(FriendsListView.LOAD);
	}

	@Override
	public void onRefresh() {
//		loadData(FriendsListView.REFRESH);
		new FriendsZone().execute(FriendsListView.REFRESH);
		
	}

}
