package com.matrix.wechat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matrix.wechat.R;
import com.matrix.wechat.model.Comment;

public class CommentAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private List<Comment> mList = new ArrayList<Comment>();
	private static String TAG = "CommentAdapter";
	
	public CommentAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);
		this.context=context;
	}

	public void setData(List<Comment> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;	
		
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.item_comment, null);
			holder = new ViewHolder();		
			holder.tv_comment_from = (TextView) convertView.findViewById(R.id.tv_comment_from);
			holder.tv_comment_to = (TextView) convertView.findViewById(R.id.tv_comment_to);
			holder.tv_comment_content=(TextView) convertView.findViewById(R.id.tv_comment_content);
						
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.tv_comment_from.setText(mList.get(position).getSharefromname());
		holder.tv_comment_to.setText(mList.get(position).getSharetoname());
		holder.tv_comment_content.setText(mList.get(position).getContent());
		
		
		return convertView;
	}
	
	public List<Comment> getmList() {
		return mList;
	}

	public void setmList(List<Comment> mList) {
		this.mList = mList;
	}



	static class ViewHolder{
		public TextView tv_comment_from;
		public TextView tv_comment_to;
		public TextView tv_comment_content;	

	}



}
