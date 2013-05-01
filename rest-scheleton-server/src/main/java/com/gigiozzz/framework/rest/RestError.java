package com.gigiozzz.framework.rest;

import java.util.List;

import org.springframework.http.HttpStatus;

public class RestError {

    private final HttpStatus status;
    private final int code;
    private final String errorMsg;
	private List<ErrorMsg> errors;

    public RestError(HttpStatus s,int c,String m){
    	status=s;
    	code=c;
    	errorMsg=m;
    }

    public RestError(HttpStatus s,int c,String m,List<ErrorMsg> e){
    	status=s;
    	code=c;
    	errorMsg=m;
    	errors=e;
    }
    
    public HttpStatus getStatus() {
		return status;
	}
	public int getCode() {
		return code;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
    
	
	public List<ErrorMsg> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorMsg> errors) {
		this.errors = errors;
	}
    
}