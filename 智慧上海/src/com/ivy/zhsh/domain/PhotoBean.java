package com.ivy.zhsh.domain;

import java.util.ArrayList;

/**
 * 组图对象封装
 * 
 * @author Kevin
 * @date 2015-8-14
 */
public class PhotoBean {
	public int retcode;
	public PhotoData data;

	public class PhotoData {
		public ArrayList<PhotoNewsData> news;
	}

	public class PhotoNewsData {
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String url;
	}
}
