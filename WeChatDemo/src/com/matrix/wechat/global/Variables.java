package com.matrix.wechat.global;

import static com.matrix.wechat.global.Constants.*;
import android.content.Context;
import android.content.SharedPreferences;

import com.matrix.wechat.model.User;

public abstract class Variables {
	private static Boolean isLogined;
	private static User user = null;
	
	private static Long userid;
	private static String userName;
	private static String nickName;
	private static String password;
	private static String picture;
	private static Integer status;

	public static Boolean getIsLogined() {
		return isLogined;
	}

	public static void setIsLogined(Boolean isLogined) {
		Variables.isLogined = isLogined;
	}
	
	public static Integer getStatus() {
		return status;
	}

	public static void setStatus(Integer status) {
		Variables.status = status;
		user.setStatus(status);
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		Variables.user = user;
		userid = user.getUserid();
		userName = user.getUsername();
		nickName = user.getNickname();
		password = user.getPassword();
		picture = user.getPicture();
		status = user.getStatus();
	}

	public static final void saveVariables(final Context context) {
		final SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        
        editor.putBoolean(LOGIN_STATUS, isLogined);
        editor.putLong(USER_ID_FLAG, userid);
		editor.putString(USER_NAME, userName);
		editor.putString(USER_NICK_NAME, nickName);
		editor.putString(USER_PASSWORD, password);
		editor.putString(USER_PICTURE, picture);
		editor.putInt(USER_STATUS, status);
        
        editor.apply();
	}
	
	public static final void loadVariables(final Context context) {
		final SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		
		isLogined = sharedPreferences.getBoolean(LOGIN_STATUS, false);
		userid = sharedPreferences.getLong(USER_ID_FLAG, -1);
		userName = sharedPreferences.getString(USER_NAME, "");
		nickName = sharedPreferences.getString(USER_NICK_NAME, "");
		password = sharedPreferences.getString(USER_PASSWORD, "");
		picture = sharedPreferences.getString(USER_PICTURE, "");
		status = sharedPreferences.getInt(USER_STATUS, 0);
		
		user = new User(userid, userName, nickName, picture, password, status);
	}
}
