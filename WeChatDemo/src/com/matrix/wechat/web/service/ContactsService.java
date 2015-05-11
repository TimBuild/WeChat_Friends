package com.matrix.wechat.web.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import com.matrix.wechat.model.FriendRequest;
import com.matrix.wechat.model.User;

public interface ContactsService {
	@GET("/GetContactList.php")
	List<User> GetContactList(@Query("userid") long userid) throws TimeoutException, IOException, URISyntaxException;
	
	@GET("/GetUserByID.php")
	User GetUserByID(@Query("userid") long userid) throws TimeoutException, IOException, URISyntaxException;
	
	@GET("/GetUserByUsername.php")
	User GetUserByUsername(@Query("username") String username) throws TimeoutException, IOException, URISyntaxException;
	
	@FormUrlEncoded
	@POST("/PostRequest.php")
	boolean PostRequest(@Field("userIdFrom") long userIdFrom, @Field("userIdTo") long userIdTo) throws TimeoutException, IOException, URISyntaxException;
	
	@GET("/GetRequestList.php")
	List<FriendRequest> GetRequestList(@Query("userid") long userid) throws TimeoutException, IOException, URISyntaxException;
	
	@FormUrlEncoded
	@POST("/ResponseRequest.php")
	boolean ResponseRequest(@Field("requestID") long requestID, @Field("status") String status) throws TimeoutException, IOException, URISyntaxException;
}
