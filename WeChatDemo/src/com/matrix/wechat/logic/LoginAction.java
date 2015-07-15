package com.matrix.wechat.logic;

import static com.matrix.wechat.global.Variables.getIsLogined;
import static com.matrix.wechat.global.Variables.getUser;
import static com.matrix.wechat.global.Variables.saveVariables;
import static com.matrix.wechat.global.Variables.setStatus;
import android.os.AsyncTask;

import com.matrix.wechat.activity.LoginActivity;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;

public class LoginAction {
	public static void logout(){
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if(!getIsLogined()){
					PersonalInfoFactory.getInstance().logout(
							CacheUtil.getUser(CacheUtil.context).getUserid(), "OUT");
					setStatus(0);
					saveVariables(CacheUtil.context);
					CacheUtil.updateCachedUser(getUser(), CacheUtil.context);
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
			}
		}.execute();
	}
}
