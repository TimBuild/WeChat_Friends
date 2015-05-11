package com.matrix.wechat.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshSwipeListView;
import com.matrix.wechat.R;
import com.matrix.wechat.activity.AddNewFriendActivity;
import com.matrix.wechat.activity.ChatActivity;
import com.matrix.wechat.activity.MainWeixin.ContactsList;
import com.matrix.wechat.dao.ChatWithUserPass;
import com.matrix.wechat.global.Constants;
import com.matrix.wechat.model.ChatHistoryContact;
import com.matrix.wechat.model.GroupLastMessage;
import com.matrix.wechat.sqlite.dao.OperateContactsHistoryDao;
import com.matrix.wechat.sqlite.dao.impl.OperateContactsHistoryDaoImpl;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.FormatDate;
import com.matrix.wechat.utils.NetworkUtil;
import com.matrix.wechat.utils.ReadProperties;
import com.matrix.wechat.web.service.factory.ChatHistoryContactFactory;
import com.matrix.wechat.web.service.factory.GroupServiceFactory;

@SuppressLint({ "ViewHolder", "InflateParams" })
public class ShowAllContactsHistory implements ContactsList, ChatWithUserPass {
	protected static final String TAG = "ShowAllContactsHistory";
	private static Context context;
	private static Integer screenWidth;
	private static ContactsHistoryAdapter mContactshistoryAdapter = null;
	private static List<ChatHistoryContact> mChatHistoryContacts = null;
	private static List<GroupLastMessage> mChatHistoryContactGroups = null;
	private static PullToRefreshSwipeListView contactsHistoryPullToRefreshSwipeListView = null;
	private static EditText find_contact_filter = null;
	private static Button btn_add_new_friend_weixin = null;
	private static List<ChatHistoryContact> filteredChatHistoryContacts = null;

	private static OperateContactsHistoryDao operateContactsHistoryDao = new OperateContactsHistoryDaoImpl();

	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				contactsHistoryPullToRefreshSwipeListView.onRefreshComplete();
				break;
			}
		}

	};

	private static void initDatas() {
		mChatHistoryContacts = new ArrayList<ChatHistoryContact>();
		mChatHistoryContactGroups = new ArrayList<GroupLastMessage>();
		if (NetworkUtil.isNetworkConnected(context))
			new GetDataTask().execute(Constants.API_MESSAGE);
		else {
			Toast.makeText(context, "network anomaly", Toast.LENGTH_LONG)
					.show();
		}
		Log.i("info", "initDatas");
	}

	public static void showContactsHistory(Context contextPass,
			Integer widthOfScreen, View view) {
		context = contextPass;
		screenWidth = widthOfScreen;

		contactsHistoryPullToRefreshSwipeListView = (PullToRefreshSwipeListView) view
				.findViewById(R.id.contacts_history_listview);
		find_contact_filter = (EditText) view
				.findViewById(R.id.find_contact_filter);
		btn_add_new_friend_weixin = (Button) view
				.findViewById(R.id.btn_add_new_friend_weixin);

		initDatas();

		contactsHistoryPullToRefreshSwipeListView.getRefreshableView()
				.setOffsetLeft(screenWidth - screenWidth / 8);

		// // 下拉刷新时的提示文本设置
		// contactsHistoryPullToRefreshSwipeListView.getLoadingLayoutProxy(true,
		// false).setLastUpdatedLabel("下拉刷新");
		// contactsHistoryPullToRefreshSwipeListView.getLoadingLayoutProxy(true,
		// false).setPullLabel("");
		// contactsHistoryPullToRefreshSwipeListView.getLoadingLayoutProxy(true,
		// false).setRefreshingLabel("正在刷新");
		// contactsHistoryPullToRefreshSwipeListView.getLoadingLayoutProxy(true,
		// false).setReleaseLabel("放开以刷新");
		// // 上拉加载更多时的提示文本设置
		// contactsHistoryPullToRefreshSwipeListView.getLoadingLayoutProxy(false,
		// true).setLastUpdatedLabel("上拉加载");
		// contactsHistoryPullToRefreshSwipeListView.getLoadingLayoutProxy(false,
		// true).setPullLabel("");
		// contactsHistoryPullToRefreshSwipeListView.getLoadingLayoutProxy(false,
		// true).setRefreshingLabel("正在加载...");
		// contactsHistoryPullToRefreshSwipeListView.getLoadingLayoutProxy(false,
		// true).setReleaseLabel("放开以加载");

		contactsHistoryPullToRefreshSwipeListView.getRefreshableView()
				.setSwipeCloseAllItemsWhenMoveList(true);
		contactsHistoryPullToRefreshSwipeListView
				.setOnRefreshListener(new OnRefreshListener<SwipeListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<SwipeListView> refreshView) {
						if (NetworkUtil.isNetworkConnected(context)) {
							find_contact_filter.setText("");
							filteredChatHistoryContacts.clear();
							new GetDataTask().execute(Constants.API_MESSAGE);
						} else {
							Toast.makeText(context, "network anomaly",
									Toast.LENGTH_LONG).show();
							Message message = Message.obtain();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				});

		contactsHistoryPullToRefreshSwipeListView.getRefreshableView()
				.setSwipeListViewListener(new BaseSwipeListViewListener() {

					@Override
					public void onChoiceChanged(int position, boolean selected) {
						Log.d(TAG, "onChoiceChanged:" + position + ", "
								+ selected);
					}

					@Override
					public void onChoiceEnded() {
						Log.d(TAG, "onChoiceEnded");
					}

					@Override
					public void onChoiceStarted() {
						Log.d(TAG, "onChoiceStarted");
					}

					@Override
					public void onClickBackView(int position) {
						Log.d(TAG, "onClickBackView:" + position);
					}

					@Override
					public void onClickFrontView(int position) {
						Log.d(TAG, "onClickFrontView:" + position);
					}

					@Override
					public void onDismiss(int[] reverseSortedPositions) {
						Log.d(TAG, "onDismiss");
					}

					@Override
					public void onFirstListItem() {
						Log.d(TAG, "onFirstListItem");
					}

					@Override
					public void onLastListItem() {
						Log.d(TAG, "onLastListItem");
					}

					@Override
					public void onListChanged() {
						Log.d(TAG, "onListChanged");

						contactsHistoryPullToRefreshSwipeListView
								.getRefreshableView().closeOpenedItems();
					}

					@Override
					public void onStartOpen(int position, int action,
							boolean right) {
						contactsHistoryPullToRefreshSwipeListView
								.getRefreshableView().closeOpenedItems();

						Log.d(TAG, "onStartOpen:" + position + "," + action
								+ "," + right);
					}

					@Override
					public void onMove(int position, float x) {
						Log.d(TAG, "onMove:" + position + "," + x);
					}

					@Override
					public void onOpened(int position, boolean toRight) {
						Log.d(TAG, "onOpened:" + position + "," + toRight);
					}

					@Override
					public void onStartClose(int position, boolean right) {
						Log.d(TAG, "onStartClose:" + position + "," + right);
					}

					@Override
					public void onClosed(int position, boolean fromRight) {
						Log.d(TAG, "onClosed:" + position + "," + fromRight);
					}
				});

		find_contact_filter.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String filterStr = find_contact_filter.getText().toString();
				for (ChatHistoryContact chatHistoryContact : mChatHistoryContacts) {
					if (chatHistoryContact.getNickname().indexOf(filterStr) != -1) {
						filteredChatHistoryContacts.add(chatHistoryContact);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				if (filteredChatHistoryContacts == null) {
					filteredChatHistoryContacts = new ArrayList<ChatHistoryContact>();
				} else {
					filteredChatHistoryContacts.clear();
				}
				contactsHistoryPullToRefreshSwipeListView.setEnabled(false);
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(find_contact_filter.getText().toString()
						.trim())) {
					filteredChatHistoryContacts.clear();
					mContactshistoryAdapter = new ContactsHistoryAdapter(
							mChatHistoryContacts, screenWidth);
				} else {
					mContactshistoryAdapter = new ContactsHistoryAdapter(
							filteredChatHistoryContacts, screenWidth);
				}
				contactsHistoryPullToRefreshSwipeListView
						.setAdapter(mContactshistoryAdapter);
				mContactshistoryAdapter.notifyDataSetChanged();
				contactsHistoryPullToRefreshSwipeListView.setEnabled(true);
			}
		});

		btn_add_new_friend_weixin
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								AddNewFriendActivity.class);
						context.startActivity(intent);
					}
				});
	}

	private static class ContactsHistoryAdapter extends BaseAdapter {
		private List<ChatHistoryContact> mChatHistoryContacts;
		private Integer wOfScreen;

		public ContactsHistoryAdapter(
				List<ChatHistoryContact> chatHistoryContacts, Integer sWidth) {
			this.mChatHistoryContacts = chatHistoryContacts;
			this.wOfScreen = sWidth;
		}

		@Override
		public int getCount() {
			return mChatHistoryContacts.size();
		}

		@Override
		public Object getItem(int position) {
			return mChatHistoryContacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_weixin_swipe_listview, null);
			if (mChatHistoryContacts.size() > 0) {
				final ChatHistoryContact chatHistoryContact = mChatHistoryContacts
						.get(position);

				TextView history_contact_userid = (TextView) convertView
						.findViewById(R.id.history_contact_userid);
				history_contact_userid.setText(""
						+ chatHistoryContact.getUserid());

				TextView history_contact_username = (TextView) convertView
						.findViewById(R.id.history_contact_username);
				TextView isGroup_TV = (TextView) convertView
						.findViewById(R.id.isGroup_TV);
				history_contact_username.setText(chatHistoryContact
						.getUsername());
				
				ImageView history_contact_head = (ImageView) convertView
						.findViewById(R.id.history_contact_head);
				history_contact_head.setImageBitmap(BitmapUtil
						.getBitmap(chatHistoryContact.getPicture()));

				TextView history_contact_name = (TextView) convertView
						.findViewById(R.id.history_contact_name);
				history_contact_name.setText(chatHistoryContact.getNickname());
				
				TextView history_group_name = (TextView) convertView
						.findViewById(R.id.group_name);
				//先设置固定的群名称
				System.out.println("chatHistoryContact.isGroup()---->"+chatHistoryContact.isGroup());
				if(chatHistoryContact.isGroup()){
					history_group_name.setText(chatHistoryContact.getGroupName());
					history_group_name.setVisibility(View.VISIBLE);
					history_contact_name.setVisibility(View.INVISIBLE);
					isGroup_TV.setText("true");
				}
				else{
					history_group_name.setVisibility(View.INVISIBLE);
					history_contact_name.setVisibility(View.VISIBLE);
					isGroup_TV.setText("false");
				}

				TextView history_contact_message = (TextView) convertView
						.findViewById(R.id.history_contact_message);
				if (chatHistoryContact.getMessage().length() > 20) {
					history_contact_message.setText(chatHistoryContact
							.getMessage().substring(0, 18));
				} else {
					history_contact_message.setText(chatHistoryContact
							.getMessage());
				}

				TextView history_contact_date = (TextView) convertView
						.findViewById(R.id.history_contact_date);
				history_contact_date.setText(FormatDate.TimeStamp2Date(
						chatHistoryContact.getDate(), "yyyy-MM-dd HH:mm:ss"));

				TextView deleteItem = (TextView) convertView
						.findViewById(R.id.contacts_remove);
				LinearLayout.LayoutParams delbtn_params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
				delbtn_params.width = wOfScreen / 8;
				deleteItem.setLayoutParams(delbtn_params);

				deleteItem.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (operateContactsHistoryDao.deleteContactsFromList(
								context, chatHistoryContact.getUserid())) {
							mChatHistoryContacts.remove(position);
							mContactshistoryAdapter.notifyDataSetChanged();
							contactsHistoryPullToRefreshSwipeListView
									.getRefreshableView().closeOpenedItems();
						} else {
							mContactshistoryAdapter.notifyDataSetChanged();
							contactsHistoryPullToRefreshSwipeListView
									.getRefreshableView().closeOpenedItems();
						}
					}
				});
			}
			return convertView;
		}
	}

	private static class GetDataTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mChatHistoryContacts.clear();
			mChatHistoryContactGroups.clear();
			contactsHistoryPullToRefreshSwipeListView.setEnabled(false);
		}

		@Override
		protected Void doInBackground(String... params) {
			String getChatWithUrl = params[0];
			// get data from server
			mChatHistoryContacts = ChatHistoryContactFactory.getInstance(
					).getChatHistoryContacts(Constants.USER_ID);
			
			mChatHistoryContactGroups = GroupServiceFactory.getInstance().getGroupLastMessgae(Constants.USER_ID);
			if (mChatHistoryContacts == null) {
				mChatHistoryContacts = new ArrayList<ChatHistoryContact>();
			}
			if(mChatHistoryContactGroups == null){
				mChatHistoryContactGroups = new ArrayList<GroupLastMessage>();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			List<Integer> userIdList = operateContactsHistoryDao
					.getAllDeletedContactId(context);
			for (int i = 0; i < userIdList.size(); i++) {
				for (int j = 0; j < mChatHistoryContacts.size(); j++) {
					ChatHistoryContact chatHistoryContact = mChatHistoryContacts
							.get(j);
					if (userIdList.get(i) == chatHistoryContact.getUserid()) {
						mChatHistoryContacts.remove(chatHistoryContact);
					}
				}
			}
			
			for (ChatHistoryContact cc : mChatHistoryContacts) {
				Log.i("Show", "---------->load id:" + cc.getUserid());
			}
//			System.out.println("----------------------->"+mChatHistoryContactGroups.size());
//			System.out.println("----------------------->"+mChatHistoryContactGroups.get(0).getGroupid());
//			System.out.println("----------------------->"+mChatHistoryContactGroups.get(0).getGroupname());
//			System.out.println("----------------------->"+mChatHistoryContactGroups.get(0).getMessage());
//			System.out.println("----------------------->"+mChatHistoryContactGroups.get(0).getSendtime());
//			
			Bitmap bmp = BitmapFactory.decodeResource(CacheUtil.context.getResources(), R.drawable.group_icon);
			String group_icon = BitmapUtil.bitmaptoString(bmp);
			for(GroupLastMessage groupLastMessage : mChatHistoryContactGroups){
				if("".equals(groupLastMessage.getSendtime().trim()))
					continue;
				
				ChatHistoryContact chatHistoryContact = new ChatHistoryContact();
				chatHistoryContact.setDate(groupLastMessage.getSendtime());
				chatHistoryContact.setGroup(true);
				chatHistoryContact.setGroupName(groupLastMessage.getGroupname());
				chatHistoryContact.setMessage(groupLastMessage.getMessage());
				chatHistoryContact.setUsername(""+groupLastMessage.getGroupid());
				chatHistoryContact.setUserid(groupLastMessage.getGroupid());
				chatHistoryContact.setPicture(group_icon);
				chatHistoryContact.setNickname(groupLastMessage.getGroupname());
				
				mChatHistoryContacts.add(chatHistoryContact);
			}
			
			mContactshistoryAdapter = new ContactsHistoryAdapter(
					mChatHistoryContacts, screenWidth);
			contactsHistoryPullToRefreshSwipeListView
					.setAdapter(mContactshistoryAdapter);
			mContactshistoryAdapter.notifyDataSetChanged();
			contactsHistoryPullToRefreshSwipeListView.onRefreshComplete();
			contactsHistoryPullToRefreshSwipeListView.setEnabled(true);
		}
	}

	@Override
	public void closeOpenedListItems() {
		contactsHistoryPullToRefreshSwipeListView.getRefreshableView()
				.closeOpenedItems();
	}

	@Override
	public void openChatDialog(ChatHistoryContact chatHistoryContact) {
		boolean isInContactList = false;
		if (mChatHistoryContacts != null) {
			for (ChatHistoryContact historyContact : mChatHistoryContacts) {
				if (historyContact.getUserid() == chatHistoryContact
						.getUserid()) {
					isInContactList = true;
				}
			}
			if (!isInContactList) {
				operateContactsHistoryDao.cancelDeleteContactsFromList(context,
						chatHistoryContact.getUserid());
				mChatHistoryContacts.add(0, chatHistoryContact);
				mContactshistoryAdapter.notifyDataSetChanged();
			}
		}
		Intent intent = new Intent(context, ChatActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString("contact_userid", "" + chatHistoryContact.getUserid());
		bundle.putString("contact_name", "" + chatHistoryContact.getNickname());
		bundle.putString("contact_userName",
				"" + chatHistoryContact.getUsername());
		bundle.putString("isGroup", ""+false);

		Bitmap bitmap = BitmapUtil.getBitmap(chatHistoryContact.getPicture());
		Constants.CHATING_HEAD_IMAGE = bitmap;

		intent.putExtras(bundle);
		context.startActivity(intent);
		if (ChatActivity.mDataArrays != null) {
			ChatActivity.mDataArrays.clear();
			if (ChatActivity.mAdapter != null) {
				ChatActivity.mAdapter.notifyDataSetChanged();
			}
		}
	}
	
	public void openGroupChatDialog(ChatHistoryContact chatHistoryContact) {
		boolean isInContactList = false;
		if (mChatHistoryContacts != null) {

			operateContactsHistoryDao.cancelDeleteContactsFromList(context,
					chatHistoryContact.getUserid());
			mChatHistoryContacts.add(0, chatHistoryContact);
			mContactshistoryAdapter.notifyDataSetChanged();

		}
		Intent intent = new Intent(context, ChatActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString("contact_userid", "" + chatHistoryContact.getUserid());
		bundle.putString("contact_name", "" + chatHistoryContact.getNickname());
		bundle.putString("contact_userName",
				"" + chatHistoryContact.getUsername());
		bundle.putString("isGroup", ""+true);
		bundle.putString("contact_groupName", chatHistoryContact.getGroupName());
		
		//Bitmap bitmap = FormatImage.getBitmap(chatHistoryContact.getPicture());
		//Constants.CHATING_HEAD_IMAGE = bitmap;

		intent.putExtras(bundle);
		context.startActivity(intent);
		if (ChatActivity.mDataArrays != null) {
			ChatActivity.mDataArrays.clear();
			if (ChatActivity.mAdapter != null) {
				ChatActivity.mAdapter.notifyDataSetChanged();
			}
		}
	}

	public static void updateGetChatHistoryContacts(
			ChatHistoryContact chatHistoryContact, HashMap<String, String> pictures) {
		// for (ChatHistoryContact cc : mChatHistoryContacts) {
		// Log.i("Show", "---------->before modify id:" + cc.getUserid());
		// }
		int m = 0;
		if (TextUtils.isEmpty(find_contact_filter.getText().toString().trim())) {
			if (mChatHistoryContacts.size() > 0) {
				for (int i = 0; i < mChatHistoryContacts.size(); i++) {
					ChatHistoryContact historyContact = mChatHistoryContacts
							.get(i);
					if (chatHistoryContact.getUsername().equals(
							historyContact.getUsername())) {
						mChatHistoryContacts.remove(historyContact);
						historyContact.setMessage(chatHistoryContact
								.getMessage());
						historyContact.setDate(chatHistoryContact.getDate());
						mChatHistoryContacts.add(0, historyContact);
						m = 0;
						break;
					} else {
						m = 1;
					}
				}
			} else {
				m = 1;
			}
			// Log.i("Show", "------------->m:" + m);
			if (m == 1) {
				if (!chatHistoryContact.getUsername().equals(
						CacheUtil.getUser(context).getUsername())) {
					chatHistoryContact.setPicture(pictures.get("userIcon"));
					mChatHistoryContacts.add(0, chatHistoryContact);
				}
			}
		} else {
			if (filteredChatHistoryContacts.size() > 0) {
				for (int j = 0; j < filteredChatHistoryContacts.size(); j++) {
					if (chatHistoryContact.getUsername().equals(
							filteredChatHistoryContacts.get(j).getUsername())) {
						filteredChatHistoryContacts.get(j).setMessage(
								chatHistoryContact.getMessage());
						filteredChatHistoryContacts.get(j).setDate(
								chatHistoryContact.getDate());
						m = 0;
						break;
					} else {
						m = 1;
					}
				}
			} else {
				m = 1;
			}
			Log.i("Show", "------------->m:" + m);
			if (m == 1) {
				if (chatHistoryContact.getNickname().indexOf(
						find_contact_filter.getText().toString()) != -1) {
					chatHistoryContact.setPicture(pictures.get("groupIcon"));
					filteredChatHistoryContacts.add(0, chatHistoryContact);
				}
				for (int i = 0; i < mChatHistoryContacts.size(); i++) {
					ChatHistoryContact historyContact = mChatHistoryContacts
							.get(i);
					if (chatHistoryContact.getUsername().equals(
							historyContact.getUsername())) {
						mChatHistoryContacts.remove(historyContact);
						historyContact.setMessage(chatHistoryContact
								.getMessage());
						historyContact.setDate(chatHistoryContact.getDate());
						mChatHistoryContacts.add(0, historyContact);
						m = 0;
						break;
					} else {
						m = 1;
					}
				}
				if (m == 1) {
					if (!chatHistoryContact.getUsername().equals(
							CacheUtil.getUser(context).getUsername())) {
						chatHistoryContact.setPicture(pictures.get("groupIcon"));
						mChatHistoryContacts.add(0, chatHistoryContact);
					}
				}
			}
		}
		// for (ChatHistoryContact cc : mChatHistoryContacts) {
		// Log.i("Show", "---------->after modify id:" + cc.getUserid());
		// }
		mContactshistoryAdapter.notifyDataSetChanged();
	}

	public static void updateLocalChatHistoryContacts(String getUserName,
			String sendTime, String sendContent) {
		for (int i = 0; i < mChatHistoryContacts.size(); i++) {
			ChatHistoryContact chatHistoryContact = mChatHistoryContacts.get(i);
			if (getUserName.equals(chatHistoryContact.getUsername())) {
				mChatHistoryContacts.remove(chatHistoryContact);
				chatHistoryContact.setDate(sendTime);
				chatHistoryContact.setMessage(sendContent);
				mChatHistoryContacts.add(0, chatHistoryContact);
				break;
			}
		}
		mContactshistoryAdapter.notifyDataSetChanged();
	}

	public static void cleanFilter() {
		find_contact_filter.setText("");
		if (filteredChatHistoryContacts != null) {
			filteredChatHistoryContacts.clear();
		}
		if (mChatHistoryContacts != null) {
			mContactshistoryAdapter = new ContactsHistoryAdapter(
					mChatHistoryContacts, screenWidth);
			contactsHistoryPullToRefreshSwipeListView
					.setAdapter(mContactshistoryAdapter);
			mContactshistoryAdapter.notifyDataSetChanged();
		}
	}
}
