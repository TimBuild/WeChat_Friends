package com.matrix.wechat.web.service;

import java.util.List;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import com.matrix.wechat.model.GroupHistoryMessage;
import com.matrix.wechat.model.GroupLastMessage;
import com.matrix.wechat.model.GroupMember;

public interface GroupService {
	 
	@FormUrlEncoded
	@POST("/addgroup.php")
	public int AddGroup(@Field("groupname") String groupname);
	
	@FormUrlEncoded
	@POST("/addgroupmember.php")
	public boolean AddGroupMember(@Field("data") String data);
	
	@POST("/postgroupmessage.php")
	@FormUrlEncoded
	public boolean postMessageGroup(@Field("groupid") int groupid,
			@Field("message") String messgae, @Field("userid") int sendbyid,
			@Field("date") String date);
	
	@GET("/getgroupmember.php")
	public List<GroupMember> getGroupMember(@Query("groupid") int groupid);
	
	
	@GET("/getgroupmessage.php")
	public List<GroupHistoryMessage> getGroupMessage(@Query("groupid") int groupid);
	
	@GET("/getchatgroup.php")
	public List<GroupLastMessage> getGroupLastMessgae(@Query("userid") int userid);
	
}
