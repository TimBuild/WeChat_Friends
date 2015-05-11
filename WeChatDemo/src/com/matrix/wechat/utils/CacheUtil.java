package com.matrix.wechat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.matrix.wechat.model.User;

public class CacheUtil {
	
	public static Context context;
	
	public static String USER_PREFERENCES_NAME = "user";
	public static String USER_ID = "userid";
	public static String USER_NAME = "username";
	public static String USER_NICK_NAME = "nickname";
	public static String USER_PASSWORD = "password";
	public static String USER_PICTURE = "picture";
	public static String USER_STATUS = "status";
	
	private static User cachedUser = null;
	
	private static User getUserInCache(Context context) {
		SharedPreferences sp = context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
		long userid = sp.getLong(USER_ID, -1);
		String username = sp.getString(USER_NAME, "");
		String nickname = sp.getString(USER_NICK_NAME, "");
		String password = sp.getString(USER_PASSWORD, "");
		String picture = sp.getString(USER_PICTURE, "");
		int status = sp.getInt(USER_STATUS, 0);
		return new User(userid, username, nickname, picture, password, status);
	}
	
	public static void updateCachedUser(User user, Context context) {
		SharedPreferences sp = context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong(USER_ID, user.getUserid());
		editor.putString(USER_NAME, user.getUsername());
		editor.putString(USER_NICK_NAME, user.getNickname());
		editor.putString(USER_PASSWORD, user.getPassword());
		editor.putString(USER_PICTURE, user.getPicture());
		editor.putInt(USER_STATUS, user.getStatus());
		editor.commit();
	}
	
	public static User getUser(Context context) {
		if(cachedUser == null) {
			cachedUser = getUserInCache(context);
		}
		return cachedUser;
	}
	
	public static void clean() {
		cachedUser = null;
	}

}
