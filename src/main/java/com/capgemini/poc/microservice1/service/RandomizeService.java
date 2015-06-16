package com.capgemini.poc.microservice1.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class RandomizeService 
{
	public String getRandomString()
	{
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0,5);
	}
}
