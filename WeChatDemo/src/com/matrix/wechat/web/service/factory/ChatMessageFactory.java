package com.matrix.wechat.web.service.factory;

import retrofit.RestAdapter;

import com.matrix.wechat.global.Constants;
import com.matrix.wechat.web.service.ChatMessageSrevice;

/**
 * provide a static method to return a object of ChatMessageSrevice
 * 
 */
public class ChatMessageFactory {
	private static ChatMessageSrevice chatMessageSrevice = null;

	public static ChatMessageSrevice getInstance() {
		if (chatMessageSrevice == null) {
			RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
					Constants.API_MESSAGE).build();
			restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
			chatMessageSrevice = restAdapter.create(ChatMessageSrevice.class);
		}
		return chatMessageSrevice;
	}
}
