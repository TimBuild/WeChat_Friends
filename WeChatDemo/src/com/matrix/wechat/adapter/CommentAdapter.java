package com.matrix.wechat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matrix.wechat.R;
import com.matrix.wechat.model.Comment;
import com.matrix.wechat.utils.ExpressionUtil;
import com.squareup.picasso.Picasso;

public class CommentAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private List<Comment> mList = new ArrayList<Comment>();
	private static String TAG = "CommentAdapter";
	
	public CommentAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);
		this.context=context;
	}

	public void setData(List<Comment> list) {
		this.mList.clear();
		this.mList.addAll(list);
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}
	
	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;	
		
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.item_comment, null);
			holder = new ViewHolder();		
			holder.tv_comment_from = (TextView) convertView.findViewById(R.id.tv_comment_from);
			holder.tv_comment_to = (TextView) convertView.findViewById(R.id.tv_comment_to);
			holder.tv_comment_content=(TextView) convertView.findViewById(R.id.tv_comment_content);
			holder.tv_comment_pic = (ImageView) convertView.findViewById(R.id.tv_comment_image);
						
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.tv_comment_from.setText(mList.get(position).getSharefromname());
		holder.tv_comment_to.setText(mList.get(position).getSharetoname());
//		holder.tv_comment_content.setText(mList.get(position).getContent());
		
		String zhengze = "f0[0-9]{2}";
		
		SpannableString spannableString = ExpressionUtil.getExpressionString(context,mList.get(position).getContent(), zhengze);
		
		holder.tv_comment_content.setText(spannableString);
		String imageFlag = ",[Image],";
		String imgUrl = mList.get(position).getContent();
		
		if(imgUrl.indexOf(imageFlag)!=-1){
			holder.tv_comment_pic.setVisibility(View.VISIBLE);
			String comment = imgUrl.substring(0, imgUrl.indexOf(imageFlag));
			String imagePath = imgUrl.substring(imgUrl.indexOf(imageFlag)+9);
			if(comment.equals("")||comment==null){
				holder.tv_comment_content.setVisibility(View.GONE);
			}else{
				holder.tv_comment_content.setVisibility(View.VISIBLE);
				holder.tv_comment_content.setText(comment);
			}
			Picasso.with(context).load(imagePath).into(holder.tv_comment_pic);
//			Log.d(TAG, "图片："+comment+"<--->"+imagePath);
			
			
		}else{
			holder.tv_comment_pic.setVisibility(View.GONE);
		}
		
		
		return convertView;
	}
	
	public List<Comment> getmList() {
		return mList;
	}

	public void setmList(List<Comment> mList) {
		this.mList = mList;
	}



	static class ViewHolder{
		public TextView tv_comment_from;
		public TextView tv_comment_to;
		public TextView tv_comment_content;	
		public ImageView tv_comment_pic;

	}



}
