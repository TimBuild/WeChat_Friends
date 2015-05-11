package com.matrix.wechat.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author JacksonLi
 * @date 2015年2月9日
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public MySQLiteOpenHelper(Context context) {
		super(context, "deletcontacts.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * create table contact deleted
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// String sql =
		// "create table contactdeleted (userid int primary key,nickname varchar(20),picture varchar(1000000),message varchar(10000),date varchar(100)) ";
		String sql = "create table contactdeleted (userid int primary key) ";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
