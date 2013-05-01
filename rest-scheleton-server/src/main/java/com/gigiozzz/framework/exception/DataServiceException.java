package com.gigiozzz.framework.exception;

public class DataServiceException extends RuntimeException {

	private static final long serialVersionUID = 5989511448218124416L;

	public DataServiceException(){
		super();
	}

	public DataServiceException(String msg){
		super(msg);
	}
}
