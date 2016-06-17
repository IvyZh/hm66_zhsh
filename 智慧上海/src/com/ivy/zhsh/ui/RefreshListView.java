package com.ivy.zhsh.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ivy.zhsh.R;

public class RefreshListView extends ListView implements OnScrollListener {

	private View headerView;
	private int startY = -1;
	private int mHeaderheight;

	private int mCurrentState = 0;
	private static final int STATE_PULL_TO_REFRESH = 1;// 下拉刷新
	private static final int STATE_RELEASE_TO_REFRESH = 2;// 松开刷新
	private static final int STATE_REFRESHING = 3;// 正在刷新

	private TextView tvTitle;
	private ImageView ivArrow;
	private ProgressBar pbLoading;
	private TextView tvTime;
	private RotateAnimation animUp;
	private RotateAnimation animDown;
	private RefreshListener mRefreshListener;
	private View mFooterView;
	private int mFooterViewHeight;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public RefreshListView(Context context) {
		super(context);

		initView(context);
	}

	private void initView(Context context) {
		headerView = View.inflate(context, R.layout.list_refresh_header, null);
		this.addHeaderView(headerView);

		tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
		ivArrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
		pbLoading = (ProgressBar) headerView.findViewById(R.id.pb_loading);
		tvTime = (TextView) headerView.findViewById(R.id.tv_time);

		// 先隐藏
		headerView.measure(0, 0);
		mHeaderheight = headerView.getMeasuredHeight();
		headerView.setPadding(0, -mHeaderheight, 0, 0);

		initAnim();
		
		
		initFooterView();
	}
	
	
	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.list_refresh_footer,
				null);
		this.addFooterView(mFooterView);

		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();

		// 隐藏脚布局
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);

		// 设置滑动监听
		this.setOnScrollListener(this);
	}

	
	/**
	 * 初始化箭头动画
	 */
	private void initAnim() {
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(500);
		animUp.setFillAfter(true);// 保持状态

		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(500);
		animDown.setFillAfter(true);// 保持状态
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			
			if(mCurrentState==STATE_REFRESHING){
				break;
			}
			int endY = (int) ev.getY();
			int dy = endY - startY;
			if (startY == -1) {// 如果用户按住头条新闻向下滑动, 会导致listview无法拿到ACTION_DOWN,
				// 此时要重新获取startY
				startY = (int) ev.getY();
			}
			if (dy > 0 && getFirstVisiblePosition() == 0) {
//				L.v("---" + startY + "---" + endY);
				// mHeaderheight = headerView.getMeasuredHeight(); 不能这么写
				int padding = -mHeaderheight + dy;
				if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					mCurrentState = STATE_RELEASE_TO_REFRESH;
				} else if (padding < 0
						&& mCurrentState != STATE_PULL_TO_REFRESH) {
					mCurrentState = STATE_PULL_TO_REFRESH;
				}
				refreshState();
				headerView.setPadding(0, padding, 0, 0);
				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// 起始坐标归零
			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				// 如果当前是松开刷新, 就要切换为正在刷新
				mCurrentState = STATE_REFRESHING;
				// 显示头布局
				headerView.setPadding(0, 0, 0, 0);
				refreshState();
				
				if (mRefreshListener != null) {
					mRefreshListener.refresh();
				}
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// 隐藏头布局
				headerView.setPadding(0, -mHeaderheight, 0, 0);
			}
			break;
		}

		return super.onTouchEvent(ev);
	}

	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("下拉刷新");
			// 箭头向下移动
			ivArrow.startAnimation(animDown);
			// 隐藏进度条
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			break;
		case STATE_RELEASE_TO_REFRESH:
			tvTitle.setText("松开刷新");
			// 箭头向上移动
			ivArrow.startAnimation(animUp);
			// 隐藏进度条
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新...");
			pbLoading.setVisibility(View.VISIBLE);
			ivArrow.clearAnimation();// 必须清除动画,才能隐藏控件
			ivArrow.setVisibility(View.INVISIBLE);
			break;
		}
	}

	public void setRefreshListener(RefreshListener listener){
		this.mRefreshListener = listener;
	}
	
	public interface  RefreshListener{
		public void refresh();
		public void loadMore();
	}

	/**
	 * 刷新成功或者失败
	 */
	public void setRefreshCompleteSuccess(boolean refreshState,int loadState) {
		if(loadState==2){
			// 没有更多数据了
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		}
		
		headerView.setPadding(0, -mHeaderheight, 0, 0);
		mCurrentState = STATE_PULL_TO_REFRESH;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		if (scrollState == SCROLL_STATE_IDLE) {
			int lastVisiblePosition = getLastVisiblePosition();// 当前界面显示的最后一个item的位置
			if (lastVisiblePosition >= getCount() - 1 ) {
				 System.out.println("到底了");
				// 加载更多了....(到底了)
				// 显示脚布局
				mFooterView.setPadding(0, 0, 0, 0);
				// listview设置当前要展示的item的位置
				setSelection(getCount() - 1);// 跳到加载更多item的位置去展示
				if (mRefreshListener != null) {
					mRefreshListener.loadMore();
				}
			}
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
}
