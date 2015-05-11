package com.matrix.wechat.model;

public class User {
	private long userid;// 编号
	private String username;// 用户名
	private String nickname;// 昵称
	private String picture;// 头像
	private String password;// 密码
	private int status;// 登陆状态0（未登录）,1

	public User() {
		super();
	}

	public User(long userid, String username, String nickname, String picture,
			int status) {
		super();
		this.userid = userid;
		this.username = username;
		this.nickname = nickname;
		this.picture = picture;
		this.status = status;
	}

	public User(long userid, String username, String nickname, String picture,
			String password, int status) {
		super();
		this.userid = userid;
		this.username = username;
		this.nickname = nickname;
		this.picture = picture;
		this.password = password;
		this.status = status;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [userid=" + userid + ", username=" + username
				+ ", nickname=" + nickname + ", picture=" + picture
				+ ", status=" + status + "]";
	}

}
