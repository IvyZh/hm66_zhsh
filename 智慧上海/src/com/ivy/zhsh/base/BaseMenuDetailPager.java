package com.ivy.zhsh.base;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {
	
	public View mRootView;
	protected Activity mActivity;

	public BaseMenuDetailPager(Activity activity){
		this.mActivity = activity;
		mRootView = initView();
	}
	
	public abstract View initView();
	public void initData(){
		
	}

}
