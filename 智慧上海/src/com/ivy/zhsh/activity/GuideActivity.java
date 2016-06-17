package com.ivy.zhsh.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ivy.zhsh.R;
import com.ivy.zhsh.utils.L;
import com.ivy.zhsh.utils.SharedPreUtils;

public class GuideActivity extends Activity {

	private ViewPager mViewPager;
	private int imgIds[] = {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
	private ArrayList<ImageView> mImagViewList;
	private ImageView mIvPoint;
	private int mPointDx = 0;
	private Button btnStart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		
		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		btnStart = (Button) findViewById(R.id.btn_start);
		
		mImagViewList = new ArrayList<ImageView>();
		for (int i = 0; i < 3; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imgIds[i]);
			mImagViewList.add(imageView);
		}
		//  小红点
		mIvPoint = (ImageView) findViewById(R.id.iv_red_point);
		final ImageView ivPoint1 = (ImageView) findViewById(R.id.iv_point_1);
		final ImageView ivPoint2 = (ImageView) findViewById(R.id.iv_point_2);
		
		// 获取两个小圆点的间距
		mIvPoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// 会执行多次
				mIvPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				//mIvPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this); //api16
				mPointDx = ivPoint2.getLeft() - ivPoint1.getLeft();
				L.v("pointDx:"+mPointDx);
			}
		});
		
		mViewPager.setAdapter(new MyAdapter());
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(position==2){
					btnStart.setVisibility(View.VISIBLE);
				}else{
					btnStart.setVisibility(View.GONE);
				}
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				L.v("positionOffset:"+positionOffset);
				int dx = (int) (mPointDx * positionOffset + position *  mPointDx);
				RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mIvPoint.getLayoutParams();
				params.leftMargin = dx;
				mIvPoint.setLayoutParams(params);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		
		
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreUtils.putBoolean(getApplicationContext(), "is_guide", true);
				startActivity(new Intent(GuideActivity.this,MainActivity.class));
				finish();
			}
		});
	}
	
	
	class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return mImagViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mImagViewList.get(position));
			return mImagViewList.get(position);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
 			// super.destroyItem(container, position, object); 这句话要注销
			container.removeView((View) object);
		}
	}

}
