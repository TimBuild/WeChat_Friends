package com.matrix.wechat.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.FriendZoneAdapter;
import com.matrix.wechat.customview.FriendsListView;
import com.matrix.wechat.customview.FriendsListView.OnRefreshListener;
import com.matrix.wechat.customview.FriendsListView.onLoadListener;
import com.matrix.wechat.customview.RoundImageView;
import com.matrix.wechat.global.Constants;
import com.matrix.wechat.listener.TouchListener;
import com.matrix.wechat.model.Comment;
import com.matrix.wechat.model.Moment;
import com.matrix.wechat.model.Share;
import com.matrix.wechat.model.ShareComment;
import com.matrix.wechat.model.ShareWithComment;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.DateUtil;
import com.matrix.wechat.utils.FileUtil;
import com.matrix.wechat.utils.voice.RecordVoice;
import com.matrix.wechat.utils.voice.SendVoice;
import com.matrix.wechat.web.service.FriendsZoneService;
import com.matrix.wechat.web.service.PersonalInfoService;
import com.matrix.wechat.web.service.factory.FriendsZoneFactory;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;
import com.matrix.wechat.widget.SquareImageView;

public class FriendZoneActivity extends Activity implements OnClickListener,OnRefreshListener,
		onLoadListener {

	private FriendsListView mListView;
	private RelativeLayout frl_header_hidden,frl_comment;
	private FriendZoneAdapter mfriendZoneAdapter;
	private SquareImageView iv_mymoment;
	private Button bt_addMoment;
	private EditText ed_comment_content;
	private RelativeLayout relBack;
	
	public static LinearLayout zoomView = null;
	public static ImageView imageView = null;
	
//	private RoundImageView comment_pic;
//	private static int PIC_REQUEST_CODE = 3;
	private static final String IMAGE_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/imgs";

	private int friend_start = 0;
	private int friend_count = FriendsListView.pageSize;

	private List<Moment> listMoments = new ArrayList<Moment>();
	private List<Moment> listResult = new ArrayList<Moment>();
	private static final String TAG = "FriendZoneActivity";
	
	private String imagePath;
	private int[] imageIds = new int[50];
	private EditText et_comment_content;
	private Dialog builder;
	
	private static int PIC_SUCCESS_CODE = 4;
	private static RecordVoice recordVoice = new RecordVoice();
	public static final String VOICE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/test.3gp";
	
	private RelativeLayout rl_voice;

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
//		comment_pic = (RoundImageView) findViewById(R.id.comment_bt_pic);
		
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
		
		zoomView = (LinearLayout) findViewById(R.id.friend_zoomView);
		imageView = (ImageView) zoomView.findViewById(R.id.friend_imageView);
		imageView.setOnTouchListener(new TouchListener(imageView));
		
	/*	comment_pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, PIC_REQUEST_CODE);				
			}
		});*/
		
		new FriendsZone().execute(FriendsListView.REFRESH);
		
	}
	
	
	//////////////////////////////////////////////////
	//适配器中调用startActivityForResult(),这边解决
	//////////////////////////////////////////////////
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FriendZoneAdapter.PIC_REQUEST_CODE && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			try {
				WindowManager manager = getWindowManager();
				Display display = manager.getDefaultDisplay();
				Bitmap bmp = BitmapUtil.getBitmapFromContentProviderUri(this,
						display.getWidth(), display.getHeight(), uri);
				
				String path = BitmapUtil.copyImageToCard(bmp, IMAGE_PATH,
						"1.png");
				
				if (path == null) {
					throw new FileNotFoundException();
				}

				String filePathOnServer = FileUtil.uploadFile(this, path,
						Constants.UPLOAD_Url);

				if (filePathOnServer == null) {
					throw new IOException();
				}

				/**
				 * 发送图片
				 */
				sendPicture(filePathOnServer);
				FileUtil.deleteFile(path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}else if(requestCode == PIC_SUCCESS_CODE&&resultCode == RESULT_OK){
			Uri uri = data.getData();
			try {
				WindowManager manager = getWindowManager();
				Display display = manager.getDefaultDisplay();
				Bitmap bmp = BitmapUtil.getBitmapFromContentProviderUri(this,
						display.getWidth(), display.getHeight(), uri);
				
				String path = BitmapUtil.copyImageToCard(bmp, IMAGE_PATH,
						"1.png");
				
				if (path == null) {
					throw new FileNotFoundException();
				}

				String filePathOnServer = FileUtil.uploadFile(this, path,
						Constants.UPLOAD_Url);

				if (filePathOnServer == null) {
					throw new IOException();
				}
				this.imagePath = filePathOnServer;
				FileUtil.deleteFile(path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendPicture(String filePathOnServer) {
		mfriendZoneAdapter.setImagePath(filePathOnServer);
	}

	@Override
	public void onBackPressed() {
		if(zoomView.getVisibility() != View.VISIBLE){
			super.onBackPressed();
		}else{
			zoomView.setVisibility(View.GONE);
			imageView.setScaleType(ScaleType.CENTER);
		}
	}
	
	private class onFriendItemClickListener implements OnItemClickListener{
		
		private List<Moment> listMoments;

		public onFriendItemClickListener(List<Moment> listResult) {
			this.listMoments = listResult;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				final long id) {
			
			frl_comment.setVisibility(View.VISIBLE);
			
			//获取输入框内容
			et_comment_content =(EditText) frl_comment.findViewById(R.id.et_comment_content);
			ImageView comment_expression = (ImageView) frl_comment.findViewById(R.id.comment_expression);
			RoundImageView comment_pic = (RoundImageView) frl_comment.findViewById(R.id.comment_bt_pic);
			
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
							
							if((!comment_content.equals("")&&comment_content!=null)||imagePath!=null){
								Comment comment_add = new Comment();
								String comment = comment_content;
								if(imagePath!=null){
									comment = comment+",[Image],"+imagePath;
								}
								Log.d(TAG, "图片地址："+comment);
								comment_add.setContent(comment);
								comment_add.setSharefromname(CacheUtil.getUser(CacheUtil.context).getUsername());
								comment_add.setSharefromid((int)CacheUtil.getUser(CacheUtil.context).getUserid());
								comment_add.setSharetoid((int)user.getUserid());
								comment_add.setSharetoname(user.getUsername());
								comment_add.setShareid(moment.getMomentid());
								
								listcComments.add(comment_add);
								
								new addComment(listMoments.get(position-2)).execute(Integer.toString(moment.getMomentid()),Long.toString(user.getUserid()),comment);
							}
							
							
						}
					}).start();
				}
			});
			
			rl_voice = (RelativeLayout) frl_comment.findViewById(R.id.rl_friend_zone_voice);
			final ImageView iv_voice = (ImageView) frl_comment.findViewById(R.id.comment_voice);
			but.setOnLongClickListener(new OnLongClickListener() {
				//长按将声音按钮显示出来
				@Override
				public boolean onLongClick(View v) {
					
					rl_voice.setVisibility(View.VISIBLE);
					final Moment moment = listMoments.get((int)id);
					rl_voice.setOnTouchListener(new OnTouchListener() {
						
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							switch (event.getActionMasked()) {
							case MotionEvent.ACTION_DOWN:
								recordVoice.startRecording();
//						iv_voice.setBackgroundResource(R.drawable.microphone_press);
								break;
							case MotionEvent.ACTION_UP:
								boolean result = recordVoice.stopRecording();
//						iv_voice.setBackgroundResource(R.drawable.microphone);
								if(!result){
									Toast.makeText(FriendZoneActivity.this, "record time is to short", Toast.LENGTH_SHORT).show();
									return true;	
								}
								
								final long time = recordVoice.calcuteVoice();
								final String voicePath = SendVoice.uploadFile(VOICE_PATH, Constants.UPLOAD_Url);
//								Log.d(TAG, "voicePath:"+voicePath);
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										
										PersonalInfoService perInfoService=PersonalInfoFactory.getInstance();
										User user=perInfoService.getUserByUsername(moment.getUserName());
										String voice = "[Voice],"+voicePath+","+time;
										new addComment(listMoments.get((int) id)).execute(Integer.toString(moment.getMomentid()),Long.toString(user.getUserid()),voice);
										FileUtil.deleteFile(VOICE_PATH);
									}
								}).start();
								break;
							}
//					rl_voice.setVisibility(View.GONE);
							return true;
						}
					});
					return true;
				}
			});
			

			comment_expression.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					createExpressionDialog();					
				}

			});
			
			comment_pic.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(intent, PIC_SUCCESS_CODE);
				}
			});
		}
		
	}
	/**
	 * 创建一个表情选择对话框
	 */
	private void createExpressionDialog() {
		builder = new Dialog(this);
		GridView gridView = createGridView();
		builder.setContentView(gridView);
		builder.setTitle("default expression");
		builder.show();

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(),
						imageIds[position % imageIds.length]);
				ImageSpan imageSpan = new ImageSpan(getApplicationContext(), bitmap);
				String str = null;
				if (position < 10) {
					str = "f00" + position;
				} else if (position < 100) {
					str = "f0" + position;
				}

				SpannableString spanableString = new SpannableString(str);
				spanableString.setSpan(imageSpan, 0, 4,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				et_comment_content.append(spanableString);
				builder.dismiss();

			}
		});
	}
	
	/**
	 * 生成一个表情对话框中的gridview
	 * 
	 * @return
	 */
	private GridView createGridView() {
		final GridView view = new GridView(this);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// 生成20个表情的id，封装

		for (int i = 0; i < 50; i++) {
			try {
				if (i < 10) {
					Field field = R.drawable.class.getDeclaredField("f00" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				} else if (i < 100) {
					Field field = R.drawable.class.getDeclaredField("f0" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				}

			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.team_layput_single_expression_cell,
				new String[] { "image" }, new int[] { R.id.image });
		view.setAdapter(simpleAdapter);
		view.setNumColumns(5);
		view.setBackgroundColor(Color.rgb(214, 211, 214));
		view.setHorizontalSpacing(1);
		view.setVerticalSpacing(1);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		view.setGravity(Gravity.CENTER);

		return view;

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
		private Moment moment;
		public addComment(Moment moment){
			this.moment = moment;
//			Log.d(TAG, "this.moment:"+this.moment.getCommentsList().toString());
		}
		@Override
		protected Integer doInBackground(String... params) {
			fZoneService = FriendsZoneFactory.getInstance();
			
			int result=fZoneService.comment(Integer.parseInt(params[0]), CacheUtil.getUser(CacheUtil.context).getUserid(),
					Long.parseLong(params[1]), params[2]);
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			imagePath = null;
			rl_voice.setVisibility(View.GONE);
			if(result!=-1){
				frl_comment.setVisibility(View.GONE);
				Toast.makeText(FriendZoneActivity.this, "comment success", Toast.LENGTH_SHORT).show();
				listResult.add(moment);
				mfriendZoneAdapter.setData(listResult);
				mfriendZoneAdapter.notifyDataSetChanged();
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
			moment.setType(shaWithComment.getShareFriend().getType());
			if(shaWithComment.getShareFriend().getType() == 1){
				//文字
				moment.setContent_text(shaWithComment.getShareFriend().getContent());
			}
			else if(shaWithComment.getShareFriend().getType() == 2){
				//图片地址
				moment.setImg_url(shaWithComment.getShareFriend().getImg_url());
			}else if(shaWithComment.getShareFriend().getType() == 3){
				moment.setVoic_url(shaWithComment.getShareFriend().getImg_url());
			}

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
	protected void onResume() {
		super.onResume();
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
