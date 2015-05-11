package com.matrix.wechat.model;

import java.io.Serializable;

/**
 * chat history message
 */
public class ChatHistoryMessage implements Serializable {

	private static final long serialVersionUID = 8348434195997405794L;
	private Integer messageId;
	private String picture;// 发送人头像(私聊)
	private Integer userIdFrom;// 谁发的
	private Integer userIdTo;// 接收人
	private String content;// 发送信息
	private String date;// 日期
	private String status;// 发送状态
	private String ownerName = null;// 群聊谁发送的

	public ChatHistoryMessage(Integer messageId, String picture,
			Integer userIdFrom, Integer userIdTo, String content, String date,
			String status) {
		super();
		this.messageId = messageId;
		this.picture = picture;
		this.userIdFrom = userIdFrom;
		this.userIdTo = userIdTo;
		this.content = content;
		this.date = date;
		this.status = status;
	}

	public ChatHistoryMessage() {
		super();
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getUserIdFrom() {
		return userIdFrom;
	}

	public void setUserIdFrom(Integer userIdFrom) {
		this.userIdFrom = userIdFrom;
	}

	public Integer getUserIdTo() {
		return userIdTo;
	}

	public void setUserIdTo(Integer userIdTo) {
		this.userIdTo = userIdTo;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public ChatHistoryMessage(Integer messageId, String picture,
			Integer userIdFrom, Integer userIdTo, String content, String date,
			String status, String ownerName) {
		super();
		this.messageId = messageId;
		this.picture = picture;
		this.userIdFrom = userIdFrom;
		this.userIdTo = userIdTo;
		this.content = content;
		this.date = date;
		this.status = status;
		this.ownerName = ownerName;
	}

	@Override
	public String toString() {
		return "ChatHistoryMessage [messageId=" + messageId + ", picture="
				+ picture + ", userIdFrom=" + userIdFrom + ", userIdTo="
				+ userIdTo + ", content=" + content + ", date=" + date
				+ ", status=" + status + "]";
	}

}
