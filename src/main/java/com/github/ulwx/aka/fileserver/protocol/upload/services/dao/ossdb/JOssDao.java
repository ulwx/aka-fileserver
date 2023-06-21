package com.github.ulwx.aka.fileserver.protocol.upload.services.dao.ossdb;

import com.github.ulwx.aka.dbutils.spring.multids.AkaDS;
import com.github.ulwx.aka.fileserver.protocol.upload.services.domain.JOss;
import com.github.ulwx.aka.webmvc.AkaDaoSupport;
import com.ulwx.tool.CTime;

import java.util.Date;

@AkaDS("${aka.file-server.ds}")
public class JOssDao extends AkaDaoSupport {
	private volatile Date lastDate=CTime.addMinutes(60);
	private volatile JOss cache;

	public   JOss get() throws Exception{
		synchronized (this){
			if(CTime.getDate().after(lastDate)){
				JOss one=new JOss();
				one.setStatus(1);
				JOss ret= this.getTemplate().queryOneBy(one);
				if(ret==null) {
					throw new RuntimeException("查询失败！");
				}else{
					cache=ret;
					lastDate=CTime.addMinutes(60);
				}
			}
		}

		return cache;
		
	}

}
