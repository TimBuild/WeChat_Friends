package com.matrix.wechat.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.matrix.wechat.R;
import com.matrix.wechat.model.FriendRequest;

public class FriendRequestListAdapter extends BaseAdapter {
	private Context context;
	public List<FriendRequest> friendRequests;

	public FriendRequestListAdapter(Context context, List<FriendRequest> friendRequests) {
		this.context = context;
		this.friendRequests = friendRequests;
	}

	@Override
	public int getCount() {
		return friendRequests.size();
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
		convertView = inflater.inflate(R.layout.item_request_friend, null);
		
		final TextView nickName = (TextView) convertView.findViewById(R.id.request_nick_name_TV);
		final TextView status = (TextView) convertView.findViewById(R.id.respone_status_TV);
		
		nickName.setText(friendRequests.get(position).getNickname());
		String statusTest;
		int statusValue = friendRequests.get(position).getStatus();
		
		if(statusValue == 0){
			statusTest = "Wait";
		} else if(statusValue == 1){
			statusTest = "Access";
		} else {
			statusTest = "Reject";
		}
		
		status.setText(statusTest);
		return convertView;
	}
}
