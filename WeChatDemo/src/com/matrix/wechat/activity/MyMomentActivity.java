package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.MyMonentAdapter;
import com.matrix.wechat.customview.FriendsListView;
import com.matrix.wechat.customview.FriendsListView.OnRefreshListener;
import com.matrix.wechat.customview.FriendsListView.onLoadListener;
import com.matrix.wechat.model.CurrentMoment;
import com.matrix.wechat.model.Share;
import com.matrix.wechat.model.ShareWithComment;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.DateUtil;
import com.matrix.wechat.web.service.FriendsZoneService;
import com.matrix.wechat.web.service.factory.FriendsZoneFactory;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class MyMomentActivity extends Activity implements OnRefreshListener,onLoadListener{

	private FriendsListView myMomentListView=null;
	private RelativeLayout relBack;
	
	private MyMonentAdapter myMonentAdapter;
	private static String TAG="MyMomentActivity";
	
	private int friend_start = 0;
	private int friend_count = FriendsListView.pageSize;
	
	private List<CurrentMoment> listMoments = new ArrayList<CurrentMoment>();
	private List<CurrentMoment> listResult = new ArrayList<CurrentMoment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_moment);
		myMomentListView=(FriendsListView) findViewById(R.id.my_moment);
		
		relBack = (RelativeLayout) findViewById(R.id.friend_current_back);
		relBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(MyMomentActivity.this,FriendZoneActivity.class);
//				startActivity(intent);
				finish();
			}
		});
		myMonentAdapter = new MyMonentAdapter(this);
		myMomentListView.setonRefreshListener(this);
		myMomentListView.setOnLoadListener(this);
		myMomentListView.setAdapter(myMonentAdapter);
		
		new CurrentMomentZone().execute(FriendsListView.REFRESH);
	}
	
	private List<CurrentMoment> getListMoments(Share share) {

		List<CurrentMoment> lists = new ArrayList<CurrentMoment>();
		for (int i = 0;i<share.shareWithComment.size()-1;i++) {
			ShareWithComment shaWithComment = share.shareWithComment.get(i);
			CurrentMoment cmoment = new CurrentMoment();
			String date = shaWithComment.getShareFriend().getDate();
			String time = DateUtil.getDateTime(date);
			cmoment.setDate(time);
			cmoment.setContext(shaWithComment.getShareFriend().getContent());
			lists.add(cmoment);
		}
		Log.d(TAG, "share.shareWithComment.size():"+share.shareWithComment.size());

		return lists;
	}
	
	
	private class CurrentMomentZone extends AsyncTask<Integer, Void, String>{
		
		private FriendsZoneService fZoneService;

		@Override
		protected String doInBackground(Integer... params) {
			fZoneService = FriendsZoneFactory.getInstance();
			Share share = null;
			String result = null;
			if (params[0].equals(FriendsListView.REFRESH)) {
				Log.d(TAG, "OnRefresh");

				friend_count = friend_count + friend_start;
				friend_start = 0;
				Log.d(TAG, "onRefresh-->start:" + friend_start + "--count:"
						+ friend_count);

				share = fZoneService.getSharesByUserid(
						CacheUtil.getUser(CacheUtil.context).getUserid(),
						friend_start, friend_count);
				if (share != null) {
					result = "REFRESH";
					listMoments = getListMoments(share);

				} else {
					result = "RefreshError";
				}

			} else if (params[0].equals(FriendsListView.LOAD)) {
				Log.d(TAG, "OnLoad");
				friend_count = FriendsListView.pageSize;
				friend_start = friend_start + friend_count;
				Log.d(TAG, "onLoad-->start:" + friend_start + "--count:"
						+ friend_count);

				share = fZoneService.getSharesByUserid(
						CacheUtil.getUser(CacheUtil.context).getUserid(),
						friend_start, friend_count);
				if (share != null) {
					result = "LOAD";
					listMoments = getListMoments(share);
				} else {
					result = "LoadError";
				}

			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("RefreshError")) {
				// mListView.setResultSize(0);
				myMomentListView.onRefreshComplete();
			} else if (result.equals("LoadError")) {
				myMomentListView.setResultSize(0);
				return;
				// mListView.onLoadComplete();
			} else if (result.equals("REFRESH")) {
				myMomentListView.setResultSize(listMoments.size());
				myMomentListView.onRefreshComplete();
				listResult.clear();
				listResult.addAll(0, listMoments);

			} else if (result.equals("LOAD")) {
				myMomentListView.setResultSize(listMoments.size());
				myMomentListView.onLoadComplete();
				listResult.addAll(listMoments);
			}

			myMonentAdapter.setData(listResult);
			myMonentAdapter.notifyDataSetChanged();
		}
		
	}
	

	@Override
	public void onLoad() {
		new CurrentMomentZone().execute(FriendsListView.LOAD);
	}

	@Override
	public void onRefresh() {
		new CurrentMomentZone().execute(FriendsListView.REFRESH);		
	}
	
}
