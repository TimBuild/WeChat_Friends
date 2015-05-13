package com.matrix.wechat.model;

import java.sql.Timestamp;

public class ShareComment {
	private Integer commentid;
	private Integer shareid;
	private Integer fromid;
	private Integer toid;
	private String content;
	private String date;
	
	

	public ShareComment() {
		super();
	}

	public ShareComment(Integer commentid, Integer shareid, Integer fromid,
			Integer toid, String content, String date) {
		super();
		this.commentid = commentid;
		this.shareid = shareid;
		this.fromid = fromid;
		this.toid = toid;
		this.content = content;
		this.date = date;
	}

	public Integer getCommentid() {
		return commentid;
	}

	public void setCommentid(Integer commentid) {
		this.commentid = commentid;
	}

	public Integer getShareid() {
		return shareid;
	}

	public void setShareid(Integer shareid) {
		this.shareid = shareid;
	}

	public Integer getFromid() {
		return fromid;
	}

	public void setFromid(Integer fromid) {
		this.fromid = fromid;
	}

	public Integer getToid() {
		return toid;
	}

	public void setToid(Integer toid) {
		this.toid = toid;
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
		return "ShareComment [commentid=" + commentid + ", shareid=" + shareid
				+ ", fromid=" + fromid + ", toid=" + toid + ", content="
				+ content + ", date=" + date + "]";
	}

}
