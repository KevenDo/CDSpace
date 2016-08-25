package com.u51.CDSpace.model;

/*
 * Author 杜亮亮
 * 2016.3.21
 */
public class Header {
	private String name;
	
	private String value;
	
	public Header(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
