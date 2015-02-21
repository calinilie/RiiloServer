package com.prototype.bll.entities;

import java.util.Date;

public class Notification {

	private long postId;
	private long conversationId;
	private String userId;
	private Date createdDate;
	private String createdDateAsString;
	
	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}
	public long getConversationId() {
		return conversationId;
	}
	public void setConversationId(long conversationId) {
		this.conversationId = conversationId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedDateAsString() {
		return createdDateAsString;
	}
	public void setCreatedDateAsString(String createdDateAsString) {
		this.createdDateAsString = createdDateAsString;
	}	
}
