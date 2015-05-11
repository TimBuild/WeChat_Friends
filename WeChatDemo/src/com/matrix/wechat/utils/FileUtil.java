package com.matrix.wechat.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.matrix.wechat.web.FileWorker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

public class FileUtil {
	/* 上传文件至Server的方法 */
	@SuppressLint("NewApi")
	public static String uploadFile(Context context, String file, String serverPath) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(serverPath);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/*
			 * Output to the connection. Default is false, set to true because
			 * post method must write something to the connection
			 */
			con.setDoOutput(true);
			/* Read from the connection. Default is true. */
			con.setDoInput(true);
			/* Post cannot use caches */
			con.setUseCaches(false);
			/* Set the post method. Default is GET */
			con.setRequestMethod("POST");
			/* 设置请求属性 */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置StrictMode 否则HTTPURLConnection连接失败，因为这是在主进程中进行网络连接 */
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			/* 设置DataOutputStream，getOutputStream中默认调用connect() */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream()); // output
																				// to
																				// the
																				// connection
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file\";filename=\"" + "111.jpg" + "\"" + end);
			ds.writeBytes(end);
			
			/**
			 * 上传
			 */
			FileWorker.uploadFile(ds, file, end, twoHyphens, boundary);
			
			/* 关闭DataOutputStream */
			ds.close();
			/* 从返回的输入流读取响应信息 */
			InputStream is = con.getInputStream(); // input from the connection
													// 正式建立HTTP连接
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			/* 显示网页响应内容 */
			con.disconnect();
			return b.toString();
			// Toast.makeText(this, b.toString().trim(),
			// Toast.LENGTH_LONG).show();//Post成功
			// android.util.Log.i("TAG", b.toString());

		} catch (Exception e) {
			/* 显示异常信息 */
			Toast.makeText(context, "Fail:" + e, Toast.LENGTH_LONG).show();// Post失败
			android.util.Log.i("TAG", e + "");
			return null;

		}
	}
	
	public static boolean deleteFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return false;
		}
		file.delete();
		return true;
	}
}
