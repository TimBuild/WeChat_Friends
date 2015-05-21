package com.matrix.wechat.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matrix.wechat.R;
import com.matrix.wechat.customview.CommentListView;
import com.matrix.wechat.model.Comment;
import com.matrix.wechat.model.Moment;
import com.matrix.wechat.model.Share;
import com.matrix.wechat.model.ShareWithComment;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.web.service.FriendsZoneService;
import com.matrix.wechat.web.service.PersonalInfoService;
import com.matrix.wechat.web.service.factory.FriendsZoneFactory;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FriendZoneAdapter extends BaseAdapter{

	private List<Moment> mList = new ArrayList<Moment>();
	private LayoutInflater mInflater;
	private static String TAG = "FriendZoneAdapter";
	private RelativeLayout frl_comment;
	private Context context;
	private String comment_content="";
	private int shareid=-1;
	private long sharetoid=-1;
	private List<Comment> listcomment=new ArrayList<Comment>();
	
	public FriendZoneAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
		this.context=context;
	}

	public void setData(List<Moment> list) {
		this.mList = list;
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
		
		holder.iv_addComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "position:"+position);
				shareid=mList.get(position).getMomentid();
				frl_comment.setVisibility(View.VISIBLE);
			}
		});
		 
		holder.comment_content_send.setOnClickListener(new OnClickListener() {
			ViewHolder holder=new ViewHolder();
			User user=null;
			@Override
			public void onClick(View v) {
				//获取输入框内容
				holder.et_comment_content=(EditText) frl_comment.findViewById(R.id.et_comment_content);	
				holder.comment_content_send=(Button) frl_comment.findViewById(R.id.comment_content_send);
				comment_content=holder.et_comment_content.getText().toString();
				
				frl_comment.setVisibility(View.GONE);
				holder.et_comment_content.setText("");
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {						
						PersonalInfoService perInfoService=PersonalInfoFactory.getInstance();
						user=perInfoService.getUserByUsername(mList.get(position).getUserName());
						sharetoid=user.getUserid();
						new AddComment().execute(Integer.toString(shareid),Long.toString(sharetoid),comment_content);
					}
				}).start();				
			}
		});
		
		CommentAdapter adapter=new CommentAdapter(context);
//		listcomment=getListComments();
		listcomment=mList.get(position).getCommentsList();
		adapter.setData(listcomment);
		holder.lv_comments.setAdapter(adapter);
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
	}	
	
	private List<Comment> getListComments(int momentid){

		List<Comment> list=new ArrayList<Comment>();
		return list;
	}
}
