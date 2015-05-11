package com.matrix.wechat.web.service;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import com.matrix.wechat.model.User;

public interface PersonalInfoService {
	
	@FormUrlEncoded
	@POST("/updateuser.php")
	boolean updateUser(@Field("userid") long id, @Field("username") String username, 
			@Field("password") String pwd, @Field("picture") String picture, @Field("nickname") String nickname);
	
	@GET("/getuserbyusername.php")
	User getUserByUsername(@Query("username") String username) ;
	
	@FormUrlEncoded
	@POST("/userlog.php")
	boolean logout(@Field("userid") long userid, @Field("status") String status);
	
	
	@FormUrlEncoded
	@POST("/createuser.php")
	boolean createUser(@Field("username") String username, 
			@Field("password") String pwd);
	
}
