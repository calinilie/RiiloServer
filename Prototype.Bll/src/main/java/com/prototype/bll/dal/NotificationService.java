package com.prototype.bll.dal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.prototype.bll.DTOs.DeviceDTO;
import com.prototype.bll.DTOs.NotificationDTO;
import com.prototype.bll.DTOs.PushNotificationDTO;
import com.prototype.bll.entities.Post;

public class NotificationService {
	
	private DataAccessObject dao;
	public void setDao(DataAccessObject dao) {
		this.dao = dao;
	}
	
	private DeviceService deviceService;
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	private PostService postService;
	public void setPostService(PostService postService) {
		this.postService = postService;
	}

	public Map<String, PushNotificationDTO> getNotifications_ForPush(){
		Map<String, PushNotificationDTO> retVal = new HashMap<String, PushNotificationDTO>();
		List<NotificationDTO> notifications = dao.getNotifications();
		
		//aggregate all notifications for each user in one PushNotificationDTO
		for (NotificationDTO notification : notifications) {
			
			PushNotificationDTO pushNotification = retVal.get(notification.getUserId());
			if (pushNotification==null) {//there is no notification for this user
				
				pushNotification = new PushNotificationDTO(notification.getUserId());
				
				pushNotification.setCreatedDate(notification.getCreatedDate());
				pushNotification.setPostId(notification.getPostId());
				pushNotification.addNotificationId(notification.getNotificationId());
				pushNotification.addConversationId_NewPostsCount_Pair(
						notification.getConversationId(), 
						notification.getNewPostsCount());
				
				retVal.put(notification.getUserId(), pushNotification);
				
			}
			else {
				pushNotification.addNotificationId(notification.getNotificationId());
				pushNotification.setPostId(notification.getPostId());
				pushNotification.setCreatedDate(notification.getCreatedDate());
				pushNotification.addConversationId_NewPostsCount_Pair(
						notification.getConversationId(), 
						notification.getNewPostsCount());
			}
		}
		
		List<PushNotificationDTO> toRemove = new ArrayList<PushNotificationDTO>();
		//get SNSRegistrationId for devices which will receive push notification
		for (PushNotificationDTO pushNotificationDTO : retVal.values()) {
			DeviceDTO deviceDTO = this.deviceService.getDeviceDTO(pushNotificationDTO.getUserId());
			if (deviceDTO!=null) {
				pushNotificationDTO.setDevice(deviceDTO);
				if (pushNotificationDTO.getNewPostsCount() == 1) {
					pushNotificationDTO.setPost(
								postService.getPost(pushNotificationDTO.getPostId())
							);
				}
			}
			else {
				toRemove.add(pushNotificationDTO);
			}
		}
		
		//remove those without SNSRegistrationId
		for (PushNotificationDTO pushNotificationDTO : toRemove) {
			retVal.remove(pushNotificationDTO.getUserId());
		}
		
		//sanity check
//		for (PushNotificationDTO pushNotificationDTO : retVal.values()) {
//			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+pushNotificationDTO.toString());
//		}
		
		return retVal;
	}
	
	public int[] update_PushNotification_Sent(String userId, List<Long> conversationIds) {
		return dao.update_PushNotificationSent(userId, conversationIds);
	}
}
