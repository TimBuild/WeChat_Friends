package com.matrix.wechat.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * get sqlite open helper
 * 
 * @author JacksonLi
 * @date 2015年2月9日
 */
public class GetSQLiteOpenHelper {
	private static SQLiteOpenHelper sqLiteOpenHelper = null;

	public static SQLiteOpenHelper getHelperInstance(Context context) {
		if (sqLiteOpenHelper == null) {
			sqLiteOpenHelper = new MySQLiteOpenHelper(context);
		}
		return sqLiteOpenHelper;
	}
}
