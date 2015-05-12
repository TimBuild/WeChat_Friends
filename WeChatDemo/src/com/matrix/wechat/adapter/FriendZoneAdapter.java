package com.matrix.wechat.adapter;

import java.util.ArrayList;
import java.util.List;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.ChatMsgViewAdapter.ViewHolder;
import com.matrix.wechat.model.Moment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FriendZoneAdapter extends BaseAdapter{

	private List<Moment> mList = new ArrayList<Moment>();
	private Context mContext;
	private LayoutInflater mInflater;
	
	public FriendZoneAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
	}

	public void setData(List<Moment> list) {
		this.mList = list;
	}
	
	@Override
	public int getCount() {
		return 20;
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
			convertView=mInflater.inflate(R.layout.item_friend_zone, null);
			holder = new ViewHolder();		
			
			holder.tv_username=(TextView) convertView.findViewById(R.id.moment_username);
			holder.tv_content_text=(TextView) convertView.findViewById(R.id.moment_content);
			holder.tv_date=(TextView) convertView.findViewById(R.id.moment_date);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
//		holder.tv_username.setText(mList.get(position).getUserName());
//		holder.tv_content_text.setText(mList.get(position).getContent_text());
//		holder.tv_date.setText(mList.get(position).getDate());

		return convertView;
	}
	
	static class ViewHolder{
		public TextView tv_username;
		public TextView tv_content_text;
		public TextView tv_date;
	}
}
