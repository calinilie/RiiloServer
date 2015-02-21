package com.prototype.bll.dal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prototype.bll.DTOs.PostInsertedDTO;
import com.prototype.bll.entities.GeoLocation;
import com.prototype.bll.entities.Notification;
import com.prototype.bll.entities.Post;
import com.protptype.bll.Helpers;

public class PostService {
	private static final double EarthRadiusInKm = 6371.01;
	
	private static Logger logger = LoggerFactory.getLogger(PostService.class);
	
	private DataAccessObject dao;
	public void setDao(DataAccessObject dao) {
		this.dao = dao;
	}
	
	private SPDataAccessObject spDao;
	public void setSpDao(SPDataAccessObject spDao) {
		this.spDao = spDao;
	}
	
	private DeviceService deviceService;
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	public PostInsertedDTO insertPost(Post post) {
		post.setLatitudeRadians(Math.toRadians(post.getLatitude()));
		post.setLongitudeRadians(Math.toRadians(post.getLongitude()));
		PostInsertedDTO retVal = spDao.insertPost(post);
		
		List<String> usersToBeNotified = this.deviceService.getAllUsersInConversation(retVal.getConversationId());
		usersToBeNotified.remove(post.getUserId());
		List<Notification> notificationsToBeInserted = new ArrayList<Notification>();
		for (String userId : usersToBeNotified) {
			Notification n = new Notification();
			n.setConversationId(retVal.getConversationId());
			n.setUserId(userId);
			n.setPostId(retVal.getPostId());
			notificationsToBeInserted.add(n);
		}
		
		dao.insertNotifications(notificationsToBeInserted);
		
		return retVal;
	}
	
	public List<Post> getConversationFromPostId(int conversationId){
		return dao.getConversationFromConversationId(conversationId);
	}
	
	public List<Post> getLatesPosts(){
		return dao.getLatestPosts(0,  20);
	}
	
	public List<Post> getLatesPosts(int start){
		return dao.getLatestPosts(start, 20);
	}
	
	public List<Post> getLatesPosts(int start, int limit){
		if (limit<=0) limit = 20;
		return dao.getLatestPosts(start, limit);
	}
	
	
	/**
	 * Performance heavy shit! needs some smarter algorithms - motherfucker!
	 * @return a list of posts - motherfucker!
	 */
	public List<Post> getPostsOnMap(){
//		String retVal = "";
		List<Post> posts = dao.getPostOnMap(0, 2000);
		Set<Post> toRemove = new HashSet<Post>();
		int size = posts.size();
		double distance = -1;
		for(int i = 0; i < size-1; i++) {
			Post postA = posts.get(i);
			for (int j = i+1; j < size; j++) {
				Post postB = posts.get(j);
				distance = Helpers.distanceFrom(
						postA.getLatitude(), postA.getLongitude(), 
						postB.getLatitude(), postB.getLongitude());
				if (distance < 200) {
					postA.addDistance(distance, postB);
					postB.addDistance(distance, postA);
				}
			}
//			retVal += postA.toString();
		}
		
		int i=0;
		size = posts.size();
		while (i<size) {
			toRemove.addAll(posts.get(i).getPostsInDistancesFromPosts());
			posts.removeAll(toRemove);
			toRemove.clear();
			size = posts.size();
			i++;
		}
		
		for(Post p: posts) {
			p.clearDistances();
		}
		return posts;
	}
	
	public List<Post> getNearbypostsByLocation(double latitude, double longitude, double targetLatitude, double targetLongitude){
		return this.getNearbypostsByLocation(latitude, longitude, Helpers.distanceFrom(latitude, longitude, targetLatitude, targetLongitude));
	}
	
	public List<Post> getNearbypostsByLocation(double latitude, double longitude, double distance){
		if (distance==0) {
			distance = 20;
		}
		GeoLocation location = GeoLocation.fromDegrees(latitude, longitude);
		GeoLocation[] boundingCoordinates = location.boundingCoordinates(distance, EarthRadiusInKm);
		boolean meridian180WithinDistance = boundingCoordinates[0].getLongitudeInRadians() > boundingCoordinates[1].getLongitudeInRadians();
		double r = distance/EarthRadiusInKm;
		List<Post> retVal = dao.getNearbyPostsByLocation(distance, r, meridian180WithinDistance, boundingCoordinates, location);
		logger.info("number of nearby posts --> "+retVal.size());
		return retVal;
	}
	
	public List<Post> getNotificationsAsPostsByUserId(String userId){
		if (userId!=null && !userId.isEmpty())
			return dao.getNotificationsAsPostsByUserId(userId);
		return null;
	}
	
	public boolean silencePostNotifications(long conversationId, String userId) {
		return dao.silenceNotifications(conversationId, userId);
	}
	
	@Deprecated
	public boolean silencePostNotifications_Old(List<Integer> postIds, String userId) {
		Post post = dao.getPost(postIds.get(0));
		long conversationId = post.getConversationId();
		return dao.silenceNotifications(conversationId, userId);
	}
	
	public Post getPost(long postId) {
		return dao.getPost(postId);
	}
}
