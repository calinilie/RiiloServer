package com.prototype.bll.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {

	private int postId;
	private String userId;
	private int repliesToPostId;
	private long conversationId;
	private int inAdditionToPostId;
	private double latitude;
	private double longitude;
	private double latitudeRadians;
	private double longitudeRadians;
	private double originLatitude;
	private double originLongitude;
	private float accuracy;
	private boolean isUserAtLocation;
	private String message;
	private String pictureUri;
	private String createdDateAsString;
	private Date createdDate;
	private List<DistanceFromPost> distancesFromPost;
	private int priority;
	private int achievementId;
	private boolean isAnouncement;
	private String alias;

	public Post(){
		this.distancesFromPost = new ArrayList<DistanceFromPost>();
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRepliesToPostId() {
		return repliesToPostId;
	}

	public void setRepliesToPostId(int repliesToPostId) {
		this.repliesToPostId = repliesToPostId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitutde) {
		this.latitude = latitutde;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public boolean isUserAtLocation() {
		return isUserAtLocation;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPictureUri() {
		return pictureUri;
	}

	public void setPictureUri(String pictureUri) {
		this.pictureUri = pictureUri;
	}

	public int getInAdditionToPostId() {
		return inAdditionToPostId;
	}

	public void setInAdditionToPostId(int inAdditionToPostId) {
		this.inAdditionToPostId = inAdditionToPostId;
	}

	public void setIsUserAtLocation(boolean isUserAtLocation) {
		this.isUserAtLocation = isUserAtLocation;
	}

	public String getCreatedDateAsString() {
		return createdDateAsString;
	}

	public void setCreatedDateAsString(String createdDateAsString) {
		this.createdDateAsString = createdDateAsString;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public long getConversationId() {
		return conversationId;
	}

	public void setConversationId(int conversationId) {
		this.conversationId = conversationId;
	}

	public double getLatitudeRadians() {
		return latitudeRadians;
	}

	public void setLatitudeRadians(double latitudeRadians) {
		this.latitudeRadians = latitudeRadians;
	}

	public double getLongitudeRadians() {
		return longitudeRadians;
	}

	public void setLongitudeRadians(double longitudeRadians) {
		this.longitudeRadians = longitudeRadians;
	}

	public double getOriginLatitude() {
		return originLatitude;
	}

	public void setOriginLatitude(double originLatitude) {
		this.originLatitude = originLatitude;
	}

	public double getOriginLongitude() {
		return originLongitude;
	}

	public void setOriginLongitude(double originLongitude) {
		this.originLongitude = originLongitude;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getAchievementId() {
		return achievementId;
	}

	public void setAchievementId(int achievementId) {
		this.achievementId = achievementId;
	}

	public boolean isAnouncement() {
		return isAnouncement;
	}

	public void setAnouncement(boolean isAnouncement) {
		this.isAnouncement = isAnouncement;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	// post grouping
	public List<DistanceFromPost> getDistancesFromPost() {
		return distancesFromPost;
	}
	
	public void clearDistances() {
		this.distancesFromPost.clear();
	}
	
	public void addDistance(double distance, Post p) {
		DistanceFromPost distanceFromPost = new DistanceFromPost();
		distanceFromPost.setDistance(distance);
		distanceFromPost.setPost(p);
		this.distancesFromPost.add(distanceFromPost);
	}
	
	public List<Post> getPostsInDistancesFromPosts(){
		List<Post> retVal = new ArrayList<Post>();
		for (DistanceFromPost distanceFromPost : this.distancesFromPost) {
			retVal.add(distanceFromPost.getPost());
		}
		return retVal;
	}
	
//	@Override
//	public String toString() {
		/*return String.format("id: %s \r\n userId: %s \r\n replyTo: %s \r\n convId: %s \r\n lat: %s \r\n long: %s \r\n isUserAtLoc: %s \r\n message: %s ", 
								postId, userId, repliesToPostId, conversationId, latitude, longitude, isUserAtLocation, message);*/
		
		/*String distancesStirng = "";
		for (DistanceFromPost distance : this.distancesFromPost) {
			distancesStirng+=distance.toString();
		}
		return String.format("Post Id: %s \r\n %s  \r\n\r\n", this.postId+"", distancesStirng);*/
//	}	
}
