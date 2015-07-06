package com.matrix.wechat.adapter;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.wechat.R;
import com.matrix.wechat.activity.ChatActivity;
import com.matrix.wechat.activity.FriendZoneActivity;
import com.matrix.wechat.customview.CommentListView;
import com.matrix.wechat.customview.FriendsListView;
import com.matrix.wechat.customview.RoundImageView;
import com.matrix.wechat.global.Constants;
import com.matrix.wechat.model.Comment;
import com.matrix.wechat.model.Moment;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.FileUtil;
import com.matrix.wechat.utils.NetworkUtil;
import com.matrix.wechat.utils.TimeUtil;
import com.matrix.wechat.utils.voice.PlayVoice;
import com.matrix.wechat.utils.voice.RecordVoice;
import com.matrix.wechat.utils.voice.SendVoice;
import com.matrix.wechat.web.service.FriendsZoneService;
import com.matrix.wechat.web.service.PersonalInfoService;
import com.matrix.wechat.web.service.factory.FriendsZoneFactory;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;
import com.squareup.picasso.Picasso;

public class FriendZoneAdapter extends BaseAdapter{

	private List<Moment> mList = new ArrayList<Moment>();
	private LayoutInflater mInflater;
	private static String TAG = "FriendZoneAdapter";
	private RelativeLayout frl_comment;
	private Context context;
	private String comment_content="";
	private int shareid;
	private int sharetoid=-1;
	private MyHandler handler;
	private FriendsListView friendsListView;
	private final static int SUCCESS = 2;
	private final static int SUCCESS_DELETESHARE = 1;
	private Dialog builder;
	private int[] imageIds = new int[50];
	private EditText et_comment_content;
	
	public static int PIC_REQUEST_CODE = 3;
	
	private String imagePath;
	private RelativeLayout rl_voice;
	private static RecordVoice recordVoice = new RecordVoice();
	
	public FriendZoneAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
		this.context=context;
	}

	public void setFriendListView(FriendsListView listview){
		this.friendsListView = listview;
	}
	public void setData(List<Moment> list) {
		this.mList.clear();
		this.mList.addAll(list);
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setFooterView(RelativeLayout layout){
		this.frl_comment = layout;
	}
	
	public void setImagePath(String imagePath){
		this.imagePath = imagePath;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Moment moment = mList.get(position);
		int type = moment.getType();
		String voice_url = moment.getVoic_url();
		ViewHolder holder = null;	
		
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.item_friend_zone, null);
			holder = new ViewHolder();		
			holder.img_icon = (ImageView) convertView.findViewById(R.id.moment_icon);
			holder.tv_username=(TextView) convertView.findViewById(R.id.moment_username);
			holder.tv_content_text=(TextView) convertView.findViewById(R.id.moment_content);
			holder.image_context = (ImageView) convertView.findViewById(R.id.context_icon);
			holder.voice_context = (ImageView) convertView.findViewById(R.id.voice_icon);
			holder.voice_length = (TextView) convertView.findViewById(R.id.voice_length);
			holder.tv_date=(TextView) convertView.findViewById(R.id.moment_date);
			holder.iv_addComment=(ImageView) convertView.findViewById(R.id.add_comment_img);
			holder.lv_comments=(CommentListView) convertView.findViewById(R.id.lv_comments);
			holder.et_comment_content=(EditText) frl_comment.findViewById(R.id.et_comment_content);	
			holder.comment_content_send=(Button) frl_comment.findViewById(R.id.comment_content_send);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img_icon.setImageBitmap(BitmapUtil.getBitmap(moment.getPicture()));
		holder.tv_username.setText(moment.getUserName());
		if(type == 1){
			holder.image_context.setVisibility(View.GONE);
			holder.voice_context.setVisibility(View.GONE);
			holder.voice_length.setVisibility(View.GONE);
			holder.tv_content_text.setVisibility(View.VISIBLE);
			holder.tv_content_text.setText(moment.getContent_text());
		}else if(type == 2){
			holder.image_context.setVisibility(View.VISIBLE);
			holder.tv_content_text.setVisibility(View.GONE);
			holder.voice_context.setVisibility(View.GONE);
			holder.voice_length.setVisibility(View.GONE);
			holder.image_context.setOnClickListener(new OnImgClickListener(moment.getImg_url()));
			Picasso.with(context).load(moment.getImg_url()).into(holder.image_context);
		}else if(type == 3){
			holder.image_context.setVisibility(View.GONE);
			holder.tv_content_text.setVisibility(View.GONE);
			holder.voice_context.setVisibility(View.VISIBLE);
			holder.voice_length.setVisibility(View.VISIBLE);
			
			Log.d(TAG, "voice_url: "+voice_url);
			if(voice_url!=null){
				String[] result = voice_url.split(",");
				
//				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//				String time = sdf.format(result[1]);
				holder.voice_length.setText(TimeUtil.parseTime(result[1]));
			}
		}
		holder.tv_date.setText(moment.getDate());
		//shareid=mList.get(position).getMomentid();
		
		/*holder.iv_addComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "position:"+position);
				shareid=mList.get(position).getMomentid();
				Log.d(TAG, "shareid:"+shareid);
				
				frl_comment.setVisibility(View.VISIBLE);
			}
		});*/
		
		//Moment moment = mList.get(position);
		
		List<Comment> listcomment;
		listcomment = new ArrayList<Comment>();
//		listcomment=getListComments();		
		listcomment=moment.getCommentsList();
		CommentAdapter adapter = new CommentAdapter(context);
		adapter.setData(listcomment);
		
		holder.lv_comments.setAdapter(adapter);
		adapter.notifyDataSetChanged();	
//		handler = new MyHandler(adapter, listcomment);
		//.....................
//		holder.comment_content_send.setOnClickListener(new OnSendClickListener(frl_comment, mList.get(position).getUserName(),adapter,mList.get(position).getCommentsList()));
		holder.lv_comments.setOnItemClickListener(new OnCommentItemClickListener(holder.lv_comments,frl_comment,adapter,listcomment,context));
		
		//长按删除评论
		holder.lv_comments.setOnItemLongClickListener(new OnCommentItemLongListener(holder.lv_comments,frl_comment,adapter,listcomment));
		
//		friendsListView.setOnItemLongClickListener(new OnFriendItemClickListener(mList));
		
		
		holder.voice_context.setOnClickListener(new onVoiceClickListener(voice_url));
		
		return convertView;
	}
	
	static class ViewHolder{
		public ImageView img_icon;
		public TextView tv_username;
		public TextView tv_content_text;
		public TextView tv_date;
		public ImageView iv_addComment;
		public CommentListView lv_comments;		
		public EditText et_comment_content;
		public Button comment_content_send;
		public ImageView image_context;
		public ImageView voice_context;
		public TextView voice_length;
	}
	
	private class OnImgClickListener implements OnClickListener{
		
		String url = null;
		public OnImgClickListener(String url){
			this.url = url;
		}

		@Override
		public void onClick(View v) {
			FriendZoneActivity.zoomView.setVisibility(View.VISIBLE);
			Picasso.with(context).load(url).into(FriendZoneActivity.imageView);
			FriendZoneActivity.imageView.setScaleType(ScaleType.MATRIX);
		}
		
	}
	
	private class AddComment extends AsyncTask<String, Void, Integer>{

		private FriendsZoneService fZoneService;
		private CommentAdapter commentAdapter;
		private List<Comment> listComments;
		private Comment comment;
		
		public AddComment(CommentAdapter commentAdapter,
				List<Comment> listComments,Comment comment) {
			super();
			this.commentAdapter = commentAdapter;
			this.listComments = listComments;
			this.comment = comment;
		}

		@Override
		protected Integer doInBackground(String... params) {
			fZoneService = FriendsZoneFactory.getInstance();
			Log.d(TAG, fZoneService.toString());
			
			int result;
			result=fZoneService.comment(Integer.parseInt(params[0]), CacheUtil.getUser(CacheUtil.context).getUserid(),
					Long.parseLong(params[1]), params[2]);
			Log.d(TAG,"result"+result);
			return result;
		}	
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			imagePath = null;
			if(result!=-1){
				//添加到list数据中
				Toast.makeText(context, "comment success", Toast.LENGTH_SHORT).show();
//				comment.setShareid(result);
//				listComments.add(comment);
				comment.setCommentid(result);
				Log.d(TAG, "onPostExecute:"+comment.toString());
				listComments.add(comment);
				Message msg = Message.obtain();
				msg.what = SUCCESS;
				msg.obj = listComments;
				
				//将信息传给handler
				handler.sendMessage(msg);
			}else{
				Toast.makeText(context, "comment failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	private class deleteComment extends AsyncTask<Integer, Void, Boolean>{
		
		private FriendsZoneService fZoneService;
		private CommentAdapter commentAdapter;
		private List<Comment> listComments;
		private Comment comment;
		private int position;
		
		
		
		public deleteComment(CommentAdapter commentAdapter,
				List<Comment> listComments, int position) {
			super();
			this.commentAdapter = commentAdapter;
			this.listComments = listComments;
			this.position = position;
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			fZoneService = FriendsZoneFactory.getInstance();
			boolean result;
			Log.d(TAG, "deleteComment:"+params[0]+"--"+params[1]);
			result = fZoneService.deleteComment(params[0], params[1]);
			Log.d(TAG, "deleteComment:"+result);
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
				listComments.remove(position);
				
				Message msg = Message.obtain();
				msg.what = SUCCESS;
				msg.obj = listComments;
				
				//将信息传给handler
				handler.sendMessage(msg);
			}
			else{
				Toast.makeText(context, "you can not delete it!", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
	
	private class onVoiceClickListener implements OnClickListener{

		String url = null;
		String time = "";
		
		public onVoiceClickListener(String url){
			
			Log.d(TAG, "url: "+url);
			if(url!=null){
				String[] result = url.split(",");
				this.url = result[0];
				this.time = result[1];
			}
		}
		
		
		@Override
		public void onClick(View v) {
			Log.i("info","VoiceOnClickListrner");
			Log.d(TAG, "url:---> "+url+" time: "+time);
			PlayVoice.startPlaying(url);
		}
		
	}
	
	
	private class OnCommentItemLongListener implements OnItemLongClickListener{
		
		private CommentListView listView;
		private RelativeLayout frl_comment;
		private CommentAdapter commentAdapter;
		private List<Comment> listComments;
		

		public OnCommentItemLongListener(CommentListView lv_comments,
				RelativeLayout frl_comment, CommentAdapter adapter,
				List<Comment> listcomment) {
			this.listView = lv_comments;
			this.frl_comment = frl_comment;
			this.commentAdapter = adapter;
			this.listComments = listcomment;
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			handler = new MyHandler(commentAdapter, listComments);
			frl_comment.setVisibility(View.GONE);
			
			final int commentid = listComments.get(position).getCommentid();
			final int fromid = (int)CacheUtil.getUser(CacheUtil.context).getUserid();
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					FriendsZoneService friendsZoneService = FriendsZoneFactory.getInstance();
//					friendsZoneService.deleteComment(fromid, commentid);
//					
//				}
//			}).start();
//			Log.d(TAG, ".................................");
			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle("Delete comment");
			builder.setMessage("You sure to delete?");
			builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new deleteComment(commentAdapter,listComments,position).execute(fromid,commentid);
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
			
			return true;
		}
		
	}
	
	/**
	 * 自定义OnItemClickListener事件监听
	 *
	 */
	private class OnCommentItemClickListener implements OnItemClickListener{
		private CommentListView listView;
		private RelativeLayout frl_comment;
		private CommentAdapter commentAdapter;
		private List<Comment> listComments;
		private Context context;
		

		public OnCommentItemClickListener(CommentListView listView,
				RelativeLayout frl_comment, CommentAdapter commentAdapter,
				List<Comment> listcComments,Context context) {
			super();
			this.listView = listView;
			this.frl_comment = frl_comment;
			this.commentAdapter = commentAdapter;
			this.listComments = listcComments;
			this.context = context;
		}


		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			frl_comment.setVisibility(View.VISIBLE);
			
			//获取输入框内容
			et_comment_content=(EditText) frl_comment.findViewById(R.id.et_comment_content);	
			final ImageView et_button_expression=(ImageView) frl_comment.findViewById(R.id.comment_expression);
			final RoundImageView comment_pic = (RoundImageView) frl_comment.findViewById(R.id.comment_bt_pic);
			final Button et_button = (Button) frl_comment.findViewById(R.id.comment_content_send_item);
			final Button et_button_before = (Button) frl_comment.findViewById(R.id.comment_content_send);
			
			et_button_expression.setVisibility(View.VISIBLE);
//			comment_pic.setVisibility(View.VISIBLE);
			et_button.setVisibility(View.VISIBLE);
			et_button_before.setVisibility(View.GONE);
			final Comment comment = (Comment) listView.getAdapter().getItem(position);
//			Log.d(TAG, "position-->"+position+" id:"+id+" listcomment:"+listView.getAdapter().getItem(position));
			
			final String userName = comment.getSharefromname();
			
			Log.d(TAG, "username-->1 "+userName);
			
			et_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					handler = new MyHandler(commentAdapter, listComments);
					et_button.setVisibility(View.GONE);
					et_button_before.setVisibility(View.VISIBLE);
					frl_comment.setVisibility(View.GONE);
					comment_content=et_comment_content.getText().toString();
					
					Log.d(TAG, "username-->2 "+userName);
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// 获取评论人的名字,根据名字获得id
							PersonalInfoService perInfoService=PersonalInfoFactory.getInstance();
							User user=perInfoService.getUserByUsername(userName);
							sharetoid=(int) user.getUserid();
							Log.d(TAG, "username-->"+comment_content.toString());
//							Log.d(TAG, "图片地址：<---"+imagePath);
							if((!comment_content.equals("")&&comment_content!=null)||imagePath!=null){
								if(imagePath!=null){
									comment_content = comment_content+",[Image],"+imagePath;
								}
								Comment comment_add = new Comment();
								comment_add.setSharefromname(CacheUtil.getUser(CacheUtil.context).getUsername());
								comment_add.setSharefromid((int)(CacheUtil.getUser(CacheUtil.context).getUserid()));
								comment_add.setSharetoname(user.getUsername());
								comment_add.setSharetoid((int)(user.getUserid()));
								comment_add.setContent(comment_content);
								comment_add.setShareid(comment.getShareid());
//								listComments.add(comment_add);
								Log.d(TAG, "listComments.toString():"+listComments.toString());
								new AddComment(commentAdapter,listComments,comment_add).execute(Integer.toString(comment.getShareid()),Integer.toString(sharetoid),comment_content);
							}
						}
					}).start();
				}
			});
			
			rl_voice = (RelativeLayout) frl_comment.findViewById(R.id.rl_friend_zone_voice);
			et_button.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					handler = new MyHandler(commentAdapter, listComments);
					rl_voice.setVisibility(View.VISIBLE);
					
					rl_voice.setOnTouchListener(new OnTouchListener() {
						
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							switch (event.getActionMasked()) {
							case MotionEvent.ACTION_DOWN:
								recordVoice.startRecording();
								break;
							case MotionEvent.ACTION_UP:
								boolean result = recordVoice.stopRecording();
								if(!result){
									Toast.makeText(context, "record time is to short", Toast.LENGTH_SHORT).show();
									return true;	
								}
								
								final long time = recordVoice.calcuteVoice();
								final String voicePath = SendVoice.uploadFile(FriendZoneActivity.VOICE_PATH, Constants.UPLOAD_Url);
//								Log.d(TAG, "voicePath:"+voicePath);
										
										PersonalInfoService perInfoService=PersonalInfoFactory.getInstance();
										User user=perInfoService.getUserByUsername(userName);
										sharetoid = (int) user.getUserid();
										String voice = "[Voice],"+voicePath+","+time;
										Comment comment_add = new Comment();
										comment_add.setSharefromname(CacheUtil.getUser(CacheUtil.context).getUsername());
										comment_add.setSharefromid((int)(CacheUtil.getUser(CacheUtil.context).getUserid()));
										comment_add.setSharetoname(user.getUsername());
										comment_add.setSharetoid((int)(user.getUserid()));
										comment_add.setContent(voice);
										comment_add.setShareid(comment.getShareid());
										new AddComment(commentAdapter,listComments,comment_add).execute(Integer.toString(comment.getShareid()),Integer.toString(sharetoid),voice);
										FileUtil.deleteFile(FriendZoneActivity.VOICE_PATH);
//										frl_comment.setVisibility(View.GONE);
										rl_voice.setVisibility(View.GONE);
								break;
							}
//					rl_voice.setVisibility(View.GONE);
							return true;
						}
					});
					return true;
				}
			});
			et_button_expression.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					createExpressionDialog();
				}
			});
			
			comment_pic.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					((FriendZoneActivity) context).startActivityForResult(intent, PIC_REQUEST_CODE);
					
				}
			});
			
			
		}
	}
	
	
	
	/**
	 * 创建一个表情选择对话框
	 */
	private void createExpressionDialog() {
		builder = new Dialog(context);
		GridView gridView = createGridView();
		builder.setContentView(gridView);
		builder.setTitle("default expression");
		builder.show();

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						imageIds[position % imageIds.length]);
				ImageSpan imageSpan = new ImageSpan(context, bitmap);
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
		final GridView view = new GridView(context);
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

		SimpleAdapter simpleAdapter = new SimpleAdapter(context, listItems,
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
	
	/**
	 * 自定义Handler
	 *
	 */
	private class MyHandler extends Handler {
		private CommentAdapter commentAdapter;
		private List<Comment> listComments;
		
		public MyHandler() {
		}
		

		public MyHandler(CommentAdapter commentAdapter,
				List<Comment> listComments) {
			super();
			this.commentAdapter = commentAdapter;
			this.listComments = listComments;
		}

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				List<Comment> lists = (List<Comment>) msg.obj;
				Log.d(TAG, "Handler-->lists:"+lists);
				Log.d(TAG, "commentAdapter.getmList(): "+commentAdapter.getmList());
				commentAdapter.setData(lists);
//				commentAdapter.getmList().add(lists.get(lists.size()-1));
				commentAdapter.notifyDataSetChanged();
				break;
			
			}
			
		}
	}
}
