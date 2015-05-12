package com.matrix.wechat.utils;

import android.content.Context;

public class DensityUtil {
	 /**
     * 根据手机的分辨率从 dp 的单位 转为 px(像素)
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
     
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转为 dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}
