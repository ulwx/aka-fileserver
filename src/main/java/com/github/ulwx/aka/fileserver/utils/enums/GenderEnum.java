package com.github.ulwx.aka.fileserver.utils.enums;

import com.ulwx.tool.StringUtils;

public enum GenderEnum {
	man("男", 1), woman("女", 2), unknown("未知", 0);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private GenderEnum(String name, int index) {  
	        this.name = name;  
	        this.index = index;  
	    }

	// 普通方法
	public static String getName(int index) {
		for (GenderEnum c : GenderEnum.values()) {
			if (c.getIndex() == index) {
				return c.name;
			}
		}
		return null;
	}
	
	public static Integer getIndex(String gender) {
		if(StringUtils.hasText(gender)){
			for (GenderEnum c : GenderEnum.values()) {
				if (c.getName() .equals(gender.trim())) {
					return c.index;
				}
			}
		}
		return 0;////
	}

	// get set 方法
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public static void main(String[] args) {
		System.out.println(GenderEnum.getName(1));
		System.out.println(GenderEnum.getIndex("女"));
	}
}
