package com.matrix.wechat.web.service;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface PushMessageService {
	@GET("/pushexample.php")
	void pushMessage(@Query("username") String username,
			@Query("text") String text, Callback<String> callback);
	
	@FormUrlEncoded
	@POST("/pushexampleAll.php")
	void pushMessageGroup(@Field("username") String usernames,
			@Field("text") String text, Callback<String> callback);
}
