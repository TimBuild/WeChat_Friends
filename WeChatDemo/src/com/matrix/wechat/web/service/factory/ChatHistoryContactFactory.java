package com.matrix.wechat.web.service.factory;

import retrofit.RestAdapter;

import com.matrix.wechat.global.Constants;
import com.matrix.wechat.web.service.ChatHistoryContactService;

/**
 * provide a static method to return a object of ChatHistoryContactService
 */
public class ChatHistoryContactFactory {
	private static ChatHistoryContactService chatHistoryContactService = null;

	public static ChatHistoryContactService getInstance() {
		if (chatHistoryContactService == null) {
			RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
					Constants.API_MESSAGE).build();
			restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
			chatHistoryContactService = restAdapter
					.create(ChatHistoryContactService.class);
		}
		return chatHistoryContactService;
	}
}
