package com.matrix.wechat.web.service;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface ChatMessageSrevice {
	@POST("/postmessage.php")
	@FormUrlEncoded
	boolean postMesage(@Field("userIdFrom") int userIdFrom,
			@Field("userIdTo") int userIdTo, @Field("content") String content,
			@Field("date") String date);
	
}
