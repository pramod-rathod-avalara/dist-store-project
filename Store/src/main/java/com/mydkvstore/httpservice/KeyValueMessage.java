package com.mydkvstore.httpservice;

public class KeyValueMessage {
	
	private String key;
	private String value;
	private String action;
	
	public KeyValueMessage() {
	}
	
	public KeyValueMessage(String action, String key, String value) {
		this.action = action;
		this.key = key;
		this.value = value;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	

}
