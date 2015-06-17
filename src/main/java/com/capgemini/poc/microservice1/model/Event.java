package com.capgemini.poc.microservice1.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Event 
{
	private String event_id;
	private Map<String, Object> data = new HashMap<String, Object>();

	public Event(String event_id) {
		this.event_id = event_id;
	}
	
	public void addData(String key, Object value) {
		this.data.put(key, value);
	}
	
	public String getEvent_id() {
		return event_id;
	}
	
	public Map<String, Object> getData() {
		return data;
	}
	
	public String toJSON() throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}
}
