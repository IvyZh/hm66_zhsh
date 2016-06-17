package com.ivy.zhsh.base.impl.content;

import java.util.ArrayList;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ivy.zhsh.activity.MainActivity;
import com.ivy.zhsh.base.BaseMenuDetailPager;
import com.ivy.zhsh.base.BasePager;
import com.ivy.zhsh.base.impl.LeftFragment;
import com.ivy.zhsh.base.impl.menudetail.HudongDetail;
import com.ivy.zhsh.base.impl.menudetail.NewsDetail;
import com.ivy.zhsh.base.impl.menudetail.PhotosDetail;
import com.ivy.zhsh.base.impl.menudetail.TopicDetail;
import com.ivy.zhsh.domain.Category;
import com.ivy.zhsh.gloable.Constants;
import com.ivy.zhsh.utils.L;
import com.ivy.zhsh.utils.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsCenterPager extends BasePager {

	public ArrayList<BaseMenuDetailPager> mDetailList;


	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initView() {
		super.initView();
		
		tvTitle.setText("新闻中心");
		TextView tv = new TextView(mActivity);
		tv.setText(getClass().getSimpleName());
		tv.setTextSize(20);
		tv.setGravity(Gravity.CENTER);
//		flContent.addView(tv);
	}

	@Override
	public void initData() {
		L.v("--"+getClass().getSimpleName()+",initData");
		
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, Constants.CATEGORIES_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				L.v(result);
				
				processResult(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				ToastUtils.showToast(mActivity, msg);
			}
		});
	}
	
	protected void processResult(String result) {
		
		Gson gson = new Gson();
		Category json = gson.fromJson(result, Category.class);
		L.v(json.toString());
		MainActivity mainActivity = (MainActivity) mActivity;
		LeftFragment fragment = mainActivity.getMenuFragment();
		fragment.setData(json.data);
		
		
		mDetailList = new ArrayList<BaseMenuDetailPager>();
		mDetailList.add(new NewsDetail(mActivity,json.data.get(0).children));
		mDetailList.add(new TopicDetail(mActivity));
		mDetailList.add(new PhotosDetail(mActivity,btnDisplay));
		mDetailList.add(new HudongDetail(mActivity));
		
		flContent.removeAllViews();
		mDetailList.get(0).initData();
		flContent.addView(mDetailList.get(0).mRootView);
	}

	
	/**
	 * 改变新闻详情页
	 * @param pos
	 */
	public void changeMenuDetail(int pos){
		flContent.removeAllViews();
		BaseMenuDetailPager pager = mDetailList.get(pos);
		flContent.addView(pager.mRootView) ;
		if(pager instanceof PhotosDetail){
			btnDisplay.setVisibility(View.VISIBLE);
		}else{
			btnDisplay.setVisibility(View.GONE);
		}
	}
}
