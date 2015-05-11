package com.matrix.wechat.model;

import java.io.Serializable;

/**
 * 聊天列表 record chat history contact info
 */
public class ChatHistoryContact implements Serializable {
	private static final long serialVersionUID = -3148729169970182934L;
	private Integer userid;
	private String nickname;
	private String username;
	private String picture;
	private String message;
	private String date;
	private boolean isGroup = false;
	private String groupName = "";

	public ChatHistoryContact() {
		super();
	}

	public ChatHistoryContact(Integer userid, String nickname, String username,
			String picture, String message, String date, boolean isGroup,
			String groupName) {
		super();
		this.userid = userid;
		this.nickname = nickname;
		this.username = username;
		this.picture = picture;
		this.message = message;
		this.date = date;
		this.isGroup = isGroup;
		this.groupName = groupName;
	}

	public ChatHistoryContact(Integer userid, String nickname, String username,
			String picture, String message, String date) {
		super();
		this.userid = userid;
		this.nickname = nickname;
		this.username = username;
		this.picture = picture;
		this.message = message;
		this.date = date;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "ChatHistoryContact [userid=" + userid + ", nickname="
				+ nickname + ", username=" + username + ", picture=" + picture
				+ ", message=" + message + ", date=" + date + "]";
	}

}
