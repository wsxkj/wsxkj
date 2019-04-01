package com.zpj.common;

import java.util.List;


    /**
    * @ClassName: MyPage
    * @Description: TODO(这里用一句话描述这个类的作用)
    * @author zpj
    * @date 2018年4月11日
    *
    */
    
public class MyPage {
	private int code=0;
	private String msg="";
	public List<?> data;
	
	public int count;

	
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	

	
	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public MyPage(){
		
	}
	public MyPage(List<?> data, int count) {
		super();
		this.data = data;
		this.count = count;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
