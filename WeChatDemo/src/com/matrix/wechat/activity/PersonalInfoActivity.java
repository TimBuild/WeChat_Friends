package com.matrix.wechat.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matrix.wechat.R;
import com.matrix.wechat.dao.BtnCallback;
import com.matrix.wechat.global.Constants;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.Compress;
import com.matrix.wechat.utils.DialogUtil;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;

public class PersonalInfoActivity extends Activity {

	public static int PIC_REQUEST_CODE = 2;

	ImageView iconImage;
	RelativeLayout personalIcon;
	RelativeLayout personalNickName;
	RelativeLayout personalPwd;
	RelativeLayout personalUsername;
	TextView lblUserName;
	public static PersonalInfoActivity instance = null;
	private User user;

	TextView lblNickName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		instance = this;
		user = CacheUtil.getUser(PersonalInfoActivity.this);
		if (user.getPicture() == null || user.getPicture().trim().equals("")) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.test);
			user.setPicture(BitmapUtil.bitmapToBase64(bitmap));
		}
		iconImage = (ImageView) findViewById(R.id.personal_icon);
		setBase64Image(user.getPicture());
		personalIcon = (RelativeLayout) findViewById(R.id.personal_icon_item);
		personalNickName = (RelativeLayout) findViewById(R.id.personal_name_item);
		personalPwd = (RelativeLayout) findViewById(R.id.personal_password_item);
		lblNickName = (TextView) personalNickName
				.findViewById(R.id.personal_nick_name);
		lblNickName.setText(user.getNickname());
		lblUserName = (TextView) findViewById(R.id.personal_user_name);
		lblUserName.setText(user.getUsername());
		personalUsername = (RelativeLayout) findViewById(R.id.personal_username_item);

		personalIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, PIC_REQUEST_CODE);
			}
		});
		personalNickName.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				String oldNickName = user.getNickname();
				View dialogView = LayoutInflater
						.from(PersonalInfoActivity.this).inflate(
								R.layout.change_name_dialog, null);
				final EditText txtNickName = (EditText) dialogView
						.findViewById(R.id.nick_name_change);
				txtNickName.setText(oldNickName);
				DialogUtil.showViewDialog(PersonalInfoActivity.this,
						R.drawable.icon, "Change Nick Name", dialogView, "OK",
						"Cancel", new BtnCallback() {
							@Override
							public void click(DialogInterface dialog, int which) {
								String newNickName = txtNickName.getText()
										.toString();
								if (newNickName.trim().equals("")) {
									Toast.makeText(PersonalInfoActivity.this,
											"Nick name can not be null!",
											Toast.LENGTH_LONG).show();
									return;
								}
								new AsyncTask<String, Void, Boolean>() {
									private String newNickName = null;

									@Override
									protected Boolean doInBackground(
											String... params) {
										newNickName = params[0];
										user.setNickname(newNickName);
										boolean result = updateUser(user);
										if (result) {
											CacheUtil.updateCachedUser(user,
													PersonalInfoActivity.this);
										}
										return result;
									}

									@Override
									protected void onPostExecute(Boolean result) {
										lblNickName.setText(newNickName);
									};

								}.execute(newNickName);
							}
						}, null);
			}
		});

		personalPwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalInfoActivity.this,
						ChangePwdActivity.class);
				startActivity(intent);
			}
		});
	}

	private void setBase64Image(String base64Str) {
		byte[] decodedString = Base64.decode(base64Str, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
				decodedString.length);
		iconImage.setImageBitmap(decodedByte);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PIC_REQUEST_CODE && resultCode == RESULT_OK) {

			Uri uri = data.getData();
			System.out.println(uri);

			try {
				Bitmap bmp = BitmapUtil.getBitmapFromContentProviderUri(this,
						50, 50, uri);

				Constants.OWN_HEAD_IMAGE = bmp;
				String imgStr = BitmapUtil.bitmapToBase64(bmp);
				Log.i("PersonalInfoActivity", imgStr);
				new AsyncTask<String, Void, Boolean>() {

					private String imgStr = null;

					@Override
					protected Boolean doInBackground(String... params) {
						imgStr = params[0];
						user.setPicture(imgStr);
						boolean result = false;
						updateUser(user);
						if (result) {
							CacheUtil.updateCachedUser(user,
									PersonalInfoActivity.this);
						}
						return result;
					}

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						setBase64Image(imgStr);
					}
				}.execute(imgStr);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Change head icon failed",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public boolean updateUser(User user) {
		boolean result = PersonalInfoFactory.getInstance().updateUser(
				user.getUserid(), user.getUsername(), user.getPassword(),
				user.getPicture(), user.getNickname());
		return result;
	}
}
