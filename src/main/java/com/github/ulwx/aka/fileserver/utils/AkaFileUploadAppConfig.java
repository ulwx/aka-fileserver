package com.github.ulwx.aka.fileserver.utils;

import com.github.ulwx.aka.fileserver.protocol.utils.AkaFileUploadSetting;
import com.github.ulwx.aka.webmvc.BeanGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Iren08
 * @date 2017年3月8日 下午1:17:25
 * @version 1.0
 */
public final class AkaFileUploadAppConfig {

	private static final Logger log = LoggerFactory.getLogger(AkaFileUploadAppConfig.class);

	public static String getUploadDir() {
		return BeanGet.getBean(AkaFileUploadSetting.class).getUploadDir();
	}

	public static String getHttpPrefix() {
		return BeanGet.getBean(AkaFileUploadSetting.class).getHttpPrefix();
	}

	public static String getOssHttpPrefix() {
		return BeanGet.getBean(AkaFileUploadSetting.class).getOssHttpPrefix();
	}



}
