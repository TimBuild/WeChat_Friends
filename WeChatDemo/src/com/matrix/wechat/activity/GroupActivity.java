package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.GroupBaseAdapter;
import com.matrix.wechat.global.Constants;
import com.matrix.wechat.logic.ContactsViewWorker;
import com.matrix.wechat.logic.ShowAllContactsHistory;
import com.matrix.wechat.model.ChatHistoryContact;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.FormatDate;
import com.matrix.wechat.web.service.factory.GroupServiceFactory;

public class GroupActivity extends Activity {

	private ListView requests_LV;
	public static GroupBaseAdapter adapter;
	public static List<User> users = null;
	
	
	private static List<ChatHistoryContact> mChatHistoryContacts = null;
	private ShowAllContactsHistory showAllContact;

	private Button showBtn;
	
	private static final String ADD_GROUP = "ADD_GROUP";
	private static final String ADD_GROUP_MEMBER = "ADD_GROUP_MEMBER";
	

	List<Integer> listItemID = new ArrayList<Integer>();
	private MyHandler handler = new MyHandler();

	public String group_name = "group test";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_request_group);
		

		users = ContactsViewWorker.contacts;
		adapter = new GroupBaseAdapter(this, users);

		requests_LV = (ListView) findViewById(R.id.requests_LV_GROUP);

		requests_LV.setAdapter(adapter);

		showBtn = (Button) findViewById(R.id.group_show);
		showBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(0);
				GroupActivity.this.finish();
			}
		});

	}
	
	
	private class MyHandler extends Handler{
		public MyHandler() {

		}

		public MyHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				try {
					Object obj1 = new Request(ADD_GROUP)
							.execute(group_name).get();
					if (obj1 != null) {
						int group_id = (Integer) obj1;
						Message message = Message.obtain();
						message.what = 1;
						message.obj = group_id;
						handler.sendMessage(message);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1:
				int group_id = (Integer) msg.obj;

				StringBuilder data_group_buffer = new StringBuilder();
				data_group_buffer.append("[{\"groupid\":" + group_id
						+ "},{\"userlist\":[");

				listItemID.clear();

				for (int i = 0; i < adapter.mChecked.size(); i++) {
					if (adapter.mChecked.get(i)) {
						listItemID.add(i);
					}
				}

				if (listItemID.size() == 0) {
					AlertDialog.Builder builder1 = new AlertDialog.Builder(
							GroupActivity.this);
					builder1.setMessage("没有选中任何记录");
					builder1.show();
				} else {
					StringBuilder sb = new StringBuilder();

					for (int i = 0; i < listItemID.size(); i++) {
						/*sb.append("ItemID="
								+ users.get(listItemID.get(i)).getUserid()
								+ "\n"); // 取出所有的被点击的nickname
*/						data_group_buffer.append("{\"userid\":"
								+ users.get(listItemID.get(i)).getUserid()
								+ "}");

							data_group_buffer.append(",");

					}
					data_group_buffer.append("{\"userid\":"
							+ CacheUtil.getUser(GroupActivity.this).getUserid()
							+ "}");
					data_group_buffer.append("]}]");
					System.out.println("测试数据：" + data_group_buffer.toString());
					/*
					 * AlertDialog.Builder builder2 = new AlertDialog.Builder(
					 * GroupActivity.this); builder2.setMessage(sb.toString());
					 * builder2.show();
					 */
					
					
				}
				try {
					Object obj1 = new Request(ADD_GROUP_MEMBER).execute(data_group_buffer.toString()).get();
					if(obj1!=null){
						boolean group_flag = (Boolean) obj1;
						
						if(group_flag){
						Map<String, Object> result = new HashMap<String, Object>();
						
						Message message = Message.obtain();
						message.what = 2;
						result.put("group_flag", group_flag);
						result.put("group_id", group_id);
						message.obj = result;
						handler.sendMessage(message);
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case 2:
				Map<String, Object> map =  (Map<String, Object>) msg.obj;
				boolean group_flag = (Boolean) map.get("group_flag");
				int group = (Integer) map.get("group_id");
				if (group_flag) {

					showAllContact = new ShowAllContactsHistory();
					ChatHistoryContact chatHistoryContact = new ChatHistoryContact();
					// User contact = contacts.get(arg2);
					chatHistoryContact.setDate(FormatDate.toUnixTime(new Date()));
					chatHistoryContact.setMessage("");
					
					chatHistoryContact.setNickname(group_name);
					//群头像
					Bitmap bmp = BitmapFactory.decodeResource(CacheUtil.context.getResources(), R.drawable.group_icon);
					chatHistoryContact
							.setPicture(BitmapUtil.bitmaptoString(bmp));
					chatHistoryContact.setUserid(group);
					chatHistoryContact.setUsername(group + "");
					chatHistoryContact.setGroup(true);
					chatHistoryContact.setGroupName(group_name);
					Constants.CURRENT_CHAT_WITH = group+"";
					showAllContact.openGroupChatDialog(chatHistoryContact);
				}
				
				break;
			default:
				break;
			}
		}
	}
	
	private class Request extends AsyncTask<Object, Integer, Object> {
		
		private String api;
		public Request(String api){
			this.api = api;
		}

		@Override
		protected Object doInBackground(Object... params) {
			if (api.equals(ADD_GROUP)) {
//				System.out.println("doInBackground:......"+(String)params[0]);
				int group_id = GroupServiceFactory.getInstance()
						.AddGroup((String) params[0]);
				return group_id;
			}

			else if (api.equals(ADD_GROUP_MEMBER)) {
				boolean group_flag = GroupServiceFactory.getInstance()
						.AddGroupMember((String) params[0]);
				System.out.println("ADD_GROUP_MEMBER  : "+ group_flag);
				return group_flag;
			}

			return null;
		}

		
		
	}

}
