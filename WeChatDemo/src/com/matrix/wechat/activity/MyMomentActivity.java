package com.matrix.wechat.activity;

import java.util.ArrayList;
import java.util.List;

import com.matrix.wechat.R;
import com.matrix.wechat.adapter.MyMonentAdapter;
import com.matrix.wechat.model.Moment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MyMomentActivity extends Activity{

	private ListView lv_myMoment=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_moment);
		lv_myMoment=(ListView) findViewById(R.id.my_moment);
		setData();
	}
	
	private void setData(){
		List<Moment> monentList=new ArrayList<Moment>();
		
		Moment moment1=new Moment();
		moment1.setDate("10.4");
		moment1.setContent_text("test1qqqqqqqqqqqqqqqqaaaaaaaaaaaaaaffffffffff");
		monentList.add(moment1);
		
		Moment moment2=new Moment();
		moment2.setDate("10.1");
		moment2.setContent_text("test1qqqqqqqqqqqqqqqqaaaaaaaaaaaaaafffffffffffffffffasas");
		monentList.add(moment2);
		
		Moment moment3=new Moment();
		moment3.setDate("09.4");
		moment3.setContent_text("test1qqqqqqqqqqqqqqqqaafaaaaaaaaaaabgbzszxxxxxxxxxxxxxxxxxxxxxx");
		monentList.add(moment3);
		
		MyMonentAdapter adapter=new MyMonentAdapter(this);
		adapter.setData(monentList);
		lv_myMoment.setAdapter(adapter);
	}
}
