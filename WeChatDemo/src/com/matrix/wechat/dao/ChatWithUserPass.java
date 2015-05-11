package com.matrix.wechat.dao;

import com.matrix.wechat.model.ChatHistoryContact;

/**
 * pass user to update chat with history list
 */
public interface ChatWithUserPass {
	public void openChatDialog(ChatHistoryContact chatHistoryContact);
}
