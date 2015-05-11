package com.matrix.wechat.web.service;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

import com.matrix.wechat.model.ChatHistoryContact;
import com.matrix.wechat.model.ChatHistoryMessage;

public interface ChatHistoryContactService {
	@GET("/getchatwith.php")
	List<ChatHistoryContact> getChatHistoryContacts(@Query("userid") int userid);

	@GET("/getmessagelistbyuser.php")
	List<ChatHistoryMessage> getChatHistoryMessages(
			@Query("userIdFrom") int userIdFrom, @Query("userIdTo") int userIdTo);
}
