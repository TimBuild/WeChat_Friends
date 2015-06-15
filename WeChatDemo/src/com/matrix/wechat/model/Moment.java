package com.matrix.wechat.model;

import java.util.ArrayList;
import java.util.List;

public class Moment {
	private int momentid; // 发布内容的id
	private String picture;// 发布者的头像
	private String userName; // 发布者的用户名
	private String content_text;// 发布的内容

	private Integer type;// 1为文字，2为图片
	private String img_url;// 图片地址
	private String voice_url;//声音地址
	private String date; // 发表时间
	private List<Comment> commentsList; // 该内容的所有评论

	public Moment() {
	}

	public Moment(int momentid, String picture, String userName,
			String content_text, Integer type, String img_url, String voic_url,
			String date, List<Comment> commentsList) {
		super();
		this.momentid = momentid;
		this.picture = picture;
		this.userName = userName;
		this.content_text = content_text;
		this.type = type;
		this.img_url = img_url;
		this.voice_url = voic_url;
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
		if (commentsList == null) {
			commentsList = new ArrayList<Comment>();
		}
		return commentsList;
	}

	public void setCommentsList(List<Comment> commentsList) {
		this.commentsList = commentsList;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getVoic_url() {
		return voice_url;
	}

	public void setVoic_url(String voice_url) {
		this.voice_url = voice_url;
	}

	@Override
	public String toString() {
		return "Moment [momentid=" + momentid + ", picture=" + picture
				+ ", userName=" + userName + ", content_text=" + content_text
				+ ", type=" + type + ", img_url=" + img_url + ", voic_url="
				+ voice_url + ", date=" + date + ", commentsList="
				+ commentsList + "]";
	}

}
