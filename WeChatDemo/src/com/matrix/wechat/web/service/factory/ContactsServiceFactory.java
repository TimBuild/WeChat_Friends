package com.matrix.wechat.web.service.factory;

import retrofit.RestAdapter;

import com.matrix.wechat.web.service.ContactsService;

import static com.matrix.wechat.global.Constants.API_CONTACTS;

public class ContactsServiceFactory {
	private static ContactsService service = null;
	
	public static ContactsService getInstance() {
		if(service == null) {
			RestAdapter restAdapter = new RestAdapter.Builder()
		    .setEndpoint(API_CONTACTS)
		    .build();
			restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
			service = restAdapter.create(ContactsService.class);
		}
		return service;
	}
}
