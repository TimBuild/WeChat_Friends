package com.matrix.wechat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.wechat.R;
import com.matrix.wechat.activity.FriendZoneActivity;
import com.matrix.wechat.customview.CommentListView;
import com.matrix.wechat.customview.FriendsListView;
import com.matrix.wechat.model.Comment;
import com.matrix.wechat.model.Moment;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.web.service.FriendsZoneService;
import com.matrix.wechat.web.service.PersonalInfoService;
import com.matrix.wechat.web.service.factory.FriendsZoneFactory;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;

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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;	
		
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.item_friend_zone, null);
			holder = new ViewHolder();		
			holder.img_icon = (ImageView) convertView.findViewById(R.id.moment_icon);
			holder.tv_username=(TextView) convertView.findViewById(R.id.moment_username);
			holder.tv_content_text=(TextView) convertView.findViewById(R.id.moment_content);
			holder.tv_date=(TextView) convertView.findViewById(R.id.moment_date);
			holder.iv_addComment=(ImageView) convertView.findViewById(R.id.add_comment_img);
			holder.lv_comments=(CommentListView) convertView.findViewById(R.id.lv_comments);
			holder.et_comment_content=(EditText) frl_comment.findViewById(R.id.et_comment_content);	
			holder.comment_content_send=(Button) frl_comment.findViewById(R.id.comment_content_send);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img_icon.setImageBitmap(BitmapUtil.getBitmap(mList.get(position).getPicture()));
		holder.tv_username.setText(mList.get(position).getUserName());
		holder.tv_content_text.setText(mList.get(position).getContent_text());
		holder.tv_date.setText(mList.get(position).getDate());
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
		listcomment=mList.get(position).getCommentsList();
		CommentAdapter adapter = new CommentAdapter(context);
		adapter.setData(listcomment);
		
		holder.lv_comments.setAdapter(adapter);
		adapter.notifyDataSetChanged();	
//		handler = new MyHandler(adapter, listcomment);
		//.....................
//		holder.comment_content_send.setOnClickListener(new OnSendClickListener(frl_comment, mList.get(position).getUserName(),adapter,mList.get(position).getCommentsList()));
		holder.lv_comments.setOnItemClickListener(new OnCommentItemClickListener(holder.lv_comments,frl_comment,adapter,listcomment));
		
		//长按删除评论
		holder.lv_comments.setOnItemLongClickListener(new OnCommentItemLongListener(holder.lv_comments,frl_comment,adapter,listcomment));
		
//		friendsListView.setOnItemLongClickListener(new OnFriendItemClickListener(mList));
		
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
		

		public OnCommentItemClickListener(CommentListView listView,
				RelativeLayout frl_comment, CommentAdapter commentAdapter,
				List<Comment> listcComments) {
			super();
			this.listView = listView;
			this.frl_comment = frl_comment;
			this.commentAdapter = commentAdapter;
			this.listComments = listcComments;
		}


		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
			frl_comment.setVisibility(View.VISIBLE);
			
			//获取输入框内容
			final EditText et_comment_content=(EditText) frl_comment.findViewById(R.id.et_comment_content);	
			final Button et_button = (Button) frl_comment.findViewById(R.id.comment_content_send_item);
			final Button et_button_before = (Button) frl_comment.findViewById(R.id.comment_content_send);
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
							if(comment_content.equals("")||comment_content!=null){
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
		}
		
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
