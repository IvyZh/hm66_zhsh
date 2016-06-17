package com.ivy.zhsh.activity;

import com.ivy.zhsh.R;
import com.ivy.zhsh.R.id;
import com.ivy.zhsh.R.layout;
import com.ivy.zhsh.utils.L;
import com.ivy.zhsh.utils.SharedPreUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public class SplashActivity extends Activity {

    private View rlRoot;
	private AnimationSet as;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        rlRoot = findViewById(R.id.rl_root);
        
        initAnimation();
        
        rlRoot.startAnimation(as);
        
        
        as.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				L.v("--动画执行结束---");
				if(SharedPreUtils.getBoolean(getApplicationContext(), "is_guide")){// 进入主界面
					startActivity(new Intent(SplashActivity.this,MainActivity.class));
				}else{// 进入引导页面
					startActivity(new Intent(SplashActivity.this,GuideActivity.class));
				}
				finish();
			}
		});
    }

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		as = new AnimationSet(false);
		
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		
		as.addAnimation(aa);
		as.addAnimation(ra);
		as.addAnimation(sa);
		
		as.setDuration(500);
	}
}
