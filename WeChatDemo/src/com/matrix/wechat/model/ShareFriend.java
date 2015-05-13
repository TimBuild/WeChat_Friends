package com.matrix.wechat.model;

import java.sql.Timestamp;

public class ShareFriend {
	private Integer shareid;
	private Integer sharefrom;
	private String content;
	private String date;

	public ShareFriend() {
		super();
	}

	public ShareFriend(Integer shareid, Integer sharefrom, String content,
			String date) {
		super();
		this.shareid = shareid;
		this.sharefrom = sharefrom;
		this.content = content;
		this.date = date;
	}

	public Integer getShareid() {
		return shareid;
	}

	public void setShareid(Integer shareid) {
		this.shareid = shareid;
	}

	public Integer getSharefrom() {
		return sharefrom;
	}

	public void setSharefrom(Integer sharefrom) {
		this.sharefrom = sharefrom;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ShareFriend [shareid=" + shareid + ", sharefrom=" + sharefrom
				+ ", content=" + content + ", date=" + date + "]";
	}

}
