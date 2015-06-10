package com.matrix.wechat.activity;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.matrix.wechat.R;
import com.matrix.wechat.global.Constants;
import com.matrix.wechat.model.Moment;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.FileUtil;
import com.matrix.wechat.web.service.FriendsZoneService;
import com.matrix.wechat.web.service.factory.FriendsZoneFactory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AddMomentActivity extends Activity{

	private EditText et_moment_content=null;
	private Button bt_send_moment=null;
	private RelativeLayout relback;
	
	public static int PIC_REQUEST_CODE = 2;
	
	public static final String IMAGE_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/imgs/zone";
	
	private static final String TAG = "AddMomentActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_moment);
		
		et_moment_content=(EditText) findViewById(R.id.add_moment_content);
		bt_send_moment=(Button) findViewById(R.id.add_moment_send);
		relback = (RelativeLayout) findViewById(R.id.friend_add_moment_back);
		bt_send_moment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AddMoment().execute("share_text");				
			}
		});
		
		bt_send_moment.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, PIC_REQUEST_CODE);
				return true;
			}
		});
		relback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(AddMomentActivity.this,FriendZoneActivity.class);
//				startActivity(intent);
				finish();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PIC_REQUEST_CODE && resultCode == RESULT_OK) {
			Uri uri = data.getData();

			try {
				WindowManager manager = getWindowManager();
				Display display = manager.getDefaultDisplay();
				Bitmap bitmap = BitmapUtil.getBitmapFromContentProviderUri(
						this, 600,600, uri);

				String path = BitmapUtil.copyImageToCard(bitmap, IMAGE_PATH,
						"1.png");

				if (path == null) {
					throw new FileNotFoundException();
				}

				String filePathOnServer = FileUtil.uploadFile(this, path,
						Constants.UPLOAD_Url);
				if (filePathOnServer == null) {
					throw new IOException();
				}
				new SendPicture().execute(filePathOnServer);
				FileUtil.deleteFile(path);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	private class SendPicture extends AsyncTask<String, Void, Integer>{

		private FriendsZoneService fZoneService;
		@Override
		protected Integer doInBackground(String... params) {
			fZoneService = FriendsZoneFactory.getInstance();
			
			String moment_content = et_moment_content.getText().toString();
			int result = fZoneService.sharePicture(
					CacheUtil.getUser(CacheUtil.context).getUserid(),
					params[0], moment_content);
			
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(result==-1){
				Toast.makeText(AddMomentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(AddMomentActivity.this, "Success"+result, Toast.LENGTH_SHORT).show();
				et_moment_content.setText("");
				Intent intent=new Intent();
				intent.setClass(AddMomentActivity.this, FriendZoneActivity.class);
//				startActivity(intent);
				finish();
			}
		}
		
	}

	private class AddMoment extends AsyncTask<String, Void, Integer>{

		private FriendsZoneService fZoneService;
		
		@Override
		protected Integer doInBackground(String... params) {
			fZoneService = FriendsZoneFactory.getInstance();
			Log.d(TAG, fZoneService.toString());
			
			String moment_content=et_moment_content.getText().toString();
			
			int result;
			result=fZoneService.share(CacheUtil.getUser(CacheUtil.context).getUserid(), moment_content);
			
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {			
			super.onPostExecute(result);
			
			if(result==-1){
				Toast.makeText(AddMomentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(AddMomentActivity.this, "Success"+result, Toast.LENGTH_SHORT).show();
				et_moment_content.setText("");
				Intent intent=new Intent();
				intent.setClass(AddMomentActivity.this, FriendZoneActivity.class);
//				startActivity(intent);
				finish();
			}
		}	
	}
}
