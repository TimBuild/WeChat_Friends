package com.matrix.wechat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * @author MGC10 工具类，获得当前的时间
 * 
 */
public class DateUtil {
	public static String getCurrentTime(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(System.currentTimeMillis());
		return currentTime;
	}

	/**
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		String format = "yyyy-MM-dd HH:mm:ss";
		return getCurrentTime(format);
	}
	
	/**
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getParseTime(String date){
		String year = date.substring(0, 10);
		String daytime = date.substring(11,19);
		String time = year+" "+daytime;
		
		return time;
	}
	//2015-05-12T11:19:26+08:00
	/**
	 * @param date
	 * @return MM-dd
	 */
	public static String getDateTime(String date){
		
		String day = date.substring(5, 10);
		return day;
	}
	
	

}
