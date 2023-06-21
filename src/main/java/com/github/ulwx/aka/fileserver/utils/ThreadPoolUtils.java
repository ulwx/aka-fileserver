package com.github.ulwx.aka.fileserver.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtils {
	public static ExecutorService pool = Executors.newFixedThreadPool(4);
	
	public static void init() {
		// pool = Executors.newFixedThreadPool(4);
	}
	
	public static void shutdown() {
		if(pool!=null) {
			pool.shutdown();
			pool=null;
		}
	}
}
