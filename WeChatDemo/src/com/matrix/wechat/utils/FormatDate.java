package com.matrix.wechat.utils;

import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class FormatDate {
	public static String TimeStamp2Date(String timestampString, String formats) {

		Long timestamp = Long.parseLong(timestampString) * 1000;

		String date = new java.text.SimpleDateFormat(formats)
				.format(new java.util.Date(timestamp));
		return date;
	}

	public static String toUnixTime(Date date) {

		return date.getTime() / 1000 + "";
	}
}
