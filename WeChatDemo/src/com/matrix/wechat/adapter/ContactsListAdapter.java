package com.matrix.wechat.adapter;

import static com.matrix.wechat.global.Constants.OFF_LINE;
import static com.matrix.wechat.global.Constants.ON_LINE;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matrix.wechat.R;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.BitmapUtil;


public class ContactsListAdapter extends BaseAdapter {
	private Context context;
	private List<User> contacts;

	public ContactsListAdapter(Context context, List<User> contacts) {
		this.context = context;
		this.contacts = contacts;
	}

	@Override
	public int getCount() {
		return contacts.size();
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
		final LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.item_contact, null);
		System.out.println("id" + convertView.getId());
		
		final ImageView headPortrait = (ImageView) convertView.findViewById(R.id.head_portrait_IMG);
		final TextView nickName = (TextView) convertView.findViewById(R.id.nick_name_TV);
		final TextView status = (TextView) convertView.findViewById(R.id.status_TV);
		final RelativeLayout off_rl = (RelativeLayout) convertView.findViewById(R.id.off_RL);
		
		boolean isOnLine = contacts.get(position).getStatus() == 1 ? true : false;
		headPortrait.setImageBitmap(BitmapUtil.getBitmap(contacts.get(position).getPicture()));
		nickName.setText(contacts.get(position).getNickname());
		if(isOnLine){
			status.setText(ON_LINE);
			off_rl.setVisibility(View.INVISIBLE);
		} else {
			status.setText(OFF_LINE);
			off_rl.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
}
