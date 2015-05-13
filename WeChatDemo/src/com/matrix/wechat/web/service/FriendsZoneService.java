package com.matrix.wechat.web.service;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

import com.matrix.wechat.model.Moment;
import com.matrix.wechat.model.Share;
import com.matrix.wechat.model.ShareWithComment;

public interface FriendsZoneService {

	@GET("/getAllFreindShares")
	Share getAllZoneList(@Query("userid") long userid);
}
