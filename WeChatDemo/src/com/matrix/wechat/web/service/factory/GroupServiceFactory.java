package com.matrix.wechat.web.service.factory;

import retrofit.RestAdapter;

import com.matrix.wechat.web.service.GroupService;

import static com.matrix.wechat.global.Constants.*;

public class GroupServiceFactory {

	private static GroupService service = null;
	
	public static GroupService getInstance(){
		if(service == null) {
			RestAdapter restAdapter = new RestAdapter.Builder()
		    .setEndpoint(API_GROUPS)
		    .build();
			restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
			service = restAdapter.create(GroupService.class);
		}
		return service;
	}
}
