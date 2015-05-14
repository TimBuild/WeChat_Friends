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
import com.matrix.wechat.model.ShareFriend;
import com.matrix.wechat.model.ShareWithComment;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.DateUtil;
import com.matrix.wechat.web.service.FriendsZoneService;
import com.matrix.wechat.web.service.factory.FriendsZoneFactory;
import com.matrix.wechat.widget.SquareImageView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FriendZoneActivity extends Activity implements OnClickListener,OnRefreshListener,
		onLoadListener {

	private FriendsListView mListView;
	private RelativeLayout frl_header_hidden;
	private FriendZoneAdapter mfriendZoneAdapter;
	private SquareImageView iv_mymoment;
	private Button bt_addMoment;
	
	private RelativeLayout relBack;

	private int friend_start = 0;
	private int friend_count = FriendsListView.pageSize;

	private List<Moment> listMoments = new ArrayList<Moment>();
	private List<Moment> listResult = new ArrayList<Moment>();
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
		
		relBack = (RelativeLayout) findViewById(R.id.friend_zone_back);
		iv_mymoment = (SquareImageView) findViewById(R.id.friend_zone_icon);
		bt_addMoment = (Button) findViewById(R.id.add_moment);
		
		relBack.setOnClickListener(this);
		iv_mymoment.setOnClickListener(this);
		bt_addMoment.setOnClickListener(this);
		new FriendsZone().execute(FriendsListView.REFRESH);
		
	}

	private List<Moment> getListMoments(Share share) {

		List<Moment> lists = new ArrayList<Moment>();
//		ShareWithComment shaWithComment : share.shareWithComment
		for (int i = 0;i<share.shareWithComment.size()-1;i++) {
			ShareWithComment shaWithComment = share.shareWithComment.get(i);
			Moment moment = new Moment();
			moment.setPicture(shaWithComment.getUser().getPicture());
			String date = shaWithComment.getShareFriend().getDate();
			String time = DateUtil.getParseTime(date);
			moment.setUserName(shaWithComment.getUser().getNickname());
			moment.setDate(time);
			moment.setContent_text(shaWithComment.getShareFriend().getContent());
			lists.add(moment);
		}
		Log.d(TAG, "share.shareWithComment.size():"+share.shareWithComment.size());

		return lists;
	}

	private class FriendsZone extends AsyncTask<Integer, Void, String> {

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

				share = fZoneService.getAllZoneList(
						CacheUtil.getUser(CacheUtil.context).getUserid(),
						friend_start, friend_count);
				if (share != null) {
					result = "REFRESH";
					listMoments = getListMoments(share);
					// mListView.onRefreshComplete();

				} else {
					result = "RefreshError";
				}

			} else if (params[0].equals(FriendsListView.LOAD)) {
				Log.d(TAG, "OnLoad");
				friend_count = FriendsListView.pageSize;
				friend_start = friend_start + friend_count;
				Log.d(TAG, "onLoad-->start:" + friend_start + "--count:"
						+ friend_count);

				share = fZoneService.getAllZoneList(
						CacheUtil.getUser(CacheUtil.context).getUserid(),
						friend_start, friend_count);
				// Log.d(TAG, "onLoad:share.toString:"+share.toString());
				if (share != null) {
					result = "LOAD";
					listMoments = getListMoments(share);
					// mListView.onLoadComplete();
				} else {
					result = "LoadError";
				}

			}

			// result[1] = listMoments+"";
			// Log.d(TAG, "result:"+result);
			// Log.d(TAG, listMoments.toString());
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// List<Moment> lists = listMoments;

			// mListView.setResultSize(listMoments.size());
			// Log.d(TAG, "lists:--->" + listMoments.toString());
			// Log.d(TAG, "size--->" + listMoments.size());
			if (result.equals("RefreshError")) {
				// mListView.setResultSize(0);
				mListView.onRefreshComplete();
			} else if (result.equals("LoadError")) {
				mListView.setResultSize(0);
				return;
				// mListView.onLoadComplete();
			} else if (result.equals("REFRESH")) {
				mListView.setResultSize(listMoments.size());
				mListView.onRefreshComplete();
				listResult.clear();
				listResult.addAll(0, listMoments);

			} else if (result.equals("LOAD")) {
				mListView.setResultSize(listMoments.size());
				mListView.onLoadComplete();
				listResult.addAll(listMoments);
			}

			mfriendZoneAdapter.setData(listResult);
			mfriendZoneAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onLoad() {
		new FriendsZone().execute(FriendsListView.LOAD);
	}

	@Override
	public void onRefresh() {
		new FriendsZone().execute(FriendsListView.REFRESH);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.friend_zone_back:
			intent = new Intent(FriendZoneActivity.this, MainWeixin.class);
			startActivity(intent);
			finish();
			break;
		case R.id.friend_zone_icon:
			intent = new Intent(FriendZoneActivity.this, MyMomentActivity.class);
			startActivity(intent);
			break;
		case R.id.add_moment:
			intent = new Intent(FriendZoneActivity.this,
					AddMomentActivity.class);
			startActivity(intent);
			break;
		}
	}
}
