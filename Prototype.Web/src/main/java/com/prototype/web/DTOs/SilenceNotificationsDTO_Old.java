package com.prototype.web.DTOs;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

public class SilenceNotificationsDTO_Old {
	
	public String userId;
	
	@JsonDeserialize(as=ArrayList.class, contentAs=Integer.class)
	public List<Integer> postIds;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Integer> getPostIds() {
		return postIds;
	}
	public void setPostIds(List<Integer> postIds) {
		this.postIds = postIds;
	}
	
}
