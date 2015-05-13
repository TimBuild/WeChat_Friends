package com.matrix.wechat.model;

import java.util.List;

public class ShareWithComment {
	User user;
	ShareFriend shareFriend;
	List<ShareComment> shareComments;
	List<CommentUser> commentUsers;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ShareFriend getShareFriend() {
		return shareFriend;
	}

	public void setShareFriend(ShareFriend shareFriend) {
		this.shareFriend = shareFriend;
	}

	public List<ShareComment> getShareComments() {
		return shareComments;
	}

	public void setShareComments(List<ShareComment> shareComments) {
		this.shareComments = shareComments;
	}

	public List<CommentUser> getCommentUsers() {
		return commentUsers;
	}

	public void setCommentUsers(List<CommentUser> commentUsers) {
		this.commentUsers = commentUsers;
	}

	public ShareWithComment(User user, ShareFriend shareFriend,
			List<ShareComment> shareComments, List<CommentUser> commentUsers) {
		super();
		this.user = user;
		this.shareFriend = shareFriend;
		this.shareComments = shareComments;
		this.commentUsers = commentUsers;
	}

	public ShareWithComment() {
		super();
	}

	@Override
	public String toString() {
		return "ShareWithComment [user=" + user + ", shareFriend="
				+ shareFriend + ", shareComments=" + shareComments
				+ ", commentUsers=" + commentUsers + "]";
	}

}
