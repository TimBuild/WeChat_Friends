package com.matrix.wechat.model;

public class Comment {

	private String username_reply; //评论的人
	private String content; //回复的内容
	private long sharetoid;
	private Integer sharefromid;
	
	public Comment(){}
	
	public Comment(String username_reply,String content,long sharetoid,Integer sharefromid){
		this.username_reply=username_reply;
		this.content=content;
		this.sharetoid=sharetoid;
		this.sharefromid=sharefromid;
	}
	
	public String getUsername_reply() {
		return username_reply;
	}
	
	public void setUsername_reply(String username_reply) {
		this.username_reply = username_reply;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public long getShareid() {
		return sharetoid;
	}

	public void setSharetoid(long sharetoid) {
		this.sharetoid = sharetoid;
	}

	public Integer getSharefromid() {
		return sharefromid;
	}

	public void setSharefromid(Integer sharefromid) {
		this.sharefromid = sharefromid;
	}	
}
