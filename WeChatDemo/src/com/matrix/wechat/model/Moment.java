package com.matrix.wechat.model;

import java.util.List;

public class Moment {
	private int momentid; // 发布内容的id
	private String picture;// 发布者的头像
	private String userName; // 发布者的用户名
	private String content_text;// 发布的内容
	private String date; // 发表时间
	private List<Comment> commentsList; // 该内容的所有评论

	public Moment() {
	}

	public Moment(int momentid, String picture, String userName,
			String content_text, String date, List<Comment> commentsList) {
		super();
		this.momentid = momentid;
		this.picture = picture;
		this.userName = userName;
		this.content_text = content_text;
		this.date = date;
		this.commentsList = commentsList;
	}

	public int getMomentid() {
		return momentid;
	}

	public void setMomentid(int momentid) {
		this.momentid = momentid;
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

	public List<Comment> getCommentsList() {
		return commentsList;
	}

	public void setCommentsList(List<Comment> commentsList) {
		this.commentsList = commentsList;
	}

	@Override
	public String toString() {
		return "Moment [momentid=" + momentid + ", picture=" + picture
				+ ", userName=" + userName + ", content_text=" + content_text
				+ ", date=" + date + ", commentsList=" + commentsList + "]";
	}

}
