package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.matrix.wechat.R;
import com.matrix.wechat.global.Constants;
import com.matrix.wechat.logic.ContactsViewWorker;
import com.matrix.wechat.logic.ShowAllContactsHistory;
import com.matrix.wechat.utils.BitmapUtil;
import com.matrix.wechat.utils.CacheUtil;
import com.matrix.wechat.utils.JPushUtil;
import com.matrix.wechat.utils.NetworkUtil;

@SuppressLint({ "InflateParams", "HandlerLeak" })
public class MainWeixin extends Activity {
	protected static final String TAG = "MainWeixin";
	public static MainWeixin instance = null;

	private ViewPager mTabPager;
	private ImageView mTabImg;// 动画图片
	private ImageView mTab1, mTab2, mTab4;
	// mTab3,
	private LinearLayout img_weixin_ll, img_address_ll, img_settings_ll;
	private int zero = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int one;// 单个水平动画位移
	private int two;
	// private int three;
	// private LinearLayout mClose;
	private LinearLayout mCloseBtn;
	private View layout;
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	private RelativeLayout personalInfoLayout;

	private Button btnAddNewFriend;
	final ArrayList<View> views = new ArrayList<View>();

	// private Button mRightBtn;
	private static final int MSG_SET_TAGS = 1002;
	private final Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_TAGS:
				Log.d(TAG, "Set tags in handler.");
				JPushInterface.setAliasAndTags(getApplicationContext(), null,
						(Set<String>) msg.obj, mTagsCallback);
				break;

			default:
				Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_weixin);

		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		instance = this;

		Constants.USER_ID = (int) CacheUtil.getUser(MainWeixin.this)
				.getUserid();
		Constants.OWN_HEAD_IMAGE = BitmapUtil.getBitmap(CacheUtil.getUser(
				MainWeixin.this).getPicture());

		/*
		 * mRightBtn = (Button) findViewById(R.id.right_btn);
		 * mRightBtn.setOnClickListener(new Button.OnClickListener() { @Override
		 * public void onClick(View v) { showPopupWindow
		 * (MainWeixin.this,mRightBtn); } });
		 */

		mTabPager = (ViewPager) findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

		img_weixin_ll = (LinearLayout) findViewById(R.id.img_weixin_ll);
		img_address_ll = (LinearLayout) findViewById(R.id.img_address_ll);
		img_settings_ll = (LinearLayout) findViewById(R.id.img_settings_ll);
		mTab1 = (ImageView) findViewById(R.id.img_weixin);
		mTab2 = (ImageView) findViewById(R.id.img_address);
		// mTab3 = (ImageView) findViewById(R.id.img_friends);
		mTab4 = (ImageView) findViewById(R.id.img_settings);
		mTabImg = (ImageView) findViewById(R.id.img_tab_now);
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		// mTab3.setOnClickListener(new MyOnClickListener(2));
		mTab4.setOnClickListener(new MyOnClickListener(3));
		img_weixin_ll.setOnClickListener(new MyOnClickListener(0));
		img_address_ll.setOnClickListener(new MyOnClickListener(1));
		img_settings_ll.setOnClickListener(new MyOnClickListener(3));
		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		// int displayHeight = currDisplay.getHeight();
		one = displayWidth / 3; // 设置水平动画平移大小
		two = one * 2;
		// three = one * 3;
		// Log.i("info", "获取的屏幕分辨率为" + one + two + three + "X" + displayHeight);

		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTabImg
				.getLayoutParams();
		lp.setMargins(one / 4, 0, 0, 0);

		// InitImageView();//使用动画
		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.main_tab_weixin, null);
		View view2 = mLi.inflate(R.layout.main_tab_address, null);
		// View view3 = mLi.inflate(R.layout.main_tab_friends, null);
		View view4 = mLi.inflate(R.layout.main_tab_settings, null);

		btnAddNewFriend = (Button) view2.findViewById(R.id.btn_add_new_friend);
		btnAddNewFriend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//修改
				Intent intent = new Intent(MainWeixin.this,
						AddNewFriendActivity.class);
				startActivity(intent);
				/*Intent intent = new Intent(MainWeixin.this,
						GroupActivity.class);
				startActivity(intent);*/
			}
		});

		personalInfoLayout = (RelativeLayout) view4
				.findViewById(R.id.setting_personal_info);

		personalInfoLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainWeixin.this,
						PersonalInfoActivity.class);
				startActivity(intent);
			}
		});

		// 每个页面的view数据
		// final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		// views.add(view3);
		views.add(view4);
		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {

				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mTabPager.setAdapter(mPagerAdapter);

		ShowAllContactsHistory.showContactsHistory(MainWeixin.this,
				displayWidth, view1);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(getApplicationContext());
		// String rID =
		// JPushInterface.getRegistrationID(getApplicationContext());
		// Log.i(TAG, "+++++++++++++++++++>>" + rID);
		setJPushTag();
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(getApplicationContext());
		super.onResume();
		Log.i("info", "onResume()");
		// test
		ContactsViewWorker.initialize(instance, views.get(1));
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(getApplicationContext());
		super.onPause();
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};

	/*
	 * 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_weixin_pressed));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, zero, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_address_normal));
				}
				// else if (currIndex == 2) {
				// animation = new TranslateAnimation(two, 0, 0, 0);
				// mTab3.setImageDrawable(getResources().getDrawable(
				// R.drawable.tab_find_frd_normal));
				// }
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, zero, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_settings_normal));
				}
				break;
			case 1:
				ShowAllContactsHistory.cleanFilter();
				mTab2.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_address_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_weixin_normal));
				}
				// else if (currIndex == 2) {
				// animation = new TranslateAnimation(two, one, 0, 0);
				// mTab3.setImageDrawable(getResources().getDrawable(
				// R.drawable.tab_find_frd_normal));
				// }
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_settings_normal));
				}
				// test
				ContactsViewWorker.initialize(instance, views.get(1));
				break;
			// case 2:
			// mTab3.setImageDrawable(getResources().getDrawable(
			// R.drawable.tab_find_frd_pressed));
			// if (currIndex == 0) {
			// animation = new TranslateAnimation(zero, two, 0, 0);
			// mTab1.setImageDrawable(getResources().getDrawable(
			// R.drawable.tab_weixin_normal));
			// } else if (currIndex == 1) {
			// animation = new TranslateAnimation(one, two, 0, 0);
			// mTab2.setImageDrawable(getResources().getDrawable(
			// R.drawable.tab_address_normal));
			// } else if (currIndex == 3) {
			// animation = new TranslateAnimation(three, two, 0, 0);
			// mTab4.setImageDrawable(getResources().getDrawable(
			// R.drawable.tab_settings_normal));
			// }
			// break;
			case 2:
				ShowAllContactsHistory.cleanFilter();
				mTab4.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_settings_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(
							R.drawable.tab_address_normal));
				}
				// else if (currIndex == 2) {
				// animation = new TranslateAnimation(two, three, 0, 0);
				// mTab3.setImageDrawable(getResources().getDrawable(
				// R.drawable.tab_find_frd_normal));
				// }
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 获取
																				// back键

			if (menu_display) { // 如果 Menu已经打开 ，先关闭Menu
				menuWindow.dismiss();
				menu_display = false;
			} else {
				Intent intent = new Intent();
				intent.setClass(MainWeixin.this, ExitActivity.class);
				startActivity(intent);
			}
		}

		else if (keyCode == KeyEvent.KEYCODE_MENU) { // 获取 Menu键
			if (!menu_display) {
				// 获取LayoutInflater实例
				inflater = (LayoutInflater) this
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				// 这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
				// 该方法返回的是一个View的对象，是布局中的根
				layout = inflater.inflate(R.layout.activity_menu_main, null);

				// 下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
				menuWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT); // 后两个参数是width和height
				// menuWindow.showAsDropDown(layout); //设置弹出效果
				// menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				// 如何获取我们main中的控件呢？也很简单
				// mClose = (LinearLayout) layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout) layout
						.findViewById(R.id.menu_close_btn);

				// 下面对每一个Layout进行单击事件的注册吧。。。
				// 比如单击某个MenuItem的时候，他的背景色改变
				// 事先准备好一些背景图片或者颜色
				mCloseBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// Toast.makeText(Main.this, "退出",
						// Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
						intent.setClass(MainWeixin.this, ExitActivity.class);
						startActivity(intent);
						menuWindow.dismiss(); // 响应点击事件之后关闭Menu
					}
				});
				menu_display = true;
			} else {
				// 如果当前已经为显示状态，则隐藏起来
				menuWindow.dismiss();
				menu_display = false;
			}

			return false;
		}
		return false;
	}

	// 设置标题栏右侧按钮的作用
	public void btnmainright(View v) {
		Intent intent = new Intent(MainWeixin.this, MainTopRightDialog.class);
		startActivity(intent);
		// Toast.makeText(getApplicationContext(), "点击了功能按钮",
		// Toast.LENGTH_LONG).show();
	}

	public void startChat(View v) { // 小黑 对话界面
		// close opened items
		new ShowAllContactsHistory().closeOpenedListItems();
		if (NetworkUtil.isNetworkConnected(MainWeixin.this)) {
			TextView history_contact_userId = (TextView) v
					.findViewById(R.id.history_contact_userid);
			TextView history_contact_name = (TextView) v
					.findViewById(R.id.history_contact_name);
			TextView history_contact_userName = (TextView) v
					.findViewById(R.id.history_contact_username);

			TextView history_contact_groupName = (TextView) v
					.findViewById(R.id.group_name);
			TextView isGroup_TV = (TextView) v
					.findViewById(R.id.isGroup_TV);
			
			ImageView history_contact_head = (ImageView) v
					.findViewById(R.id.history_contact_head);
			history_contact_head.setDrawingCacheEnabled(true);
			Bitmap bitmap = Bitmap.createBitmap(history_contact_head
					.getDrawingCache());
			history_contact_head.setDrawingCacheEnabled(false);
			Constants.CHATING_HEAD_IMAGE = bitmap;

			Bundle bundle = new Bundle();
			bundle.putString("contact_userid",
					"" + history_contact_userId.getText());
			bundle.putString("contact_name",
					"" + history_contact_name.getText());
			bundle.putString("contact_userName",
					"" + history_contact_userName.getText());
			bundle.putString("contact_groupName",
					"" + history_contact_groupName.getText());
			bundle.putString("isGroup",
					"" + isGroup_TV.getText());

			Intent intent = new Intent(MainWeixin.this, ChatActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			if (ChatActivity.mDataArrays != null) {
				ChatActivity.mDataArrays.clear();
				if (ChatActivity.mAdapter != null) {
					ChatActivity.mAdapter.notifyDataSetChanged();
				}
			}
		} else {
			Toast.makeText(MainWeixin.this, "network anomaly",
					Toast.LENGTH_LONG).show();
		}

	}

	public void add_new_friend_setting(View view) {
		Intent intent = new Intent(MainWeixin.this, AddNewFriendActivity.class);
		startActivity(intent);
	}

	public void exit_settings(View v) { // 退出 伪“对话框”，其实是一个activity
		Intent intent = new Intent(MainWeixin.this, ExitFromSettings.class);
		startActivity(intent);
	}

	public void btn_shake(View v) { // 手机摇一摇
		Intent intent = new Intent(MainWeixin.this, ShakeActivity.class);
		startActivity(intent);
	}

	public interface ContactsList {
		// close opened contacts history list items
		public void closeOpenedListItems();
	}

	private void setJPushTag() {
		String tag = CacheUtil.getUser(MainWeixin.this).getUsername();
		Log.i(TAG, "xxxxxxxx--->" + tag);
		// 检查 tag 的有效性
		if (TextUtils.isEmpty(tag)) {
			Log.i(TAG, "xxxxxxxx can not be empty");
			return;
		}

		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!JPushUtil.isValidTagAndAlias(sTagItme)) {
				Log.i(TAG, "xxxxxxxx can not be empty");
				return;
			}
			tagSet.add(sTagItme);
		}

		// 调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

	}

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i(TAG, logs);
				if (JPushUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(
							mHandler.obtainMessage(MSG_SET_TAGS, tags),
							1000 * 60);
				} else {
					Log.i(TAG, "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}

			// JPushUtil.showToast(logs, getApplicationContext());
		}

	};
}
