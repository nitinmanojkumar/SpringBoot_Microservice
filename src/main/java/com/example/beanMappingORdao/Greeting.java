package com.example.beanMappingORdao;

import org.springframework.stereotype.Component;

@Component
public class Greeting {

	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
