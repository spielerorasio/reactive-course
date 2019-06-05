package com.example.reactive.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Todo {
	
	@Id
	private String id;
	private int daysLeft;
	private String desc;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getDaysLeft() {
		return daysLeft;
	}
	public void setDaysLeft(int daysLeft) {
		this.daysLeft = daysLeft;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
