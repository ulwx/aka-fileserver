package com.github.ulwx.aka.fileserver.utils;


import com.github.ulwx.aka.dbutils.database.sql.SqlUtils;

public class DataBaseToJavaBean {

	public static void main(String[] args) {
		////System.out.println(Path.getClassPath());
		SqlUtils.exportTables("jyd2", "jyd2", "d:/ok4/jyd2", "com.jydsoft.domain.db.jyd2","utf-8",true);
	
	}
}