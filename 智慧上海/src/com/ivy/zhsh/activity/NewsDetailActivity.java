package com.ivy.zhsh.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ivy.zhsh.R;
import com.ivy.zhsh.utils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 新闻详情页面
 * 
 * @author Kevin
 * @date 2015-8-14
 */
public class NewsDetailActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.ll_controller)
	private LinearLayout llController;
	@ViewInject(R.id.btn_back)
	private ImageButton btnBack;
	@ViewInject(R.id.btn_menu)
	private ImageButton btnMenu;
	@ViewInject(R.id.btn_textsize)
	private ImageButton btnTextSize;
	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;
	@ViewInject(R.id.wv_webview)
	private WebView mWebView;
	@ViewInject(R.id.pb_loading)
	private ProgressBar pbLoading;
	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		ViewUtils.inject(this);

		btnBack.setVisibility(View.VISIBLE);
		btnMenu.setVisibility(View.GONE);
		llController.setVisibility(View.VISIBLE);

		btnBack.setOnClickListener(this);
		btnTextSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);

		mUrl = getIntent().getStringExtra("url");

		
		mUrl = mUrl.replace(":8080", ":80");
		// 加载网页
		mWebView.loadUrl(mUrl);

		WebSettings settings = mWebView.getSettings();
		settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
		settings.setUseWideViewPort(true);// 只是双击缩放
		settings.setJavaScriptEnabled(true);// 打开js功能

		mWebView.setWebViewClient(new WebViewClient() {
			// 网页开始加载
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("网页开始加载");
				pbLoading.setVisibility(View.VISIBLE);
			}

			// 网页跳转
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// <a href="tel:110">联系我们</a>
				// PhoneGap(js和本地代码互动)
				System.out.println("网页跳转:" + url);
				view.loadUrl(url);// 强制在当前页面加载网页, 不用跳浏览器
				return true;
			}

			// 网页加载结束
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("网页加载结束");
				pbLoading.setVisibility(View.GONE);
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {

			// 加载进度回调
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				System.out.println("newProgress:" + newProgress);
			}

			// 网页标题
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				System.out.println("title:" + title);
			}

		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_textsize:
			showChooseDialog();
			break;
		case R.id.btn_share:
			showShare();
			break;

		default:
			break;
		}
	}

	// 点击确定前, 用户选择的字体大小的位置
	private int mChooseItem;
	// 当前的字体位置
	private int mSelectItem = 2;

	/**
	 * 选择字体大小的弹窗
	 */
	private void showChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("字体设置");
		String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
				"超小号字体" };
		builder.setSingleChoiceItems(items, mSelectItem,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mChooseItem = which;
					}
				});

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				WebSettings settings = mWebView.getSettings();

				switch (mChooseItem) {
				case 0:
					settings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					settings.setTextSize(TextSize.SMALLEST);
					break;

				default:
					break;
				}

				mSelectItem = mChooseItem;
			}
		});

		builder.setNegativeButton("取消", null);
		builder.show();
	}

	/**
	 * 确保SDcard下面存在此张图片
	 */
	private void showShare() {
		
		ToastUtils.showToast(this, "功能暂未实现");
		
	}
}
