package com.matrix.wechat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	private List<Comment> listcomment;
	
	
	public FriendZoneAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
		this.context=context;
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
		
		holder.iv_addComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "position:"+position);
				shareid=mList.get(position).getMomentid();
				Log.d(TAG, "shareid:"+shareid);
				
				frl_comment.setVisibility(View.VISIBLE);
			}
		});
		
		listcomment = new ArrayList<Comment>();
		
//		listcomment=getListComments();		
		listcomment=mList.get(position).getCommentsList();
		CommentAdapter adapter = new CommentAdapter(context);
		if (listcomment != null) {
			adapter.setData(listcomment);
		} else{
			adapter.setData(new ArrayList<Comment>());
		}
		holder.lv_comments.setAdapter(adapter);
		adapter.notifyDataSetChanged();		
		
		holder.comment_content_send.setOnClickListener(new OnClickListener() {
//			ViewHolder holder=new ViewHolder();
			User user=null;
			@Override
			public void onClick(View v) {
				//获取输入框内容
				EditText et_comment_content=(EditText) frl_comment.findViewById(R.id.et_comment_content);	
//				holder.comment_content_send=(Button) frl_comment.findViewById(R.id.comment_content_send);
				comment_content=et_comment_content.getText().toString();
				
				frl_comment.setVisibility(View.GONE);
				et_comment_content.setText("");
				new Thread(new Runnable() {
					
					@Override
					public void run() {						
						PersonalInfoService perInfoService=PersonalInfoFactory.getInstance();
						user=perInfoService.getUserByUsername(mList.get(position).getUserName());
						sharetoid=(int) user.getUserid();
						Log.d(TAG, "shareid:after:"+shareid);
						new AddComment().execute(Integer.toString(shareid),Long.toString(sharetoid),comment_content);
					}
				}).start();				
			}
		});
		holder.lv_comments.setOnItemClickListener(new OnItemClickListenerTest(holder.lv_comments,frl_comment));
		
		
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
			}else{
				Toast.makeText(context, "comment failed", Toast.LENGTH_SHORT).show();
			}
		}
	}	
	
	class OnItemClickListenerTest implements OnItemClickListener{
		private CommentListView listView;
		private RelativeLayout frl_comment;
		public OnItemClickListenerTest(CommentListView listView,RelativeLayout layout) {
			super();
			this.listView = listView;
			this.frl_comment = layout;
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
								new AddComment().execute(Integer.toString(comment.getShareid()),Integer.toString(sharetoid),comment_content);
							}
						}
					}).start();
				}
			});
		}
		
	}
	
	
}
