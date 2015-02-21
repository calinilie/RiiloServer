package com.prototype.bll.jobs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.prototype.aws.AWSCredentialsImpl;
import com.prototype.bll.DTOs.DeviceDTO.Platform;
import com.prototype.bll.DTOs.PushNotificationDTO;
import com.prototype.bll.dal.NotificationService;
import com.prototype.bll.dal.PostService;
import com.protptype.bll.Helpers;

@DisallowConcurrentExecution
public class ReplyToPost_NotificationsSender_Job implements Job{

	private static final String AndroidSNSApplicationARN = "arn:aws:sns:eu-west-1:906102622038:app/GCM/Riilo";
	
	
	private AmazonSNSClient snsClient;
	public void setSnsClient(AmazonSNSClient snsClient) {
		this.snsClient = snsClient;
	}
	
	private NotificationService notificationService;
	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}


	public void execute(JobExecutionContext context) throws JobExecutionException {		
		Collection<PushNotificationDTO> pushNotifications = this.notificationService.getNotifications_ForPush().values();
		for (PushNotificationDTO pushNotification : pushNotifications) {
			CreatePlatformEndpointRequest request = new CreatePlatformEndpointRequest();
			PublishRequest publishRequest = new PublishRequest();
			if (pushNotification.getDevice().getPlatform() == Platform.Android) {
				request.setPlatformApplicationArn(AndroidSNSApplicationARN);
			}
			else {
				//TODO iOS not supported yet
			}
			request.setToken(pushNotification.getDevice().getSnsRegsitrationId());
//			TODO Exception handling, logging!
			
			CreatePlatformEndpointResult result =  snsClient.createPlatformEndpoint(request);
			publishRequest.setTargetArn(result.getEndpointArn());
			if (pushNotification.getNewPostsCount()==1) {
				//only one new post
				publishRequest.setMessage(String.format("New reply: \"%s\"", pushNotification.getPost().getMessage()));
			}
			else {
				if (pushNotification.getConversationIds().size() > 1) {
					//many posts on many conversations
					publishRequest.setMessage("You have new replies to "+pushNotification.getConversationIds().size()+" of your previous posts"); 
				}
				else {
					//many posts on one conversation
					publishRequest.setMessage("You have new replies to one of your previous posts");
				}
			}
			

			PublishResult publishResult = snsClient.publish(publishRequest);
//			TODO Exception handling, logging!
			
			List<Long> ids = new ArrayList<Long>();
			ids.addAll(pushNotification.getConversationIds().keySet());
			notificationService.update_PushNotification_Sent(pushNotification.getUserId(), ids);
		}
	}

}
