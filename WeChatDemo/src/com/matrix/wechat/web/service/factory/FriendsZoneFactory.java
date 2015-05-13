package com.matrix.wechat.web.service.factory;

import retrofit.RestAdapter;

import com.matrix.wechat.global.Constants;
import com.matrix.wechat.web.service.ChatMessageSrevice;
import com.matrix.wechat.web.service.FriendsZoneService;

public class FriendsZoneFactory {
	private static FriendsZoneService friendsZoneService = null;
	
	public static FriendsZoneService getInstance(){
		if (friendsZoneService == null) {
			RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
					Constants.API_Zone).build();
			restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
			friendsZoneService = restAdapter.create(FriendsZoneService.class);
		}
		return friendsZoneService;
	}
}
