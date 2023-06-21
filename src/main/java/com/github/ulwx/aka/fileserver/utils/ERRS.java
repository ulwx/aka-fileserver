package com.github.ulwx.aka.fileserver.utils;

import com.github.ulwx.aka.frame.protocol.utils.IError;

public class ERRS extends IError{

	public static int BG_ERROR=9998;
	public static int UNKNOW_ERROR=8000;

	static{
		errors.put(UNKNOW_ERROR, "服务器处理失败！请联系客服处理！");
		errors.put(BG_ERROR, "后台处理失败!");
	}


	public static void main(String[] args) {
	}

}
