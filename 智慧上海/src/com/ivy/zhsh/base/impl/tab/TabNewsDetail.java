package com.ivy.zhsh.base.impl.tab;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ivy.zhsh.R;
import com.ivy.zhsh.activity.NewsDetailActivity;
import com.ivy.zhsh.base.BaseMenuDetailPager;
import com.ivy.zhsh.domain.Category.Menu.Children;
import com.ivy.zhsh.domain.NewsData;
import com.ivy.zhsh.domain.NewsData.News;
import com.ivy.zhsh.gloable.Constants;
import com.ivy.zhsh.ui.HorizontalScrollViewPager;
import com.ivy.zhsh.ui.RefreshListView;
import com.ivy.zhsh.ui.RefreshListView.RefreshListener;
import com.ivy.zhsh.utils.L;
import com.ivy.zhsh.utils.SharedPreUtils;
import com.ivy.zhsh.utils.ToastUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

public class TabNewsDetail extends BaseMenuDetailPager implements
		OnPageChangeListener {

	private final Children children;
	@ViewInject(R.id.lv_tab_detail)
	private RefreshListView lvTabDetail;
	private NewsData mNewsTabData;

	private HorizontalScrollViewPager detailViewPager;
	private CirclePageIndicator indicator;
	private TextView tvHeaderTitle;

	public TabNewsDetail(Activity activity, Children children) {
		super(activity);
		this.children = children;
		L.v("children==null" + (children == null));
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (detailViewPager != null && mNewsTabData != null) {
				int currentItem = detailViewPager.getCurrentItem() + 1;
				if (currentItem >= mNewsTabData.data.topnews.size()) {
					currentItem = 0;
				}
				detailViewPager.setCurrentItem(currentItem);

				handler.sendEmptyMessageDelayed(0, 2000);
			}

		};
	};
	private MyAdapter adapter;
	private ArrayList<News> mNewsList;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);

		View header = View.inflate(mActivity, R.layout.list_header_topnews,
				null);

		detailViewPager = (HorizontalScrollViewPager) header
				.findViewById(R.id.vp_tab_detail);
		indicator = (CirclePageIndicator) header.findViewById(R.id.indicator);
		tvHeaderTitle = (TextView) header.findViewById(R.id.tv_title);

		lvTabDetail.addHeaderView(header);

		detailViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					System.out.println("ACTION_DOWN");
					// 删除所有消息
					handler.removeCallbacksAndMessages(null);
					break;
				case MotionEvent.ACTION_CANCEL:// 事件取消(当按下后,然后移动下拉刷新,导致抬起后无法响应ACTION_UP,
												// 但此时会响应ACTION_CANCEL,也需要继续播放轮播条)
				case MotionEvent.ACTION_UP:
					// 延时2秒切换广告条
					handler.sendEmptyMessageDelayed(0, 2000);
					break;
				default:
					break;
				}
				return false;
			}
		});

		lvTabDetail.setRefreshListener(new RefreshListener() {

			@Override
			public void refresh() {
				getDataFromServer(false);
			}
			
			@Override
			public void loadMore() {
				String more = mNewsTabData.data.more;
				if(!TextUtils.isEmpty(more)){
					getDataFromServer(true);
				}else{
					lvTabDetail.setRefreshCompleteSuccess(false,2);
					ToastUtils.showToast(mActivity, "没有更多数据了...");
				}
			}
		});
		
		
		lvTabDetail.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				News news = mNewsList.get(position-2);

				// 当前点击的item的标题颜色置灰
				TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
				tvTitle.setTextColor(Color.GRAY);

				// 将已读状态持久化到本地
				// key: read_ids; value: 1324,1325,1326
				String readIds = SharedPreUtils.getString(mActivity,"read_ids");
				if(readIds==null){
					readIds="";
				}
				if (!readIds.contains(news.id)) {// 以前没有添加过,才添加进来
					readIds = readIds + news.id + ",";// 1324,1325,
					SharedPreUtils.putString(mActivity, "read_ids", readIds);
				}

				// 跳到详情页
				Intent intent = new Intent(mActivity, NewsDetailActivity.class);
				intent.putExtra("url", news.url);
				mActivity.startActivity(intent);
			}
		});

		return view;

		// tv = new TextView(mActivity);
		// tv.setTextSize(20);
		// tv.setGravity(Gravity.CENTER);
		// return tv;
	}

	@Override
	public void initData() {
		getDataFromServer(false);
	}

	private void getDataFromServer(final boolean isLoadMore) {
		String url = children.url;
		if(isLoadMore){
			url = Constants.SERVER_URL + mNewsTabData.data.more;
		}else{
			url = Constants.SERVER_URL + url;
		}
		
		L.v("----url-----"+url);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				prcocessResult(responseInfo.result,isLoadMore);
				lvTabDetail.setRefreshCompleteSuccess(true,-1);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				lvTabDetail.setRefreshCompleteSuccess(false,-1);
			}
		});

	}

	protected void prcocessResult(String result, boolean isLoadMore) {
		
		L.v("result---"+result);
		
		if(isLoadMore){
			Gson gson = new Gson();
			mNewsTabData = gson.fromJson(result, NewsData.class);
			
			mNewsList.addAll(mNewsTabData.data.news);

			adapter.notifyDataSetChanged();
		}else{
			Gson gson = new Gson();
			mNewsTabData = gson.fromJson(result, NewsData.class);

			
			mNewsList = mNewsTabData.data.news;
			// 头条新闻
			MyPagerAdapter pagerAdapter = new MyPagerAdapter(mActivity);
			detailViewPager.setAdapter(pagerAdapter);

			indicator.setViewPager(detailViewPager);

			indicator.setOnPageChangeListener(this);
			adapter = new MyAdapter(mActivity);
			lvTabDetail.setAdapter(adapter);

			String title = mNewsTabData.data.topnews.get(0).title;
			tvHeaderTitle.setText(title);

			handler.sendEmptyMessageDelayed(0, 1000);
		}
		
	}

	class MyPagerAdapter extends PagerAdapter {
		private BitmapUtils utils;

		public MyPagerAdapter(Context context) {
			utils = new BitmapUtils(context);
			utils.configDefaultLoadFailedImage(R.drawable.news_pic_default);
			utils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return mNewsTabData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(mActivity);
			view.setScaleType(ScaleType.FIT_XY);// 设置图片填充效果, 表示填充父窗体
			// 获取图片链接, 使用链接下载图片, 将图片设置给ImageView, 考虑内存溢出问题, 图片本地缓存

			String url = mNewsTabData.data.topnews.get(position).topimage
					.replace(":8080", ":80");
			utils.display(view, url);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	class MyAdapter extends BaseAdapter {

		private BitmapUtils utils;

		public MyAdapter(Context context) {
			utils = new BitmapUtils(context);
			utils.configDefaultLoadFailedImage(R.drawable.news_pic_default);
			utils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public News getItem(int position) {
			 News news = mNewsList.get(position);
			return news;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_news,
						null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);
				holder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			News news = getItem(position);
			holder.tvTitle.setText(news.title);
			holder.tvDate.setText(news.pubdate);

			L.v("tabnewsDetails:" + news.listimage);

			// 把服务器的端口修改成80
			news.listimage = news.listimage.replace(":8080", ":80");

			utils.display(holder.ivIcon, news.listimage);

			// 标记已读和未读
			String readIds = SharedPreUtils.getString(mActivity, "read_ids");
			if (readIds != null && readIds.contains(news.id)) {
				// 已读
				holder.tvTitle.setTextColor(Color.GRAY);
			} else {
				// 未读
				holder.tvTitle.setTextColor(Color.BLACK);
			}

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivIcon;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		if (mNewsTabData != null) {
			String title = mNewsTabData.data.topnews.get(arg0).title;
			tvHeaderTitle.setText(title);
		}

	}
}
