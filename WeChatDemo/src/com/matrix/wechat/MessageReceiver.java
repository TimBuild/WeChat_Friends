package com.matrix.wechat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.matrix.wechat.R;
import com.matrix.wechat.activity.ChatActivity;
import com.matrix.wechat.global.Constants;
import com.matrix.wechat.logic.ShowAllContactsHistory;
import com.matrix.wechat.model.ChatHistoryContact;
import com.matrix.wechat.model.ChatMsgEntity;
import com.matrix.wechat.model.User;
import com.matrix.wechat.sqlite.dao.OperateContactsHistoryDao;
import com.matrix.wechat.sqlite.dao.impl.OperateContactsHistoryDaoImpl;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.FormatDate;
import com.matrix.wechat.web.service.ContactsService;
import com.matrix.wechat.web.service.factory.ContactsServiceFactory;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MessageReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private static OperateContactsHistoryDao operateContactsHistoryDao = new OperateContactsHistoryDaoImpl();

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		Log.i(TAG, "action" + intent.getAction() + " \nextras: "
				+ printBundle(bundle) + "\nmessage:" + message);

		try {
			JSONObject messageJson = new JSONObject(message);

			// 发送消息的人的信息
			String userId = messageJson.getString("send_userId");
			// username接收的是群的
			String userName = messageJson.getString("send_userName");
			String nickName = messageJson.getString("send_userNickName");

			String groupName="";
			Boolean isGroup = messageJson.getBoolean("isGroup");
			if(isGroup){
				groupName = messageJson.getString("groupName");
			}

//			String picture = messageJson.getString("send_pictures");
			String msgStr = messageJson.getString("msg_content");
			System.out.println("MessageReceiver:userid------------------>"
					+ userId);
			System.out.println("MessageReceiver:username------------------>"
					+ userName);
			System.out.println("MessageReceiver:nickname------------------>"
					+ nickName);
			System.out.println("MessageReceiver:msgStr------------------>"
					+ msgStr);
			System.out.println("MessageReceiver:isGroup------------------>"
					+ isGroup);
			System.out.println("MessageReceiver:groupName------------------>"
					+ groupName);
//			System.out.println("MessageReceiver:pictures------------------>"
//					+ picture);
			operateContactsHistoryDao.cancelDeleteContactsFromList(context,
					Integer.parseInt(userId));
			if ("chatting".equals(Constants.CURRENT_VIEW)) {
				ChatMsgEntity chatMsgEntity = null;
				if (userName.equals(Constants.CURRENT_CHAT_WITH)) {
					chatMsgEntity = new ChatMsgEntity(nickName,
							FormatDate.TimeStamp2Date(
									FormatDate.toUnixTime(new Date()),
									"yyyy-MM-dd HH:mm:ss"), msgStr, true);
//					ChatActivity.mDataArrays.add(chatMsgEntity);
//					ChatActivity.mAdapter.notifyDataSetChanged();
//					ChatActivity.mListView.setSelection(ChatActivity.mListView
//							.getCount() - 1);
				}
				ChatHistoryContact historyContact = new ChatHistoryContact(
						Integer.parseInt(userId), nickName, userName, null,
						msgStr, FormatDate.toUnixTime(new Date()));
				historyContact.setGroup(isGroup);
				historyContact.setGroupName(groupName);
				new UserInfoAsync().execute(historyContact,isGroup,true,chatMsgEntity);
			} else {
				ChatHistoryContact historyContact = new ChatHistoryContact(
						Integer.parseInt(userId), nickName, userName, null,
						msgStr, FormatDate.toUnixTime(new Date()));
				historyContact.setGroup(isGroup);
				historyContact.setGroupName(groupName);
				new UserInfoAsync().execute(historyContact,isGroup,false,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	private class UserInfoAsync extends
			AsyncTask<Object, Void, HashMap<String, String>> {
		private ChatHistoryContact historyContact;
		private boolean isGroup;
		private boolean isChatting;
		private ChatMsgEntity chatMsgEntity;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, String> doInBackground(Object... params) {
			historyContact = (ChatHistoryContact) params[0];
			isGroup = (Boolean) params[1];
			isChatting = (Boolean) params[2];
			chatMsgEntity = (ChatMsgEntity) params[3];
			
			HashMap<String, String> result = new HashMap<String, String>();
				ContactsService contactsService = ContactsServiceFactory
						.getInstance();
				User user = null;
				try {
					user = contactsService.GetUserByID(historyContact.getUserid());
					System.out.println("MessageReceiver--UserInfoAsync---->"+user.toString());
					result.put("userIcon", user.getPicture());
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(isGroup){
//					CacheUtil.context.getResources().getIdentifier(name, defType, defPackage)
					Bitmap bmp = BitmapFactory.decodeResource(CacheUtil.context.getResources(), R.drawable.group_icon);
					result.put("groupIcon", BitmapUtil.bitmaptoString(bmp));;
				}			
			return result;
		}

		@Override
		protected void onPostExecute(HashMap<String, String> result) {
			super.onPostExecute(result);
			
			/**
			 * 2015-4-17 群聊测试
			 */
			if(chatMsgEntity != null){
				chatMsgEntity.setGroup(isGroup);
				if(isChatting){
					if(isGroup){
						chatMsgEntity.setSendNameBy(historyContact.getNickname());
					}
					chatMsgEntity.setPic(result.get("userIcon"));
					ChatActivity.mDataArrays.add(chatMsgEntity);
					ChatActivity.mAdapter.notifyDataSetChanged();
					ChatActivity.mListView.setSelection(ChatActivity.mListView
							.getCount() - 1);
				}
			}
			ShowAllContactsHistory.updateGetChatHistoryContacts(historyContact,
					result);
		}
	}
}
