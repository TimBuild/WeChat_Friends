package com.matrix.wechat.web;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileWorker {
	public static void uploadFile(DataOutputStream ds, String file,
			String end, String twoHyphens, String boundary) throws IOException {
		/* 取得文件的FileInputStream */
		FileInputStream fStream = new FileInputStream(file);
		/* 设置每次写入8192bytes */
		int bufferSize = 8192;
		byte[] buffer = new byte[bufferSize]; // 8k
		int length = -1;
		/* 从文件读取数据至缓冲区 */
		while ((length = fStream.read(buffer)) != -1) {
			/* 将资料写入DataOutputStream中 */
			ds.write(buffer, 0, length);
		}
		ds.writeBytes(end);
		ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
		/* 关闭流，写入的东西自动生成Http正文 */
		fStream.close();
	}
}
