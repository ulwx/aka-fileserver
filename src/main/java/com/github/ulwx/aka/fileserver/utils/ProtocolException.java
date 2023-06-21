package com.github.ulwx.aka.fileserver.utils;


/**
 *   自定义异常
 * @author Administrator
 *
 */
public class ProtocolException extends RuntimeException {

	private int code;

	public ProtocolException(int exceptionCode,String message) {
		super(ERRS.get(exceptionCode,message));
		this.code=exceptionCode;
	}
	
	public ProtocolException(int exceptionCode) {
		super(ERRS.get(exceptionCode));
		this.code=exceptionCode;
	}
	public ProtocolException(String message) {
		super(ERRS.get(ERRS.COMMON_ERROR,message));
		this.code=ERRS.COMMON_ERROR;
	}	
	
}
