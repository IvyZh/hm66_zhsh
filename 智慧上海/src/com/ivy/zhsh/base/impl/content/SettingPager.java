package com.ivy.zhsh.base.impl.content;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ivy.zhsh.base.BasePager;
import com.ivy.zhsh.utils.L;

public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initView() {
		super.initView();
		tvTitle.setText("设置");
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
