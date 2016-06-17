package com.ivy.zhsh.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ivy.zhsh.R;
import com.ivy.zhsh.activity.MainActivity;
import com.ivy.zhsh.base.BaseFragment;
import com.ivy.zhsh.domain.Category.Menu;

public class LeftFragment extends BaseFragment {

	private ListView mLvMenu;
	private ArrayList<Menu> data;
	public int mCurrrentPos;

	@Override
	public View initView() {
		View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_menu, null);
		mLvMenu = (ListView) view.findViewById(R.id.lv_menu);
		return view;
	}
	
	public void setData(ArrayList<Menu> data){
		this.data = data;
		mCurrrentPos = 0;
		final MenuAdapter adapter = new MenuAdapter();
		mLvMenu.setAdapter(adapter);
		
		mLvMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				mCurrrentPos = position;
				adapter.notifyDataSetChanged();
				toggle(mCurrrentPos);
			}
		});
	}
	
	
	protected void toggle(int pos) {
		MainActivity mainActivty = (MainActivity) mActivity;
		mainActivty.toggle();
		
		// 改变新闻的Detail的页面
		mainActivty.changeNewDetail(pos);
	}


	class MenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Menu getItem(int position) {
			Menu menu = data.get(position);
			return menu;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.list_item_left_menu,
					null);
			TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);

			Menu data = getItem(position);
			tvMenu.setText(data.title);

			if (mCurrrentPos == position) {
				// 如果当前要绘制的item刚好是被选中的, 需要设置为红色
				tvMenu.setEnabled(true);
			} else {
				// 其他item都是白色
				tvMenu.setEnabled(false);
			}

			return view;
		}

	}
}
