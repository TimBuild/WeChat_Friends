package com.matrix.wechat.model;

import java.sql.Timestamp;

public class ShareFriend {
	private Integer shareid;
	private Integer sharefrom;
	private Integer type;// 1是文字，2是图片
	private String img_url;
	private String content;
	private String date;

	public ShareFriend() {
		super();
	}

	public ShareFriend(Integer shareid, Integer sharefrom, Integer type,
			String img_url, String content, String date) {
		super();
		this.shareid = shareid;
		this.sharefrom = sharefrom;
		this.type = type;
		this.img_url = img_url;
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

	@Override
	public String toString() {
		return "ShareFriend [shareid=" + shareid + ", sharefrom=" + sharefrom
				+ ", type=" + type + ", img_url=" + img_url + ", content="
				+ content + ", date=" + date + "]";
	}

}
