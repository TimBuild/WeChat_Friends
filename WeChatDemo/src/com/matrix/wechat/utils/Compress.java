package com.matrix.wechat.utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;

public class Compress {
	
	public static Bitmap copressImage(String imgPath){
		Bitmap bmap;

	    File picture = new File(imgPath);

	    Options bitmapFactoryOptions = new BitmapFactory.Options();

	    //set the picture adjustable
	    bitmapFactoryOptions.inJustDecodeBounds = true;
	    bitmapFactoryOptions.inSampleSize = 2;

	    bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
	         bitmapFactoryOptions);

	    float imagew = 150;
	    float imageh = 150;

	    int yRatio = (int) Math.ceil(bitmapFactoryOptions.outHeight
	            / imageh);

	    int xRatio = (int) Math
	            .ceil(bitmapFactoryOptions.outWidth / imagew);

	    if (yRatio > 1 || xRatio > 1) {
	        if (yRatio > xRatio) {
	            bitmapFactoryOptions.inSampleSize = yRatio;
	        } else {
	            bitmapFactoryOptions.inSampleSize = xRatio;
	        }
	    } 

	    bitmapFactoryOptions.inJustDecodeBounds = false;//false --- allowing the caller to query the bitmap without having to allocate the memory for its pixels.
	    bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
	            bitmapFactoryOptions);
	    if(bmap != null){               
	        return bmap;
	    }

	    return null;

	}
	
	@SuppressLint("NewApi")
	public static Bitmap revitionImageSize(String path, int size) throws IOException { 
		 // get picture
        InputStream temp = new FileInputStream(new File(path));  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        //the param means:don't distribute memory to bitmap,only record some picture info(like picture size)
        options.inJustDecodeBounds = true;  
        //get options content
        BitmapFactory.decodeStream(temp, null, options);  
        temp.close();  
        // generate compress picture
        int i = 0;  
        Bitmap bitmap = null;
        
        
        while (true) {  
            // make both height and width satisfy according the settings
            if ((options.outWidth >> i <= size)  
                    && (options.outHeight >> i <= size)) {  
            	//reget the stream
                temp = new FileInputStream(new File(path)); 
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;  
  
                bitmap = BitmapFactory.decodeStream(temp, null, options); 
                
                break;
            }  
            i += 1;  
        }  
        return bitmap;  
	}
	
	@SuppressLint("NewApi")
	public static Bitmap revitionImageSize2(String path, int size, int maxKb) throws IOException { 

		InputStream temp = new FileInputStream(new File(path));  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        BitmapFactory.decodeStream(temp, null, options);  
        temp.close();  
  
        int i = 0;  
        Bitmap bitmap = null;
        byte[] bytes=null;
        
        while (true) {  
            if ((options.outWidth >> i <= size) && (options.outHeight >> i <= size)) {  
                temp = new FileInputStream(new File(path)); 
                
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;  
  
                bitmap = BitmapFactory.decodeStream(temp, null, options);
                bytes = compress(bitmap, maxKb);
                
                break;
            }  
            i += 1;  
        }  
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);  
	}
	
	@SuppressLint("NewApi")
	public static byte[] compress(Bitmap image, int maxKB) {
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //compress the quality of image,100 means don't compress,and put the compressed data to baos
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;  
        //if the size>100kb,continue to compress
        while ( baos.toByteArray().length / 1024>maxKB) {      
            baos.reset();//reset baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//compress options%，put the compressed data to baos
            options -= 10;//reduce 10 every time
        }  
        return baos.toByteArray();  
    } 
	
	@SuppressLint("NewApi")
	public static ByteArrayOutputStream compressToOutStream(Bitmap image, int maxKB) {  
		
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
      //compress the quality of image,100 means don't compress,and put the compressed data to baos
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        
        int options = 100;  
        int oldLength = baos.toByteArray().length;
        if (maxKB*1024 < oldLength) {
        	options = maxKB*1024/oldLength *100;
        }
        
        image.compress(Bitmap.CompressFormat.JPEG, options, baos);//compress options%，put the compressed data to baos
        return baos;  
    } 
	
	@SuppressLint("NewApi")
	public static Bitmap compressBitmap(Bitmap bitmap, int maxKb) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); 
		byte[] b = baos.toByteArray(); 
		double mid = b.length/1024; 
		
		 if (mid > maxKb) { 
			 //get bitmap size
             double i = mid / maxKb; 
             bitmap = zoomImage(bitmap, bitmap.getWidth() / Math.abs(i), bitmap.getHeight() / Math.abs(i)); 
		 }
		
		return bitmap;
		
	}
	
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth, 
	        double newHeight) { 
		// get width and height of the picture
		float width = bgimage.getWidth(); 
		float height = bgimage.getHeight(); 
		// create matrix object
		Matrix matrix = new Matrix(); 
		// calculate compress scale
		float scaleWidth = ((float) newWidth) / width; 
		float scaleHeight = ((float) newHeight) / height; 
		// compress picture action
		matrix.postScale(scaleWidth, scaleHeight); 
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, 
		                (int) height, matrix, true); 
		return bitmap; 
	} 
	
	@SuppressLint("NewApi")
	public static Bitmap zoom(Bitmap bitmap, float ratio) { 
		
        if (ratio < 0f) {return bitmap;} 
        
        if (bitmap.getByteCount() > 100*1024) {
        	
        	ratio = 200.0f*1024/bitmap.getByteCount();
        	
        	Matrix matrix = new Matrix();  
            matrix.postScale(0.3f, 0.7f);  
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        
        
        return bitmap;  
    }  
	
	
}
