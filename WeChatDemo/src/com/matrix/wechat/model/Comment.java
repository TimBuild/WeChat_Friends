package com.matrix.wechat.model;

public class Comment {

	private String sharetoname;
	private String sharefromname;
	private String content; // 回复的内容
	private Integer sharetoid;
	private Integer sharefromid;

	public Comment() {
	}

	public Comment(String sharetoname, String sharefromname, String content,
			Integer sharetoid, Integer sharefromid) {
		super();
		this.sharetoname = sharetoname;
		this.sharefromname = sharefromname;
		this.content = content;
		this.sharetoid = sharetoid;
		this.sharefromid = sharefromid;
	}

	public String getSharetoname() {
		return sharetoname;
	}

	public void setSharetoname(String sharetoname) {
		this.sharetoname = sharetoname;
	}

	public String getSharefromname() {
		return sharefromname;
	}

	public void setSharefromname(String sharefromname) {
		this.sharefromname = sharefromname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSharetoid() {
		return sharetoid;
	}

	public void setSharetoid(Integer sharetoid) {
		this.sharetoid = sharetoid;
	}

	public Integer getSharefromid() {
		return sharefromid;
	}

	public void setSharefromid(Integer sharefromid) {
		this.sharefromid = sharefromid;
	}

	@Override
	public String toString() {
		return "Comment [sharetoname=" + sharetoname + ", sharefromname="
				+ sharefromname + ", content=" + content + ", sharetoid="
				+ sharetoid + ", sharefromid=" + sharefromid + "]";
	}

}
