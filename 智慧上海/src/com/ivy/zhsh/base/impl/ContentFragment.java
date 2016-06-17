package com.ivy.zhsh.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ivy.zhsh.R;
import com.ivy.zhsh.activity.MainActivity;
import com.ivy.zhsh.base.BaseFragment;
import com.ivy.zhsh.base.BasePager;
import com.ivy.zhsh.base.impl.content.GovernmentPager;
import com.ivy.zhsh.base.impl.content.HomePager;
import com.ivy.zhsh.base.impl.content.NewsCenterPager;
import com.ivy.zhsh.base.impl.content.ServerPager;
import com.ivy.zhsh.base.impl.content.SettingPager;

public class ContentFragment extends BaseFragment implements OnPageChangeListener {

	private ViewPager mViewPager;
	public ArrayList<BasePager> mPagerList;
	private RadioGroup mRgGroup;

	@Override
	public View initView() {
		View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_content, null);
		mRgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
		
		return view;
	}
	
	@Override
	public void initData() {
		
		mPagerList = new ArrayList<BasePager>();
		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new NewsCenterPager(mActivity));
		mPagerList.add(new ServerPager(mActivity));
		mPagerList.add(new GovernmentPager(mActivity));
		mPagerList.add(new SettingPager(mActivity));
		
		mViewPager = (ViewPager) mActivity.findViewById(R.id.vp_content);
		mViewPager.setAdapter(new MyAdapter());
		
		mViewPager.setOnPageChangeListener(this);
		
		MainActivity mainActivity = (MainActivity) mActivity;
		mainActivity.setSlidingMenuMode(false);
		
		mPagerList.get(0).initData();// 第一次进来要加载数据
		
		mRgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					// 首页
					mViewPager.setCurrentItem(0,false);
					mPagerList.get(0).initData();
					break;
				case R.id.rb_news:
					// 新闻中心
					mViewPager.setCurrentItem(1,false);
					mPagerList.get(1).initData();
					break;
				case R.id.rb_smart:
					// 智慧服务
					mViewPager.setCurrentItem(2,false);
					mPagerList.get(2).initData();
					break;
				case R.id.rb_gov:
					// 政务
					mViewPager.setCurrentItem(3,false);
					mPagerList.get(3).initData();
					break;
				case R.id.rb_setting:
					// 设置
					mViewPager.setCurrentItem(4,false);
					mPagerList.get(4).initData();
					break;
				}
				
				
				
			}
		});
		
		
	}

	
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mPagerList.get(position).mRootView);
			return mPagerList.get(position).mRootView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
 			// super.destroyItem(container, position, object); 这句话要注销
			container.removeView((View) object);
		}
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		
		if(position==2||position==3||position ==1){
			// 设置SlidingMenu的滑动
			MainActivity mainActivity = (MainActivity) mActivity;
			mainActivity.setSlidingMenuMode(true);
		}else{
			MainActivity mainActivity = (MainActivity) mActivity;
			mainActivity.setSlidingMenuMode(false);
		}
	}
	
	
}
