package com.github.ulwx.aka.fileserver.utils;

import com.ulwx.tool.StringUtils;

public class PicPathUtil {

	public static String ROOT_IMG = "imgs";

	public static String getSmallPic(String bigUrl) {

		String url = bigUrl;
		int lastPoint=bigUrl.lastIndexOf(".");
		url = url.substring(0, lastPoint);
		url = url + "_small" + "." + bigUrl.substring(lastPoint+1);
		String smallPicUrl = url;
		return smallPicUrl;
	}

	public static String getRelativePic(String url) {
		if (StringUtils.hasText(url)) {
			try {
				int imgs = url.lastIndexOf(ROOT_IMG);
				return url.substring(imgs, url.length());
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getSmallPic("http://192.168.4.201:8880/greenshoot-inter/imgs/touxiang.jpg"));
	}

}
