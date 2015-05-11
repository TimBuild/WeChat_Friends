package com.matrix.wechat.web.service.factory;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

import com.matrix.wechat.global.Constants;
import com.matrix.wechat.web.service.PersonalInfoService;
import com.squareup.okhttp.OkHttpClient;

public class PersonalInfoFactory {
	
	private static PersonalInfoService service = null;
	
	public static PersonalInfoService getInstance() {
		if(service == null) {
			RestAdapter restAdapter = new RestAdapter.Builder()
		    .setEndpoint(Constants.API_CONTACTS)
		    .setClient(new OkClient(new OkHttpClient()))
		    .build();
			restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
			service = restAdapter.create(PersonalInfoService.class);
		}
		return service;
	}

}
