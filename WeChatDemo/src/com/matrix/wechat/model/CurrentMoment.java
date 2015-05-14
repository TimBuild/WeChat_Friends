package com.matrix.wechat.model;

public class CurrentMoment {
	private String context;
	private String date;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public CurrentMoment() {
		super();
	}

	public CurrentMoment(String context, String date) {
		super();
		this.context = context;
		this.date = date;
	}

	@Override
	public String toString() {
		return "CurrentMoment [context=" + context + ", date=" + date + "]";
	}

}
