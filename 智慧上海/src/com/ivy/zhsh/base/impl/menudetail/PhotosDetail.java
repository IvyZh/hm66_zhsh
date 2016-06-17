package com.ivy.zhsh.base.impl.menudetail;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ivy.zhsh.R;
import com.ivy.zhsh.base.BaseMenuDetailPager;
import com.ivy.zhsh.domain.PhotoBean;
import com.ivy.zhsh.domain.PhotoBean.PhotoNewsData;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PhotosDetail extends BaseMenuDetailPager implements
		OnClickListener {

	private ArrayList<PhotoNewsData> mPhotoList;

	@ViewInject(R.id.lv_list)
	private ListView lvList;
	@ViewInject(R.id.gv_list)
	private GridView gvList;

	private boolean isList = true;// 当前界面状态

	private ImageButton btnDisplay;

	public PhotosDetail(Activity activity, ImageButton btnDisplay) {
		super(activity);
		this.btnDisplay = btnDisplay;
		btnDisplay.setOnClickListener(this);
//		btnDisplay.setVisibility(View.VISIBLE);
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_menu_detail_photo,
				null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();

		utils.send(HttpMethod.GET, com.ivy.zhsh.gloable.Constants.SERVER_URL
				+ "/photos/photos_1.json", new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				processResult(responseInfo.result);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});

	}

	protected void processResult(String result) {
		Gson gson = new Gson();
		PhotoBean photoBean = gson.fromJson(result, PhotoBean.class);
		mPhotoList = photoBean.data.news;

		lvList.setAdapter(new PhotoAdapter());
		gvList.setAdapter(new PhotoAdapter());
	}

	class PhotoAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public PhotoAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return mPhotoList.size();
		}

		@Override
		public PhotoNewsData getItem(int position) {
			return mPhotoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_photo,
						null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.ivIcon = (ImageView) convertView
						.findViewById(R.id.iv_icon);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PhotoNewsData item = getItem(position);

			holder.tvTitle.setText(item.title);
			mBitmapUtils.display(holder.ivIcon, item.listimage);

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvTitle;
		public ImageView ivIcon;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_display:
			// 如果当前是列表, 要切换成grid, 如果是grid,就切换成列表
			if (isList) {
				isList = false;

				lvList.setVisibility(View.GONE);
				gvList.setVisibility(View.VISIBLE);

				btnDisplay.setImageResource(R.drawable.icon_pic_list_type);
			} else {
				isList = true;

				lvList.setVisibility(View.VISIBLE);
				gvList.setVisibility(View.GONE);

				btnDisplay.setImageResource(R.drawable.icon_pic_grid_type);
			}

			break;

		default:
			break;
		}
	}
}
