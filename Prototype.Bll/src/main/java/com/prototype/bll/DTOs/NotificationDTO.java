package com.prototype.bll.DTOs;

import java.util.Date;

/**
 * represents how many new posts there are in a given conversation for a given user
 * @author calin
 *
 */
public class NotificationDTO {

	private long notificationId;
	private String userId;
	private long conversationId;
	private Date createdDate;
	private int newPostsCount;
	private long postId;
	
	public long getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getConversationId() {
		return conversationId;
	}
	public void setConversationId(long conversationId) {
		this.conversationId = conversationId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getNewPostsCount() {
		return newPostsCount;
	}
	public void setNewPostsCount(int newPostsCount) {
		this.newPostsCount = newPostsCount;
	}
	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}
}
