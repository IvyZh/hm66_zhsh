package com.ivy.zhsh.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.ivy.zhsh.R;
import com.ivy.zhsh.base.BasePager;
import com.ivy.zhsh.base.impl.ContentFragment;
import com.ivy.zhsh.base.impl.LeftFragment;
import com.ivy.zhsh.base.impl.content.NewsCenterPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
	private SlidingMenu slidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.fragment_menu);

		slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏都可以滑动
		slidingMenu.setBehindOffset(200);// 设置预留边界

		initFragment();
	}

	private void initFragment() {
		ContentFragment contentFragment = new ContentFragment();
		LeftFragment leftFragment = new LeftFragment();

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fl_root_main, contentFragment, "CONTENT");
		ft.replace(R.id.fl_root_left, leftFragment, "MENU");
		ft.commit();
		
	}

	
	public LeftFragment getMenuFragment(){
		FragmentManager fm = getSupportFragmentManager();
		LeftFragment fragment = (LeftFragment) fm.findFragmentByTag("MENU");
		return fragment;
	}
	public ContentFragment getContentFragment(){
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm.findFragmentByTag("CONTENT");
		return fragment;
	}
	
	/**
	 * 设置SlidingMenu的滑动
	 * @param b
	 */
	public void setSlidingMenuMode(boolean b) {
		if(b){
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
	/**
	 * 开关SlidingMenu
	 */
	public void toggle() {
		slidingMenu.toggle();
	}
	
	/**
	 * 改变新闻Detail的页签
	 * @param pos
	 */
	public void changeNewDetail(int pos){
		ContentFragment fragment = getContentFragment();
		NewsCenterPager basePager = (NewsCenterPager) fragment.mPagerList.get(1);
		basePager.changeMenuDetail(pos);
		basePager.mDetailList.get(pos).initData();
	}

}
