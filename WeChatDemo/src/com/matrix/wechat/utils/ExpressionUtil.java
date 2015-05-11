package com.matrix.wechat.utils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.matrix.wechat.R;

public class ExpressionUtil {
	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * @param context
	 * @param spanableString
	 * @param pattern
	 * @param start
	 * @throws NoSuchFieldException
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void dealExpression(Context context,SpannableString spanableString,Pattern pattern ,int start) throws NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException{
		Matcher matcher = pattern.matcher(spanableString);
		while(matcher.find()){
			String key = matcher.group();
			if(matcher.start()<start){
				continue;
			}
			Field field = R.drawable.class.getDeclaredField(key);
			int resId = Integer.parseInt(field.get(null).toString());//通过上面匹配得到的字符串来生成图片资源id
			if(resId!=0){
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
				
				ImageSpan imageSpan = new ImageSpan(context,bitmap);//通过图片资源id得到bitmap
				int end = matcher.start()+key.length();
				spanableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				if(end<spanableString.length()){
					dealExpression(context, spanableString, pattern, end);
				}
				break;
			}
		}
	}
	
	/**
	 * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
	 * @param context
	 * @param str
	 * @param zhengze
	 * @return
	 */
	public static SpannableString getExpressionString(Context context,String str,String zhengze){
		SpannableString spannableString = new SpannableString(str);
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten, 0);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return spannableString;
		
	}
}
