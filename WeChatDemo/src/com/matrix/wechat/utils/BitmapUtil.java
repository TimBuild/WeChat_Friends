package com.matrix.wechat.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;

public class BitmapUtil {
	public static Bitmap getBitmap(String imgBase64Str) {
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(imgBase64Str, Base64.DEFAULT);
			return BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String bitmaptoString(Bitmap bitmap) {
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 50, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

	/**
	 * s
	 * Developer Sam
	 * 2015年4月29日
	 * @param _bitmap
	 * @return
	 */
	public static final String bitmapToBase64(final Bitmap _bitmap) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        _bitmap.compress(Bitmap.CompressFormat.PNG, 20, baos);
        final byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }
	
	/**
	 * Get bitmap from uri return from contentProvider
	 * 
	 * Developer Sam 2015年4月28日
	 * 
	 * @param context
	 * @param windowManager
	 * @param uri
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Bitmap getBitmapFromContentProviderUri(Context context,
			int width, int height, Uri uri) throws FileNotFoundException  {
		ContentResolver cr = context.getContentResolver();
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeStream(cr.openInputStream(uri), null,
				opt);

		int picWidth = opt.outWidth;
		int picHeight = opt.outHeight;

		opt.inSampleSize = 1;
		// 根据屏的大小和图片大小计算出缩放比例
		if (picWidth > picHeight) {
			if (picWidth > width)
				opt.inSampleSize = picWidth / width;
		}

		else {
			if (picHeight > height)
				opt.inSampleSize = picHeight / height;
		}

		// 这次再真正地生成一个有像素的，经过缩放了的bitmap
		opt.inJustDecodeBounds = false;

		bmp = BitmapFactory.decodeStream(cr.openInputStream(uri), null, opt);
		return bmp;
	}

	/**
	 * copy image to sdCard
	 * 
	 * @param bitmap
	 * @param name
	 */
	public static String copyImageToCard(Bitmap bitmap, String path, String name) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File file = new File(path, name);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (name.endsWith(".png")) {
				bitmap.compress(CompressFormat.PNG, 100, out);
			} else if (name.endsWith(".jpg") || name.endsWith(".JPG")
					|| name.endsWith(".jpeg")) {
				bitmap.compress(CompressFormat.JPEG, 100, out);
			}

			out.flush();
			out.close();
			return path + "/" + name;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 调整正方形图片适应正方形盒子大小(图片为正方形图片)
	 * @param width
	 * @param bitmap
	 * @return
	 */
	public static Bitmap resizeSquareBitmap(int width, Bitmap bitmap) {
		float scale = ((float) width) / ((float) bitmap.getWidth());
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return newBitmap;
	}
	
	/**
	 * 旋转图片(旋转中心为图片的正中心)
	 * @param degree
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotateBitmap(float degree, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return newBitmap;
	}
	
	/**
	 * 放大或者缩小图片
	 * @param scale
	 * @param bitmap
	 * @return
	 */
	public static Bitmap zoomBitmap(float scale, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return newBitmap;
	}
}
