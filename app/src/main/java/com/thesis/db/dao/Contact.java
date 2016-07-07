package com.thesis.db.dao;

public class Contact {
	private String name;
	private String key;

	public Contact() {
		super();
	}

	public Contact(String name, String key) {
		super();
		this.name = name;
		this.key = key;
	}
	
	public Contact(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "Contact [name=" + name + ", key=" + key + "]";
	}
	
	public String toStringNameOnly() {
		return "" + name +"";
	}
}
