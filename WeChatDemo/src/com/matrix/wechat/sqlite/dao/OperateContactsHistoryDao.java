package com.matrix.wechat.sqlite.dao;

import java.util.List;

import android.content.Context;

/**
 * operate contact history list
 * 
 */
public interface OperateContactsHistoryDao {
	// delete chat with contact
	public boolean deleteContactsFromList(Context context, Integer userId);

	// get all contact id in database
	public List<Integer> getAllDeletedContactId(Context context);

	// remove user from deleted list
	public void cancelDeleteContactsFromList(Context context, Integer userId);
}
