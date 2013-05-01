package com.gigiozzz.framework.rest;

public class ErrorMsg {
	
	private String field;
	private String errorMsg;
	
	public ErrorMsg(String f,String e){
		field=f;
		errorMsg=e;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
