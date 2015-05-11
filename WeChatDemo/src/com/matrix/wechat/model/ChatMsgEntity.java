package com.matrix.wechat.model;

public class ChatMsgEntity {
	private static final String TAG = ChatMsgEntity.class.getSimpleName();
	private String name;// 谁发的
	private String date;// 日期
	private String text;// 内容
	private boolean isComMeg = true;// 是接收的还是发送的
	private boolean isGroup = false;// 是否群聊
	private String sendNameBy = null;// 群聊信息谁发的
	private String pic = null;// 发信息人的头像

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean getMsgType() {
		return isComMeg;
	}

	public void setMsgType(boolean isComMsg) {
		isComMeg = isComMsg;
	}

	public boolean isComMeg() {
		return isComMeg;
	}

	public void setComMeg(boolean isComMeg) {
		this.isComMeg = isComMeg;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public String getSendNameBy() {
		return sendNameBy;
	}

	public void setSendNameBy(String sendNameBy) {
		this.sendNameBy = sendNameBy;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public ChatMsgEntity() {
	}

	public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
		super();
		this.name = name;
		this.date = date;
		this.text = text;
		this.isComMeg = isComMsg;
	}

	public ChatMsgEntity(String name, String date, String text,
			boolean isComMeg, boolean isGroup, String sendNameBy, String pic) {
		super();
		this.name = name;
		this.date = date;
		this.text = text;
		this.isComMeg = isComMeg;
		this.isGroup = isGroup;
		this.sendNameBy = sendNameBy;
		this.pic = pic;
	}

	public ChatMsgEntity(String name, String date, String text,
			boolean isComMeg, boolean isGroup) {
		super();
		this.name = name;
		this.date = date;
		this.text = text;
		this.isComMeg = isComMeg;
		this.isGroup = isGroup;
	}

}
