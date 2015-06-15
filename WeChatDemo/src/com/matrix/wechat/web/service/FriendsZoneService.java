package com.matrix.wechat.web.service;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import com.matrix.wechat.model.Share;
import com.matrix.wechat.model.User;

public interface FriendsZoneService {

	@GET("/getFreindSharesByOffsetAndNum")
	Share getAllZoneList(@Query("userid") long userid,@Query("offset") Integer offset,@Query("num") Integer num);
	
	@GET("/share")
	Integer share(@Query("userid") long userid,@Query("content") String content);
	
	@GET("/getSharesByUserid")
	Share getSharesByUserid(@Query("userid") long userid,@Query("offset") Integer offset,@Query("num") Integer num);

	@GET("/comment")
	Integer comment(@Query("shareid") int shareid,@Query("fromid") long fromid,@Query("toid") long toid,@Query("content") String content);
	
	@POST("/deleteComment")
	@FormUrlEncoded
	boolean deleteComment(@Field("fromid") int fromid,@Field("commentid") int commentid);
	

	@POST("/deleteShare")
	@FormUrlEncoded
	boolean deleteShare(@Field("sharefrom") int sharefrom,@Field("shareid") int shareid);
	
	@POST("/shareImage")
	@FormUrlEncoded
	Integer sharePicture(@Field("userid") long userid,@Field("image") String image,@Field("content") String content);

	
	@POST("/shareVoice")
	@FormUrlEncoded
	Integer shareVoice(@Field("userid") long userid,@Field("voice") String voice,@Field("content") String content);
	
}
