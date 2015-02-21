package com.prototype.bll.DTOs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prototype.bll.entities.Post;

public class PushNotificationDTO {

	private String userId;
	private List<Long> notificationIds;
	/**
	 * conversationId and number of new posts
	 */
	private Map<Long, Integer> conversationIds;
	private Date createdDate;
	private int totalNewPostsCount;
	private long postId;
	private Post post;
	private DeviceDTO device;
	
	public PushNotificationDTO(String userId) {
		this.userId = userId;
	}
	
	public List<Long> getNotificationIds() {
		return notificationIds;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Map<Long, Integer> getConversationIds() {
		return conversationIds;
	}
	public void setConversationIds(Map<Long, Integer> conversationIds) {
		this.conversationIds = conversationIds;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		if (this.createdDate!=null) {
			if (this.createdDate.before(createdDate)) {
				this.createdDate = createdDate;
			}
		}
		else {
			this.createdDate = createdDate;
		}
		
	}
	public int getNewPostsCount() {
		return totalNewPostsCount;
	}
	public void setTotalNewPostsCount(int totalNewPostsCount) {
		this.totalNewPostsCount = totalNewPostsCount;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public DeviceDTO getDevice() {
		return device;
	}
	public void setDevice(DeviceDTO device) {
		this.device = device;
	}
	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}
	public void addNotificationId(long notificationId) {
		if (this.notificationIds==null)
			this.notificationIds = new ArrayList<Long>();
		
		this.notificationIds.add(notificationId);
	}
	
	public void addConversationId_NewPostsCount_Pair(long conversationId, int postsCount) {
		if (conversationIds==null)
			this.conversationIds = new HashMap<Long, Integer>();
		
		this.conversationIds.put(conversationId, postsCount);
		this.totalNewPostsCount += postsCount;
	}
	
	@Override
	public String toString() {
		String notificationIds = "";
		for (long l : this.notificationIds) {
			notificationIds += l+" ";
		}
		return String.format("%s %s %s %s %s", this.userId, this.totalNewPostsCount, this.conversationIds.size(), this.createdDate.toString(), notificationIds);
//		return String.format("%s %s %s", this.userId, this.totalNewPostsCount, this.conversationIds.size());
	}
	
}
