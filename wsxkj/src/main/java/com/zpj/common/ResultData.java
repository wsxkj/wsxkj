package com.zpj.common;

public class ResultData<T> implements java.io.Serializable{
	
	private String msg;
	private int code;
	private T data;
	private int count;
	
	public ResultData(){
		
	}
	
	public ResultData(T data,String msg,int code) {
		this.data=data;
		this.msg=msg;
		this.code=code;
	}
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	


	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}




	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
