package com.matrix.wechat.global;

import android.graphics.Bitmap;

public class Constants {
	/**
     * preferences flag
     */
    public static final String SHARED_PREFERENCES               = "preferences";
    public static final String LOGIN_STATUS = "login static";
	public static final String USER_ID_FLAG = "userid";
	public static final String USER_NAME = "username";
	public static final String USER_NICK_NAME = "nickname";
	public static final String USER_PASSWORD = "password";
	public static final String USER_PICTURE = "picture";
	public static final String USER_STATUS = "status";
	
	// API for restful 
	public final static String API_BASE_URL = "http://192.168.1.73:8090";
	public final static String API_CONTACTS =  API_BASE_URL + "/api1/user";
	public final static String API_VOICE = API_BASE_URL + "/api1/upload.php";
	public final static String API_MESSAGE = API_BASE_URL + "/api1/message";
	public final static String API_PUSH = API_BASE_URL + "/api1/message/jpush/examples";
	public final static String API_GROUPS = API_BASE_URL + "/api1/group";
	public final static String UPLOAD_Url = API_BASE_URL + "/api1/upload.php"; // 处理POST请求的页面
	public final static String API_Zone = "http://192.168.1.73:8080/FriendZoomServer/rest/friendzoom";
	

	public final static String API_FIND_FRIEND = "find friend";
	public final static String API_ADD_FRIEND_REQUEST = "post request";
	public final static String API_GET_REQUEST_LIST = "get request list";
	public final static String API_ADD_FRIEND = "add friend";
	public final static String API_GET_USER_BY_USERID = "get user by userid";
	public final static String API_RESPONSE_REQUEST = "response request";

	public final static String ACTIVITY_REQUEST = "request";
	public final static String ACTIVITY_DIALOG = "dialog";

	// Status
	public final static String ON_LINE = "On Line";
	public final static String OFF_LINE = "Off Line";

	// Response request
	public final static String ACCEPT = "ACCEPT";
	public final static String REJECT = "REJECT";
	// save logon user id
	public static Integer USER_ID = null;

	public static Bitmap OWN_HEAD_IMAGE = null;
	public static Bitmap CHATING_HEAD_IMAGE = null;

	// judge how to handle message
	public static String CURRENT_VIEW = "";
	public static String CURRENT_CHAT_WITH = "";
}
