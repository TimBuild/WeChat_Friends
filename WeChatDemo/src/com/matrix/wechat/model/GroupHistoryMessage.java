package com.matrix.wechat.model;

public class GroupHistoryMessage {
	private int messageid;// 消息编号
	private String message;// 消息
	private int senduser;// 谁发送的
	private String sendtime;// 发送日期
	private String nickname;// 昵称
	private String picture;// 头像

	public GroupHistoryMessage() {
		super();
	}

	public GroupHistoryMessage(int messageid, String message, int senduser,
			String sendtime, String nickname, String picture) {
		super();
		this.messageid = messageid;
		this.message = message;
		this.senduser = senduser;
		this.sendtime = sendtime;
		this.nickname = nickname;
		this.picture = picture;
	}

	public int getMessageid() {
		return messageid;
	}

	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getSenduser() {
		return senduser;
	}

	public void setSenduser(int senduser) {
		this.senduser = senduser;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
