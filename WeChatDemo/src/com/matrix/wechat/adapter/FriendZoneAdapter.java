package com.matrix.wechat.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matrix.wechat.R;
import com.matrix.wechat.customview.CommentListView;
import com.matrix.wechat.model.Moment;
import com.matrix.wechat.utils.BitmapUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
	String[] strs = new String[] {"first", "second", "third", "fourth", "fifth"};
	String[] reply_names = new String[] {"XX", "XX回复XX", "XX", "XX回复XX"};
	String[] reply_contents=new String[]{"aaaaaaa","bbbbb","cccc","ddd"};
	
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
				
				frl_comment.setVisibility(View.VISIBLE);
			}
		});
		
//		holder.lv_comments.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,strs));
	
		List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();  
		for (int i = 0; i < reply_names.length; i++) {  
	            Map<String, Object> listem = new HashMap<String, Object>();  
	            listem.put("reply_names", reply_names[i]+":");  
	            listem.put("reply_contents", reply_contents[i]);   
	            listems.add(listem);  
	    }  
		SimpleAdapter adapter=new SimpleAdapter(context, listems, R.layout.item_comment, 
				new String[]{"reply_names","reply_contents"}, new int[] {R.id.tv_comment_reply,R.id.tv_comment_content});
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
		
	}
	
	
}
