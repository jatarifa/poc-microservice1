package com.capgemini.poc.microservice1.model;

public class Password 
{
	private int key;
	private String password;
	
	public Password(String password, int key) 
	{
		this.key = key;
		this.password = password;
	}

	public int getKey() {
		return key;
	}
	
	public String getPassword() {
		return password;
	}
}

