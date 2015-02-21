package com.prototype.web.DTOs;

public class SilenceNotificationsDTO {

	private long conversationId;
	private String userId;
	
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
}
