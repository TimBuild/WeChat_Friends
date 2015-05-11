package com.matrix.wechat.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.matrix.wechat.R;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.web.service.PersonalInfoService;
import com.matrix.wechat.web.service.factory.PersonalInfoFactory;

public class ChangePwdActivity extends Activity {
	
	private EditText txtNewPwd;
	private EditText txtOldPwd;
	private EditText txtRepeatPwd;
	private Button btnChangePwd;
	private TextView lblAlert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
		
		txtNewPwd = (EditText) findViewById(R.id.new_pwd);
		txtOldPwd = (EditText) findViewById(R.id.old_pwd);
		txtRepeatPwd = (EditText) findViewById(R.id.pwd_repeat);
		btnChangePwd = (Button) findViewById(R.id.btn_change_pwd);
		lblAlert = (TextView) findViewById(R.id.alert_info);
		
		btnChangePwd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String oldPwd = txtOldPwd.getText().toString();
				String newPwd = txtNewPwd.getText().toString();
				final String repeatPwd = txtRepeatPwd.getText().toString();
				if(oldPwd.equals("") || newPwd.equals("") || repeatPwd.equals("")) {
					lblAlert.setText("Please input old password, new password and repeat password!");
					return;
				}
				if(!oldPwd.equals(CacheUtil.getUser(ChangePwdActivity.this).getPassword())) {
					lblAlert.setText("Please input correct password");
					return;
				}
				if(!newPwd.equals(repeatPwd)) {
					lblAlert.setText("Repeat password is not right");
					return;
				}
				new AsyncTask<String, Void, Boolean>() {

					@Override
					protected Boolean doInBackground(String... params) {
						PersonalInfoService service = PersonalInfoFactory.getInstance();
						User user = CacheUtil.getUser(ChangePwdActivity.this);
						user.setPassword(repeatPwd);
						boolean result = service.updateUser(user.getUserid(), user.getUsername(), user.getPassword(), user.getPicture(), user.getNickname());
						CacheUtil.updateCachedUser(user, ChangePwdActivity.this);
						return result;
					}
					
					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
						if(result) {
							finish();
							PersonalInfoActivity.instance.finish();
							MainWeixin.instance.finish();
						}
					}
				}.execute();
			}
		});
	}
	
}
