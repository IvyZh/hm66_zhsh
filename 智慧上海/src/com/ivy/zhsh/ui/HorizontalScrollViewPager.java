package com.ivy.zhsh.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalScrollViewPager extends ViewPager {

	private int startY;
	private int startX;

	public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalScrollViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			startY = (int) ev.getY();
			startX = (int) ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int endY = (int) ev.getY();
			int endX = (int) ev.getX();
			if (startY == -1) {// 如果用户按住头条新闻向下滑动, 会导致listview无法拿到ACTION_DOWN,
				// 此时要重新获取startY
				startY = (int) ev.getY();
			}
			if (startX == -1) {// 如果用户按住头条新闻向下滑动, 会导致listview无法拿到ACTION_DOWN,
				// 此时要重新获取startY
				startX = (int) ev.getY();
			}
			int dy = endY - startY;
			int dx = endX - startX;

			if (Math.abs(dy) > Math.abs(dx)) {// 上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);
			} else {
				if (dx > 0) {
					if (getCurrentItem() == 0) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					if (getCurrentItem() == this.getAdapter().getCount() - 1) {
						// 最后一个item
						// 请求父控件及祖宗控件拦截事件
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}

			}

			break;
		case MotionEvent.ACTION_UP:

			break;
		}

		return super.onTouchEvent(ev);
	}
}
