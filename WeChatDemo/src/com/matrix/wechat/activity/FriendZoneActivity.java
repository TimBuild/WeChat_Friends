package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.CommentAdapter;
import com.matrix.wechat.adapter.FriendZoneAdapter;
import com.matrix.wechat.customview.CommentListView;
import com.matrix.wechat.customview.FriendsListView;
import com.matrix.wechat.customview.FriendsListView.OnRefreshListener;
import com.matrix.wechat.customview.FriendsListView.onLoadListener;
import com.matrix.wechat.model.Comment;
import com.matrix.wechat.model.Moment;
import com.matrix.wechat.model.Share;
import com.matrix.wechat.model.ShareComment;
import com.matrix.wechat.model.ShareWithComment;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.DateUtil;
import com.matrix.wechat.web.service.FriendsZoneService;
import com.matrix.wechat.web.service.PersonalInfoService;
import com.matrix.wechat.web.service.factory.FriendsZoneFactory;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;
import com.matrix.wechat.widget.SquareImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FriendZoneActivity extends Activity implements OnClickListener,OnRefreshListener,
		onLoadListener {

	private FriendsListView mListView;
	private RelativeLayout frl_header_hidden,frl_comment;
	private FriendZoneAdapter mfriendZoneAdapter;
	private SquareImageView iv_mymoment;
	private Button bt_addMoment;
	private EditText ed_comment_content;
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
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		mListView = (FriendsListView) findViewById(R.id.friend_zone);
		mfriendZoneAdapter = new FriendZoneAdapter(this);
		mListView.setonRefreshListener(this);
		mListView.setOnLoadListener(this);
		
		mfriendZoneAdapter.setFriendListView(mListView);
		mListView.setAdapter(mfriendZoneAdapter);
		
		relBack = (RelativeLayout) findViewById(R.id.friend_zone_back);
		iv_mymoment = (SquareImageView) findViewById(R.id.friend_zone_icon);
		bt_addMoment = (Button) findViewById(R.id.add_moment);
		
		frl_comment=(RelativeLayout) findViewById(R.id.rl_friend_zone_bottom);
		frl_comment.setVisibility(View.GONE);
		mfriendZoneAdapter.setFooterView(frl_comment);
		ed_comment_content=(EditText) findViewById(R.id.et_comment_content);
		mListView.setOnItemLongClickListener(new OnFriendItemLongClickListener(listResult));
		///..............
		mListView.setOnItemClickListener(new onFriendItemClickListener(listResult));

		mListView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				frl_comment.setVisibility(View.GONE);
				ed_comment_content.setText("");
				View view = getWindow().peekDecorView();
		        if (view != null) {
		            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		        }
				return false;
			}
		});	
		relBack.setOnClickListener(this);
		iv_mymoment.setOnClickListener(this);
		bt_addMoment.setOnClickListener(this);
		
		new FriendsZone().execute(FriendsListView.REFRESH);
		
	}
	
	private class onFriendItemClickListener implements OnItemClickListener{
		
		private List<Moment> listMoments;

		public onFriendItemClickListener(List<Moment> listResult) {
			this.listMoments = listResult;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				final long id) {
			
			frl_comment.setVisibility(View.VISIBLE);
			
			//获取输入框内容
			final EditText et_comment_content=(EditText) frl_comment.findViewById(R.id.et_comment_content);	
			
			Button but = (Button) frl_comment.findViewById(R.id.comment_content_send);
			but.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					final String comment_content=et_comment_content.getText().toString();
					final Moment moment = listMoments.get((int)id);
					
					frl_comment.setVisibility(View.GONE);
					et_comment_content.setText("");
					
					final List<Comment> listcComments= moment.getCommentsList();
					Log.d(TAG, "moment.toString():"+moment.toString());
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							PersonalInfoService perInfoService=PersonalInfoFactory.getInstance();
							User user=perInfoService.getUserByUsername(moment.getUserName());
							
							if(!comment_content.equals("")||comment_content!=null){
								Comment comment_add = new Comment();
								
								comment_add.setContent(comment_content);
								comment_add.setSharefromname(CacheUtil.getUser(CacheUtil.context).getUsername());
								comment_add.setSharefromid((int)CacheUtil.getUser(CacheUtil.context).getUserid());
								comment_add.setSharetoid((int)user.getUserid());
								comment_add.setSharetoname(user.getUsername());
								comment_add.setShareid(moment.getMomentid());
								
								listcComments.add(comment_add);
								
								new addComment().execute(Integer.toString(moment.getMomentid()),Long.toString(user.getUserid()),comment_content);
							}
							
							
						}
					}).start();
				}
			});
			
		}
		
	}
	
	private class OnFriendItemLongClickListener implements OnItemLongClickListener{
		
		private List<Moment> listMoments;

		public OnFriendItemLongClickListener(List<Moment> listMoments) {
			this.listMoments = listMoments;
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			Moment moment = listMoments.get(position-2);
			final int sharefrom = (int)CacheUtil.getUser(CacheUtil.context).getUserid();
			final int shareid = moment.getMomentid();
			
			AlertDialog.Builder builder = new Builder(FriendZoneActivity.this);
			builder.setTitle("Delete share");
			builder.setMessage("You sure to delete?");
			builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.d(TAG, "position:"+position);
					new deleteShare(listMoments,position-2).execute(sharefrom,shareid);
					dialog.dismiss();
					
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
//			Log.d(TAG,"OnFriendItemClickListener:"+listMoments.get(position-2).toString());
			return true;
		}
		
	}
	
	private class addComment extends AsyncTask<String, Void, Integer>{

		private FriendsZoneService fZoneService;
		@Override
		protected Integer doInBackground(String... params) {
			fZoneService = FriendsZoneFactory.getInstance();
			
			int result=fZoneService.comment(Integer.parseInt(params[0]), CacheUtil.getUser(CacheUtil.context).getUserid(),
					Long.parseLong(params[1]), params[2]);
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if(result!=-1){
				Toast.makeText(FriendZoneActivity.this, "comment success", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(FriendZoneActivity.this, "comment failed", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	private class deleteShare extends AsyncTask<Integer, Void, Boolean>{
		
		private FriendsZoneService friendsZoneService;
		private List<Moment> listMoments;
		private int position;

		public deleteShare(List<Moment> listMoments2, int position) {
			this.listMoments = listMoments2;
			this.position = position;
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			friendsZoneService = FriendsZoneFactory.getInstance();
			boolean result = friendsZoneService.deleteShare(params[0], params[1]);
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				Toast.makeText(FriendZoneActivity.this, "delete success", Toast.LENGTH_SHORT).show();
				Log.d(TAG, "listResult.toString()"+listResult.toString());
				Log.d(TAG, "position"+position);
				
				listResult.remove(position);
				mfriendZoneAdapter.setData(listResult);
				mfriendZoneAdapter.notifyDataSetChanged();
			}
			else{
				Toast.makeText(FriendZoneActivity.this, "you can not delete it!", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	

	private List<Moment> getListMoments(Share share) {

		List<Moment> lists = new ArrayList<Moment>();
		
//		ShareWithComment shaWithComment : share.shareWithComment
		
		for (int i = 0;i<share.shareWithComment.size()-1;i++) {
			ShareWithComment shaWithComment = share.shareWithComment.get(i);
			Moment moment = new Moment();
			moment.setMomentid(shaWithComment.getShareFriend().getShareid());
			moment.setPicture(shaWithComment.getUser().getPicture());
			String date = shaWithComment.getShareFriend().getDate();
			String time = DateUtil.getParseTime(date);
			moment.setUserName(shaWithComment.getUser().getUsername());
			moment.setDate(time);
			moment.setContent_text(shaWithComment.getShareFriend().getContent());

			List<ShareComment> shareCommentsLists=shaWithComment.getShareComments();
			if(shareCommentsLists!=null){
				List<Comment> commentLists=new ArrayList<Comment>();
				Log.d(TAG, "shareCommentsLists:"+shareCommentsLists.toString());
				for(int j=0;j<shareCommentsLists.size()-1;j++){
					Comment comment=new Comment();
					comment.setCommentid(shaWithComment.getShareComments().get(j).getCommentid());
					comment.setShareid(shaWithComment.getShareComments().get(j).getShareid());
					comment.setSharefromname(shaWithComment.getCommentUsers().get(j).getFrom_userName());
					comment.setSharetoname(shaWithComment.getCommentUsers().get(j).getTo_userName());
					comment.setSharefromid(shaWithComment.getShareComments().get(j).getFromid());
					comment.setSharetoid(shaWithComment.getShareComments().get(j).getToid());
					comment.setContent(shaWithComment.getShareComments().get(j).getContent());
					commentLists.add(comment);
				}
				Log.d(TAG, i+" commentLists:"+commentLists.toString());			
				moment.setCommentsList(commentLists);
			}
			lists.add(moment);
		}
		Log.d(TAG, "lists:-->"+lists.toString());
//		Log.d(TAG, "share.shareWithComment.size():"+share.shareWithComment.size());

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
				Log.d(TAG, "share-->"+share);
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
				Log.d(TAG, "share-->"+share);
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
			Log.d(TAG, "result:"+result);
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
//			intent = new Intent(FriendZoneActivity.this, MainWeixin.class);
//			startActivity(intent);
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
