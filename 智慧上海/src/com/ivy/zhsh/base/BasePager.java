package com.ivy.zhsh.base;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ivy.zhsh.R;
import com.ivy.zhsh.activity.MainActivity;

/**
 *  五大模块需要继承此BasePager：首页新闻中心，智慧服务等
 */
public abstract class BasePager {
	
	public View mRootView;
	public Activity mActivity;
	public TextView tvTitle;
	public ImageButton btnMenu;
	// 帧布局容器, 将来要动态向里面添加内容
	public FrameLayout flContent;
	// 组图切换按钮
	public ImageButton btnDisplay;
	
	public BasePager(Activity activity){
		this.mActivity = activity;
		initView();
		// initData(); 可以不用在这里就初始化数据，当获取到网络数据之后，手动调用
	}
	
	public void initView(){
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
		btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
		flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
		btnDisplay = (ImageButton) mRootView.findViewById(R.id.btn_display);

		btnMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle();
			}
		});
	}
	protected void toggle() {
		MainActivity mainActivty = (MainActivity) mActivity;
		mainActivty.toggle();
	}

	public abstract void initData();
}
