package com.matrix.wechat.utils;

import java.text.SimpleDateFormat;

public class TimeUtil {

	/**
	 * @param time
	 * @return 将毫秒时间转换为mm:ss格式的时间
	 */
	public static String parseTime(String time) {

		Long result = Long.parseLong(time);
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		return sdf.format(result);

	}
}
