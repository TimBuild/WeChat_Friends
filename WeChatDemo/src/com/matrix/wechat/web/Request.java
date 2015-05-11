package com.matrix.wechat.web;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.matrix.wechat.activity.FriendRequestActivity;
import com.matrix.wechat.model.FriendRequest;
import com.matrix.wechat.model.User;

import static com.matrix.wechat.logic.ContactsWorker.*;
import static com.matrix.wechat.global.Constants.*;

/**
 * Created by MGC14 on 2014/12/4.
 */
public class Request extends AsyncTask<Object, Integer, HashMap<String,Object>> {
    private Activity activity = null;
    private String api = null;
    private boolean isProgressNeeded = false;

    private ProgressDialog mpDialog = null;

    public Request(Activity activity, String api, boolean isProgressNeeded) {
        this.activity = activity;
        this.api = api;
        this.isProgressNeeded = isProgressNeeded;
    }

    @Override
    protected HashMap<String, Object> doInBackground(Object... params) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
     
        if(api.equals(API_CONTACTS)){
            List<User> contacts = getContacts(activity);
            resultMap.put(API_CONTACTS, contacts);
        } else if(api.equals(API_FIND_FRIEND)){
            List<User> friends = getNewFriends((String) params[0]);
            resultMap.put(API_FIND_FRIEND, friends);
        } else if(api.equals(API_ADD_FRIEND_REQUEST)){
        	boolean result = postNewFriendRequest((Long)params[0], (Long)params[1]);
        	resultMap.put(API_ADD_FRIEND_REQUEST, result);
        } else if(api.equals(API_GET_REQUEST_LIST)){
        	List<FriendRequest> result = getRequestList((Long) params[0]);
        	resultMap.put(API_GET_REQUEST_LIST, result);
        } else if(api.equals(API_GET_USER_BY_USERID)){
        	User result = gerUserByUserid((Long) params[0]);
        	if(activity instanceof FriendRequestActivity)
        		resultMap.put(ACTIVITY_REQUEST, params[1]);
        	resultMap.put(API_GET_USER_BY_USERID, result);
        } else if(api.equals(API_RESPONSE_REQUEST)){
        	boolean result = responseRequest((Long)params[0], (String)params[1]);
        	resultMap.put(ACTIVITY_DIALOG, params[2]);
        	resultMap.put(API_RESPONSE_REQUEST, result);
        }

        return resultMap;
    }

    @Override
    protected void onPreExecute() {
        if(!isProgressNeeded) return;

        mpDialog = ProgressDialog.show(activity, null, "loading...", true);
    }

    @Override
    protected void onPostExecute(HashMap<String, Object> resultMap) {
        if(api.equals(API_CONTACTS)){
            notifyContacts(resultMap);
        } else if(api.equals(API_FIND_FRIEND)) {
        	notifyNewFriends(resultMap);
        } else if(api.equals(API_ADD_FRIEND_REQUEST)){
        	notifyNewFriendRequest(resultMap);
        } else if(api.equals(API_GET_REQUEST_LIST)){
        	notifyRequestList(resultMap);
        } else if(api.equals(API_GET_USER_BY_USERID)){
        	notifyGetUserById(activity, resultMap);
        } else if(api.equals(API_RESPONSE_REQUEST)){
        	notifyResponseRequests(resultMap);
        }

        if(isProgressNeeded){
            mpDialog.dismiss();
        }
    }
}
