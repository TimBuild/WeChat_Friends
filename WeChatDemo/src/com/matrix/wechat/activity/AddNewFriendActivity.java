package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.FriendsListAdapter;
import com.matrix.wechat.model.User;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.NetworkUtil;
import com.matrix.wechat.web.Request;
import static com.matrix.wechat.global.Constants.*;



public class AddNewFriendActivity extends Activity {
	EditText userName_ET;
	Button search_BTN;
	ListView friends_LV;

	Button deal;

	public static Activity instance = null;

	public static List<User> friendList = null;
	public static FriendsListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friend);

		instance = this;

		userName_ET = (EditText) findViewById(R.id.find_new_friend_filter);
		search_BTN = (Button) findViewById(R.id.btn_search_new_friend);

		friendList = new ArrayList<User>();
		adapter = new FriendsListAdapter(this, friendList);

		friends_LV = (ListView) findViewById(R.id.new_friends_LV);
		friends_LV.setAdapter(adapter);

		friends_LV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				User curUser = CacheUtil.getUser(instance);
				long from = curUser.getUserid();
				long to = adapter.friends.get(arg2).getUserid();

				// showFriendInfoDialog(instance, curUser, from, to);

				new Request(instance, API_GET_USER_BY_USERID, true).execute(to);
			}
		});
		search_BTN.setOnClickListener(new SearchBtnOnClickListrner());

		deal = (Button) findViewById(R.id.btn_deal_requests);
		deal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddNewFriendActivity.this,
						FriendRequestActivity.class);
				startActivity(intent);
			}
		});

	}

	class SearchBtnOnClickListrner implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if(!NetworkUtil.isNetworkConnected(instance)) {
				Toast.makeText(instance, "network anomaly", Toast.LENGTH_LONG).show();
				return;
			}
			String userName = userName_ET.getText().toString().trim();

			new Request(instance, API_FIND_FRIEND, true).execute(userName);
		}
	}

}
