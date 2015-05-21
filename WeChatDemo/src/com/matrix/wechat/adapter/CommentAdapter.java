package com.matrix.wechat.adapter;

import java.util.ArrayList;
import java.util.List;

import com.matrix.wechat.R;
import com.matrix.wechat.model.Comment;
import com.matrix.wechat.web.service.FriendsZoneService;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context context;
	private List<Comment> mList = new ArrayList<Comment>();
	
	public CommentAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);
		this.context=context;
	}

	public void setData(List<Comment> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;	
		
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.item_comment, null);
			holder = new ViewHolder();		
			holder.tv_comment_reply = (TextView) convertView.findViewById(R.id.tv_comment_reply);
			holder.tv_comment_content=(TextView) convertView.findViewById(R.id.tv_comment_content);
						
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_comment_reply.setText(mList.get(position).getUsername_reply());
		holder.tv_comment_content.setText(mList.get(position).getContent());
		
		return convertView;
	}
	
	static class ViewHolder{
		public TextView tv_comment_reply;
		public TextView tv_comment_content;	
	}
}
