package com.ivy.zhsh.base.impl.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ivy.zhsh.R;
import com.ivy.zhsh.activity.MainActivity;
import com.ivy.zhsh.base.BaseMenuDetailPager;
import com.ivy.zhsh.base.impl.tab.TabNewsDetail;
import com.ivy.zhsh.domain.Category.Menu.Children;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

public class NewsDetail extends BaseMenuDetailPager {

	@ViewInject(R.id.iv_next_page)
	private ImageView mNext;
	@ViewInject(R.id.vp_news_detail)
	private ViewPager mViewPager;
	private final ArrayList<Children> childrenList;
	private ArrayList<TabNewsDetail> tabDetaiList;
	
	@ViewInject(R.id.indicator)
	private TabPageIndicator mIndicator;
	
	
	public NewsDetail(Activity activity, ArrayList<Children> children) {
		super(activity);
		this.childrenList = children;
	}

	@Override
	public View initView() {
		// TextView view = new TextView(mActivity);
		// view.setText("菜单详情页-新闻");
		// view.setTextColor(Color.RED);
		// view.setTextSize(22);
		// view.setGravity(Gravity.CENTER);

		View view = View.inflate(mActivity, R.layout.pager_newsdetail, null);
		ViewUtils.inject(this,view);
		
		
		return view;
	}
	
	
	@Override
	public void initData() {
		tabDetaiList = new ArrayList<TabNewsDetail>();
		
		for (int i = 0; i < childrenList.size(); i++) {
			tabDetaiList.add(new TabNewsDetail(mActivity,childrenList.get(i)));
		}
		
		
		NewsMenuAdapter adapter = new NewsMenuAdapter();
		mViewPager.setAdapter(adapter);
		
		
		// 此方法在viewpager设置完数据之后再调用
		mIndicator.setViewPager(mViewPager);// 将页面指示器和ViewPager关联起来
		
		// 当viewpager和指针绑定时,需要将页面切换监听设置给指针
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				if(pos==0){// 打开SlidingMenu滑动
					MainActivity mainActivity = (MainActivity) mActivity;
					mainActivity.setSlidingMenuMode(true);
				}else{// 禁止SlidingMenu滑动
					MainActivity mainActivity = (MainActivity) mActivity;
					mainActivity.setSlidingMenuMode(false);
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		mNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int currentItem = mViewPager.getCurrentItem();
				currentItem++;
				mViewPager.setCurrentItem(currentItem);
			}
		});
	}

	
	class NewsMenuAdapter extends PagerAdapter {
		// 返回页面指示器的标题
		@Override
		public CharSequence getPageTitle(int position) {
			return childrenList.get(position).title;
		}

		@Override
		public int getCount() {
			return childrenList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabNewsDetail tabNewsDetail = tabDetaiList.get(position);
			container.addView(tabNewsDetail.mRootView);
			tabNewsDetail.initData();// 初始化数据
			return tabNewsDetail.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
