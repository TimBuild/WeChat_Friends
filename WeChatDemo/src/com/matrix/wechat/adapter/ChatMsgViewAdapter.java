package com.matrix.wechat.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.wechat.R;
import com.matrix.wechat.activity.ChatActivity;
import com.matrix.wechat.global.Constants;
import com.matrix.wechat.model.ChatMsgEntity;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.ExpressionUtil;
import com.matrix.wechat.utils.NetworkUtil;
import com.matrix.wechat.utils.voice.PlayVoice;
import com.squareup.picasso.Picasso;

@SuppressLint("InflateParams")
public class ChatMsgViewAdapter extends BaseAdapter{

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();

	private List<ChatMsgEntity> coll;

	private Context ctx;

	private LayoutInflater mInflater;

	public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {
		ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		ChatMsgEntity entity = coll.get(position);

		if (entity.getMsgType()) {
			return IMsgViewType.IMVT_COM_MSG;
		} else {
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ChatMsgEntity entity = coll.get(position);
		boolean isComMsg = entity.getMsgType();

		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (isComMsg) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
			}

			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			viewHolder.iv_userhead = (ImageView) convertView
					.findViewById(R.id.iv_userhead);
			viewHolder.isComMsg = isComMsg;
			viewHolder.iv_content = (ImageView) convertView.findViewById(R.id.iv_chatcontent);
			viewHolder.voice_content = (ImageView) convertView.findViewById(R.id.voice_chatcontent);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSendTime.setText(entity.getDate());
//		viewHolder.tvContent.setText(entity.getText());
		
		String zhengze = "f0[0-9]{2}";
		
		SpannableString spannableString = ExpressionUtil.getExpressionString(ctx, entity.getText(), zhengze);
		
		viewHolder.tvContent.setText(spannableString);
		
		/**
		 * Sam 2015-04-13
		 */
		String imageFlag = "[Image][http://";
		String voiceFlag = "[Voice][http://";
		String imgUrl = entity.getText();
		if(imgUrl.indexOf(imageFlag) != -1 ){
			viewHolder.tvContent.setVisibility(View.GONE);
			viewHolder.voice_content.setVisibility(View.GONE);
			viewHolder.iv_content.setVisibility(View.VISIBLE);
			
			String url = imgUrl.substring(imgUrl.lastIndexOf("[") + 1);
			viewHolder.iv_content.setOnClickListener(new ImgOnClickListrner(url));
			Picasso.with(ctx).load(url).into(viewHolder.iv_content);
		} else if(imgUrl.indexOf(voiceFlag) != -1){
			viewHolder.tvContent.setVisibility(View.GONE);
			viewHolder.iv_content.setVisibility(View.GONE);
			viewHolder.voice_content.setVisibility(View.VISIBLE);
			
			String url = imgUrl.substring(imgUrl.lastIndexOf("[") + 1);
			viewHolder.voice_content.setOnClickListener(new VoiceOnClickListrner(url));
		}
		else
		{
			viewHolder.tvContent.setVisibility(View.VISIBLE);
			viewHolder.iv_content.setVisibility(View.GONE);
			viewHolder.voice_content.setVisibility(View.GONE);
		}
		System.out.println("message------------>"+entity.isGroup());
		// 群聊要修改
		if (isComMsg) {
			if(entity.isGroup()){
				viewHolder.tvUserName.setText(entity.getSendNameBy());
				viewHolder.iv_userhead.setImageBitmap(BitmapUtil.getBitmap(entity.getPic()));
			}else{
				viewHolder.tvUserName.setText(entity.getName());
				viewHolder.iv_userhead.setImageBitmap(Constants.CHATING_HEAD_IMAGE);
			}
		} else {
			viewHolder.iv_userhead.setImageBitmap(Constants.OWN_HEAD_IMAGE);
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public ImageView iv_userhead;
		public ImageView iv_content;
		public ImageView voice_content;
		public boolean isComMsg = true;
	}

	class ImgOnClickListrner implements View.OnClickListener {
		String url = null;
		
		public ImgOnClickListrner(String url){
			this.url = url;
		}
		
		@Override
		public void onClick(View v) {
			if(!NetworkUtil.isNetworkConnected(ChatActivity.instance)) {
				Toast.makeText(ChatActivity.instance, "network anomaly", Toast.LENGTH_LONG).show();
				return;
			}
			Log.i("info","ImgOnClickListrner");
			ChatActivity.zoomView.setVisibility(View.VISIBLE);
			Picasso.with(ctx).load(url).into(ChatActivity.imageView);
			ChatActivity.imageView.setScaleType(ScaleType.MATRIX);
		}
	}
	
	class VoiceOnClickListrner implements View.OnClickListener {
		String url = null;
		
		public VoiceOnClickListrner(String url){
			this.url = url;
		}
		
		@Override
		public void onClick(View v) {
			if(!NetworkUtil.isNetworkConnected(ChatActivity.instance)) {
				Toast.makeText(ChatActivity.instance, "network anomaly", Toast.LENGTH_LONG).show();
				return;
			}
			Log.i("info","VoiceOnClickListrner");
			PlayVoice.startPlaying(url);
		}
	}
	
}
