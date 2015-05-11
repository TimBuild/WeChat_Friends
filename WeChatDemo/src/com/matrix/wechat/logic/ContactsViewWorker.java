package com.matrix.wechat.logic;


import static com.matrix.wechat.utils.FormatDate.toUnixTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.ContactsListAdapter;
import com.matrix.wechat.model.ChatHistoryContact;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.NetworkUtil;
import com.matrix.wechat.web.Request;

import static com.matrix.wechat.global.Constants.API_CONTACTS;

public class ContactsViewWorker {
	public static Activity activity;

	public static ListView contactList;
	public static View layout;
	public static List<User> contacts;
	public static ContactsListAdapter adapter;

	public static void initialize(Activity _activity, View _layout) {
		activity = _activity;
		layout = _layout;
		Log.i("info", "initialize");

		if (contacts == null)
			contacts = new ArrayList<User>();
		if (adapter == null)
			adapter = new ContactsListAdapter(_layout.getContext(), contacts);

		contactList = (ListView) layout.findViewById(R.id.contacts_LV);
		contactList.setAdapter(adapter);

		contactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (NetworkUtil.isNetworkConnected(activity)) {
					ChatHistoryContact chatHistoryContact = new ChatHistoryContact();
					User contact = contacts.get(arg2);
					chatHistoryContact.setDate(toUnixTime(new Date()));
					chatHistoryContact.setMessage("");
					chatHistoryContact.setNickname(contact.getNickname());
					chatHistoryContact.setPicture(contact.getPicture());
					chatHistoryContact.setUserid((int) contact.getUserid());
					chatHistoryContact.setUsername(contact.getUsername());
					new ShowAllContactsHistory()
							.openChatDialog(chatHistoryContact);
				} else {
					Toast.makeText(activity, "network anomaly",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		if (NetworkUtil.isNetworkConnected(activity))
			new Request(_activity, API_CONTACTS, true)
					.execute("current User id");

	}
}
