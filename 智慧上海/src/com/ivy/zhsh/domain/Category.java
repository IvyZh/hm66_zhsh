package com.ivy.zhsh.domain;

import java.util.ArrayList;

public class Category {

	public int retcode;
	public ArrayList<Menu> data;
	
	public class Menu{
		public int id;
		public String title;
		public ArrayList<Children> children;
		
		public class Children{
			public int id;
			public String title;
			public int type;
			public String url;
		}
	}

	@Override
	public String toString() {
		return "Category [retcode=" + retcode + ", data=" + data + "]";
	}
	
	
}
