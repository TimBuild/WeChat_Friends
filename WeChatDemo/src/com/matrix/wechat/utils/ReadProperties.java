package com.matrix.wechat.utils;

import java.util.ResourceBundle;

/**
 * read properties file
 * 
 * @date 2015年2月2日
 */
public class ReadProperties {
	/**
	 * a static function use to read the statement by file name and key
	 * 
	 * @param sourceName
	 *            file's name
	 * @param key
	 *            statement key
	 * @return
	 */
	public static String read(String sourceName, String key) {
		try {
			return ResourceBundle.getBundle("com.matrix.properties." + sourceName)
					.getString(key);
		} catch (Exception e) {
			return null;
		}
	}
}
