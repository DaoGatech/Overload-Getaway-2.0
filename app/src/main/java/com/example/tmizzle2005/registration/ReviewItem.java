package com.example.tmizzle2005.registration;

public class ReviewItem {
	private String serial;
	private String name;
	private String remaining;
	public ReviewItem(String serial, String name, String remaining) {
		this.serial = serial;
		this.name = name;
		this.remaining = remaining;
	}
	
	public String getserial() {
		return serial;
	}
	
	public String getname() {
		return name;
	}
	public String getRemaining() {
		return remaining;
	}
	
		

}
