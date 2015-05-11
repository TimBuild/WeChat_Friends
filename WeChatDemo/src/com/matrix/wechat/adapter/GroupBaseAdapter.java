package com.matrix.wechat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.wechat.R;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.BitmapUtil;

public class GroupBaseAdapter extends BaseAdapter{
	
	private Context context;
	private List<User> users;
	public List<Boolean> mChecked = new ArrayList<Boolean>();
	
	public GroupBaseAdapter(Context context,List<User> user){
		this.context = context;
		this.users = user;
		
//		mChecked = new ArrayList<Boolean>();
		/*for(int i=0;i<friendRequest.size();i++){
			mChecked.add(false);
		}*/
	}

	@Override
	public int getCount() {
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = LayoutInflater.from(context);
		
		convertView = inflater.inflate(R.layout.item_contact_group, null);
		
		for(int i=0;i<users.size();i++){
			mChecked.add(false);
		}
		
		TextView nickName = (TextView) convertView.findViewById(R.id.group_nick_name_TV);
		CheckBox check = (CheckBox) convertView.findViewById(R.id.group_check);
		
		ImageView headIcon = (ImageView) convertView.findViewById(R.id.group_head_portrait_IMG);
		
		
		final int p=position;
		check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox)v;
				mChecked.set(p, cb.isChecked());
			}
		});
		
		nickName.setText(users.get(position).getNickname());
		check.setChecked(mChecked.get(position));
		
		headIcon.setImageBitmap(BitmapUtil.getBitmap(users.get(position).getPicture()));
		return convertView;
	}
	

}
