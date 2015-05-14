package com.matrix.wechat.adapter;

import java.util.ArrayList;
import java.util.List;

import com.matrix.wechat.R;
import com.matrix.wechat.model.CurrentMoment;
import com.matrix.wechat.model.Moment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyMonentAdapter extends BaseAdapter{

	private List<CurrentMoment> mList = new ArrayList<CurrentMoment>();
	private LayoutInflater mInflater;

	
	public MyMonentAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
	}

	public void setData(List<CurrentMoment> list) {
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
			convertView=mInflater.inflate(R.layout.item_my_moment, null);
			holder = new ViewHolder();		
			
			holder.tv_moment_date=(TextView) convertView.findViewById(R.id.my_moment_date);
			holder.tv_moment_content=(TextView) convertView.findViewById(R.id.my_moment_content);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_moment_date.setText(mList.get(position).getDate());
		holder.tv_moment_content.setText(mList.get(position).getContext());
		return convertView;
	}
	
	static class ViewHolder{
		public TextView tv_moment_date;
		public TextView tv_moment_content;
	}

}
