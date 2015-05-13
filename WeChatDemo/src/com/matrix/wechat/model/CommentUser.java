package com.matrix.wechat.model;


public class CommentUser {
	private String from_userName;
	private String from_nickName;
	private String to_userName;
	private String to_nickName;

	public String getFrom_userName() {
		return from_userName;
	}

	public void setFrom_userName(String from_userName) {
		this.from_userName = from_userName;
	}

	public String getFrom_nickName() {
		return from_nickName;
	}

	public void setFrom_nickName(String from_nickName) {
		this.from_nickName = from_nickName;
	}

	public String getTo_userName() {
		return to_userName;
	}

	public void setTo_userName(String to_userName) {
		this.to_userName = to_userName;
	}

	public String getTo_nickName() {
		return to_nickName;
	}

	public void setTo_nickName(String to_nickName) {
		this.to_nickName = to_nickName;
	}

	public CommentUser(String from_userName, String from_nickName,
			String to_userName, String to_nickName) {
		super();
		this.from_userName = from_userName;
		this.from_nickName = from_nickName;
		this.to_userName = to_userName;
		this.to_nickName = to_nickName;
	}

	public CommentUser() {
		super();
	}

	@Override
	public String toString() {
		return "CommentUser [from_userName=" + from_userName
				+ ", from_nickName=" + from_nickName + ", to_userName="
				+ to_userName + ", to_nickName=" + to_nickName + "]";
	}

}
