package com.matrix.wechat.sqlite.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.matrix.wechat.sqlite.GetSQLiteOpenHelper;
import com.matrix.wechat.sqlite.dao.OperateContactsHistoryDao;

public class OperateContactsHistoryDaoImpl implements OperateContactsHistoryDao {

	@Override
	public boolean deleteContactsFromList(Context context, Integer userId) {
		long ret = -1;
		SQLiteOpenHelper sqLiteOpenHelper = GetSQLiteOpenHelper
				.getHelperInstance(context);
		SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("userid", userId);
		ret = db.insert("contactdeleted", null, values);
		db.close();
		if (ret > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Integer> getAllDeletedContactId(Context context) {
		List<Integer> contactsDeleted = new ArrayList<Integer>();
		SQLiteOpenHelper sqLiteOpenHelper = GetSQLiteOpenHelper
				.getHelperInstance(context);
		SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("contactdeleted", null, null, null, null,
				null, null, null);
		while (cursor.moveToNext()) {
			int uid = cursor.getInt(cursor.getColumnIndex("userid"));

			contactsDeleted.add(uid);
		}
		db.close();
		return contactsDeleted;
	}

	@Override
	public void cancelDeleteContactsFromList(Context context, Integer userId) {
		SQLiteOpenHelper sqLiteOpenHelper = GetSQLiteOpenHelper
				.getHelperInstance(context);
		SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
		db.execSQL("delete from contactdeleted where userid=?",
				new Object[] { userId });
		db.close();
	}
}
