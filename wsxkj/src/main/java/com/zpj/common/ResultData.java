package com.zpj.common;

public class ResultData<T> implements java.io.Serializable{
	
	private String msg;
	private boolean code;
	private T data;
	
	public ResultData(){
		
	}
	
	public ResultData(T data,String msg,boolean code) {
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
	
	public boolean isCode() {
		return code;
	}

	public void setCode(boolean code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	
	
}
