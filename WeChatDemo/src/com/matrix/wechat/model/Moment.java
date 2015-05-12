package com.matrix.wechat.model;

public class Moment {
	private String picture;// 发布者的头像
	private String userName; // 发布者的用户名
	private String content_text;// 发布的内容
	private String date; // 发表时间

	public Moment() {
	}

	public Moment(String picture, String userName, String content_text,
			String date) {
		super();
		this.picture = picture;
		this.userName = userName;
		this.content_text = content_text;
		this.date = date;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent_text() {
		return content_text;
	}

	public void setContent_text(String content_text) {
		this.content_text = content_text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
