package com.matrix.wechat.logic;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.wechat.R;
import com.matrix.wechat.model.FriendRequest;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.FormatDate;
import com.matrix.wechat.web.Request;

import static com.matrix.wechat.global.Constants.*;
public class Dialogs {
	public static final void showFriendInfoDialog(final Activity _activity,
			final User user, final long from, final long to) {
		final Dialog dialog = new Dialog(_activity, R.style.DialogWithoutTitle);
		dialog.setContentView(R.layout.dialog_info);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

		final ImageView personalIcon = (ImageView) dialog
				.findViewById(R.id.dialog_personal_icon_IMG);
		final TextView username = (TextView) dialog
				.findViewById(R.id.dialog_personal_username_TV);
		final TextView nickname = (TextView) dialog
				.findViewById(R.id.dialog_personal_nick_name_TV);
		final Button addBTN = (Button) dialog
				.findViewById(R.id.btn_dialog_add_new_friend);
		final Button cancelBTN = (Button) dialog
				.findViewById(R.id.btn_dialog_cancel);

		personalIcon.setImageBitmap(BitmapUtil.getBitmap(user.getPicture()));
		username.setText(user.getUsername());
		nickname.setText(user.getNickname());
		
		addBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				new Request(_activity, API_ADD_FRIEND_REQUEST, true).execute(from,to);
				dialog.dismiss();
			}
		});
		
		cancelBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View _view) {
				dialog.dismiss();
			}
		});

		if(from == to){
			addBTN.setVisibility(View.GONE);
		}
		
		boolean isFriend = false;
		
		for(User contact:ContactsViewWorker.contacts){
			if(contact.getUserid() == to){
				isFriend = true;
				break;
			}
		}
		
		if(isFriend){
			addBTN.setVisibility(View.GONE);
		}
		
		try {
			dialog.show();
		} catch (final WindowManager.BadTokenException _e) {
			_e.printStackTrace();
		}
	}
	
	public static final void showRequestInfoDialog(final Activity _activity,
			final User user, final FriendRequest request) {
		final Dialog dialog = new Dialog(_activity, R.style.DialogWithoutTitle);
		dialog.setContentView(R.layout.dialog_request_info);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);

		final ImageView personalIcon = (ImageView) dialog
				.findViewById(R.id.dialog_request_icon_IMG);
		final TextView username = (TextView) dialog
				.findViewById(R.id.dialog_request_username_TV);
		final TextView nickname = (TextView) dialog
				.findViewById(R.id.dialog_request_nick_name_TV);
		final TextView date = (TextView) dialog
				.findViewById(R.id.dialog_request_date_TV);
		
		final Button accessBTN = (Button) dialog
				.findViewById(R.id.btn_dialog_access_new_friend);
		final Button rejectBTN = (Button) dialog
				.findViewById(R.id.btn_dialog_reject_new_friend);

		personalIcon.setImageBitmap(BitmapUtil.getBitmap(user.getPicture()));
		username.setText(user.getUsername());
		nickname.setText(user.getNickname());
		date.setText(FormatDate.TimeStamp2Date(request.getDate().toString(), "yyyy-MM-dd HH:mm:ss"));
		
		accessBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Request(_activity, API_RESPONSE_REQUEST, true).execute(request.getRequestid(),ACCEPT,dialog);
			}
		});
		
		rejectBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View _view) {
				new Request(_activity, API_RESPONSE_REQUEST, true).execute(request.getRequestid(),REJECT,dialog);
			}
		});
		
		if(request.getStatus() != 0){
			accessBTN.setVisibility(View.GONE);
			rejectBTN.setVisibility(View.GONE);
		}

		try {
			dialog.show();
		} catch (final WindowManager.BadTokenException _e) {
			_e.printStackTrace();
		}
	}
}
