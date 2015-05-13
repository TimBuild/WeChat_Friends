package com.matrix.wechat.web.service;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

import com.matrix.wechat.model.Moment;
import com.matrix.wechat.model.Share;
import com.matrix.wechat.model.ShareWithComment;

public interface FriendsZoneService {

	@GET("/getFreindSharesByOffsetAndNum")
	Share getAllZoneList(@Query("userid") long userid,@Query("offset") Integer offset,@Query("num") Integer num);
}
