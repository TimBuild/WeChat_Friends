package com.matrix.wechat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.matrix.wechat.R;

public class ChatContactInfoActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_info);
	}

	public void btn_back(View v) { // 标题栏 返回按钮
		this.finish();
	}

	public void btn_back_send(View v) { // 标题栏 返回按钮
		this.finish();
	}

	public void head_xiaohei(View v) { // 头像按钮
		Intent intent = new Intent();
		intent.setClass(ChatContactInfoActivity.this, ChatContactHeadActivity.class);
		startActivity(intent);
	}

}
