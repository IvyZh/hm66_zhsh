package com.ivy.zhsh.base.impl.content;

import android.app.Activity;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ivy.zhsh.base.BasePager;
import com.ivy.zhsh.domain.Category;
import com.ivy.zhsh.gloable.Constants;
import com.ivy.zhsh.utils.L;
import com.ivy.zhsh.utils.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initView() {
		super.initView();
		tvTitle.setText("智慧上海");
		btnMenu.setVisibility(View.GONE);
		
		TextView tv = new TextView(mActivity);
		tv.setText(getClass().getSimpleName());
		tv.setTextSize(20);
		tv.setGravity(Gravity.CENTER);
		flContent.addView(tv);
	}

	@Override
	public void initData() {
		L.v("--"+getClass().getSimpleName()+",initData");
		
		
	}



}
