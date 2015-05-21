package com.matrix.wechat.web.service;

import retrofit.http.GET;
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
}
