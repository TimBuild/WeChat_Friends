package com.matrix.wechat.model;


public class FriendRequest {
	private long requestid;//编号
	private long userid;//申请加好友的id
	private String nickname;//昵称
	private Long date;//日期
	private int status;//状态

	public FriendRequest() {
		super();
	}

	public FriendRequest(long requestid, long userid, String nickname,
			Long date, int status) {
		super();
		this.requestid = requestid;
		this.userid = userid;
		this.nickname = nickname;
		this.date = date;
		this.status = status;
	}

	public long getRequestid() {
		return requestid;
	}

	public void setRequestid(long requestid) {
		this.requestid = requestid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "FriendRequest [requestid=" + requestid + ", userid=" + userid
				+ ", nickname=" + nickname + ", date=" + date + ", status="
				+ status + "]";
	}

}
