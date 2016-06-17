# hm66_zhsh
黑马66期智慧上海

---
写在前面：

由于有几个功能在以前开发中做个，然后就没有做。

* 分享
* 缓存【这是重点！！！！】
	* 自己尝试写一个三级缓存
	* AsycTask
	* 强引用 Person a = new Person();
	- 软引用 SoftReference	  较弱
	- 弱引用 WeakReference     其次弱
	- 虚引用 PhantomReference  引用最弱
	- LruCache
* 友盟统计
* 极光推送
* 讯飞语言云
* 二维码Zxing


以上几个其中：

* 二维码
* 缓存

这两个值得再练一下，其余内容，可以单独写一个项目来。




----


1. 搭建本地服务器（tomcat）
	1. 修改chrome浏览器编码为`utf-8`

2. 完整项目需要引入3个jar包
	1. Xutils
	2. ViewPagerIndicator
	3. SlidingMenu

3. 虚拟机访问本地服务器可以用这样一个网址
	1. http://10.0.2.2:80/zhbj
	2. 同时也解决了eclipse快捷键无效的问题：alt+shift+down

4. 给单独的Activity设置全屏无titlebar
	1. android:theme="@android:style/Theme.Black.NoTitleBar.Fullscreen"


5. ViewPager的Adapter

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

6. requestWindowFeature(Window.FEATURE_NO_TITLE);
7. support-v4 代码关联
	1. http://my.oschina.net/u/1389206/blog/300172

8. 获取两个小圆点的间距

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

9. 动态移动小红点

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			L.v("positionOffset:"+positionOffset);
			int dx = (int) (mPointDx * positionOffset + position *  mPointDx);
			RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mIvPoint.getLayoutParams();
			params.leftMargin = dx;
			mIvPoint.setLayoutParams(params);
		}

10. Button字体颜色的Selector
	1. 在res目录下新建color文件夹
	
			<selector xmlns:android="http://schemas.android.com/apk/res/android">
	
		    <item android:state_pressed="true" android:color="@color/press"></item>
		    <item android:state_pressed="false" android:color="@color/normal"></item>
	
			</selector>
	2. 使用：android:textColor="@color/txt_guide_selector"

* SlidingMenu使用
	* 引入jar包：SlidingMenuLibrary
	* 继承 SlidingFragmentActivity
	* onCreate改成public
	* Both setBehindContentView must be called in onCreate in addition to setContentView.
		
			关于Android开发中引用slidingmenu库文件报：IllegalStateException: Both setBehindContentView must be called in onCreate in addition to setContentView.错误。
			这个错误是在一启动，还没见到界面的时候，就爆出的错误。这是因为在代码中，缺少setBehindContentView(R.layout.menu_frame)。而这个函数主要就是用来决定侧边栏长什么样的。
			public class BaseActivity extends SlidingFragmentActivity
			而这又是因为我的Activity继承了SlidingFragmentActivity,这个特殊的Activity本身就带有侧边栏，因此必须在OnCreate函数那里就调用setBehindContentView()函数来设置侧边栏的样式。
	* setBehindContentView(R.layout.fragment_menu);
	 
			SlidingMenu slidingMenu = getSlidingMenu();
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏都可以滑动
			slidingMenu.setBehindOffset(200);// 设置预留边界


* Fragment的生命周期

		public abstract class BaseFragment extends Fragment {
			
			public View mRootView;
			public Activity mActivity;
		
			// Fragment被创建
			@Override
			public void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				mActivity = getActivity();
			}
		
			// 初始化Fragment布局
			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container,
					Bundle savedInstanceState) {
				mRootView = initView();
				return mRootView;
			}
		
			abstract View initView();
		
			// activity创建结束
			@Override
			public void onActivityCreated(Bundle savedInstanceState) {
				super.onActivityCreated(savedInstanceState);
				initData();
			}
		
			public void initData() {
				
			}
		
		}

* 创建BasePager来放在Content里面作为ViewPager的内容使用

	public abstract class BasePager {
		
		public View mRootView;
		public Activity mActivity;
		public BasePager(Activity activity){
			this.mActivity = activity;
			mRootView = initView();
			// initData(); 可以不用在这里就初始化数据，当获取到网络数据之后，手动调用
		}
		
		public abstract View initView();
		public abstract void initData();
	}

* 5个页签继承BasePager

	public class HomePager extends BasePager {
	
		public HomePager(Activity activity) {
			super(activity);
		}
	
		@Override
		public View initView() {
			
			TextView tv = new TextView(mActivity);
			tv.setText(getClass().getSimpleName());
			tv.setTextSize(20);
			tv.setGravity(Gravity.CENTER);
			return tv;
		}
	
		@Override
		public void initData() {
			
		}
	
	}

* NoScrollViewPager

		public class NoScrollViewPager extends ViewPager {
		
			public NoScrollViewPager(Context context, AttributeSet attrs) {
				super(context, attrs);
			}
		
			public NoScrollViewPager(Context context) {
				super(context);
			}
		
			
			@Override
			public boolean onTouchEvent(MotionEvent arg0) {
				return true;// 重写ViewPager滑动事件, 改为什么都不做
			}
		}

* CheckBox设置Selector

		<selector xmlns:android="http://schemas.android.com/apk/res/android">
		
		    <item android:drawable="@drawable/govaffairs_press" android:state_checked="true"></item>
		    <item android:drawable="@drawable/govaffairs"></item>
		
		</selector>

* ViewPager 的initView优化
	* 只有在pager切换的时候在调用pager的initData方法，不需要预加载下一页数据

* 网络请求
* 侧边栏数据填充
	* 布局中item选中的sele

			<selector xmlns:android="http://schemas.android.com/apk/res/android">
			
			    <item android:drawable="@drawable/menu_arr_select" android:state_enabled="true"></item>
			    <item android:drawable="@drawable/menu_arr_normal"></item>
			
			</selector>


* 修改之前的BasePager


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

* 又重新按照老师的代码，重构了一遍代码，感觉自己的也能实现这样的效果

* 为了方便管理新闻中心-四大模块，现在将四大模块也用BaseMenuDetailPager封装
* 滑动困难问题
	* 父类NoScroolViewPager
		
			@Override
			public boolean onInterceptTouchEvent(MotionEvent arg0) {
				return false;
			}


* 回滑的时候会拉出来侧边栏，解决方法：
监听ViewPager，如果pos是0，则可以开启侧边栏滑动，否则取消。


* ViewPageIndicator 的使用

		# 11.ViewPagerIndicator的使用
		
		* TabPageIndicator
		* 设置主题的样式
		
		
		# 12.ViewPagerIndicator样式修改&事件处理(请求父控件不拦截)
		
		* ViewPagerIndicator样式修改
			* 修改源码的背景图片
			* 文字颜色 大小
		
		* 将监听设置给 ViewPageIndicator
		* 滑动冲突：ViewPageIndicator 有时会和侧边栏冲突
			* 解决办法：请求父控件不拦截，修改tabViewPagIndecator源码，
				* 加入方法distpathEnvet
					* getParent().requestDisallowInterceotEvent(true)



		// 此方法在viewpager设置完数据之后再调用
		mIndicator.setViewPager(mViewPager);// 将页面指示器和ViewPager关联起来
		
		// 当viewpager和指针绑定时,需要将页面切换监听设置给指针
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {


* ViewPageIndicator样式没改了
* 自定义下拉刷新的RefreshListView

 		<ProgressBar
            android:id="@+id/pb_loading"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:visibility="invisible"
            android:indeterminateDrawable="@drawable/custom_progress" />


		
		<?xml version="1.0" encoding="utf-8"?>
		<rotate xmlns:android="http://schemas.android.com/apk/res/android"
		    android:fromDegrees="0"
		    android:pivotX="50%"
		    android:pivotY="50%"
		    android:toDegrees="360" >
		
		    <shape
		        android:innerRadius="15dp"
		        android:shape="ring"
		        android:thickness="3dp"
		        android:useLevel="false" >
		        <gradient
		            android:centerColor="#5f00"
		            android:endColor="#fff"
		            android:startColor="#f00"
		            android:type="sweep" />
		    </shape>
		
		</rotate>

*  隐藏HeaderView

		// 先隐藏
		headerView.measure(0, 0);
		int height = headerView.getMeasuredHeight();
		headerView.setPadding(0, -height, 0, 0);

* 请求父控件不要拦截
	
		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			getParent().requestDisallowInterceptTouchEvent(true);
			return super.dispatchTouchEvent(ev);
		}

* 下拉刷新


		@Override
		public boolean onTouchEvent(MotionEvent ev) {
	
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = (int) ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				int endY = (int) ev.getY();
				int dy = endY - startY;
				if (startY == -1) {// 如果用户按住头条新闻向下滑动, 会导致listview无法拿到ACTION_DOWN,
					// 此时要重新获取startY
					startY = (int) ev.getY();
				}
				if (dy > 0 && getFirstVisiblePosition() == 0) {
					L.v("---" + startY + "---" + endY);
					// mHeaderheight = headerView.getMeasuredHeight(); 不能这么写
					int padding = -mHeaderheight + dy;
					if(padding>0 && mCurrentState != STATE_RELEASE_TO_REFRESH){
						mCurrentState = STATE_RELEASE_TO_REFRESH;
					}else if(padding<0 && mCurrentState != STATE_PULL_TO_REFRESH){
						mCurrentState = STATE_PULL_TO_REFRESH;
					}
					refreshState();
					headerView.setPadding(0,padding , 0, 0);
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
			case STATE_RELEASE_TO_REFRESH:
				tvTitle.setText("松开刷新");
				break;
			case STATE_PULL_TO_REFRESH:
				tvTitle.setText("下拉刷新");
				break;
			case STATE_REFRESHING:
				tvTitle.setText("正在刷新");
				break;
	
			}
		}


* DetailNewsViewPager的触摸事件修正

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


* 加一个脚布局
* WebView的使用
	* 缩放
		* 双击缩放
	* 默认不支持js的
		* 打开js
	* 设置监听
		* setWebViewClient
			* onPagerStart
			* onFinish
			* shouldOverrideUrlLoading
				* 可以获取特定url链接
	
	* PhoneGap
		* js和本地代码互动
	
	* setWebChromeClient
		* onProcessChanged
		* onReceiverdTitle

* 分享
	* Mob

* 组图