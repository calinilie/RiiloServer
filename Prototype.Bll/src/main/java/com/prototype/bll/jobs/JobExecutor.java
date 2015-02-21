package com.prototype.bll.jobs;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.prototype.bll.dal.NotificationService;
import com.prototype.bll.dal.PostService;

public class JobExecutor {
	
	private SchedulerFactory factory;
	public void setFactory(SchedulerFactory factory) {
		this.factory = factory;
	}
	
	private AmazonSNSClient snsClient;
	public void setSnsClient(AmazonSNSClient snsClient) {
		this.snsClient = snsClient;
	}
	
	private NotificationService notificationService;
	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}


	public void startJob() throws SchedulerException {
		Scheduler scheduler = factory.getScheduler();
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, 5);
		
		//inserting required dependencies into JobContext, so that Spring will wire them automatically.
		JobDataMap map = new JobDataMap();
		snsClient.setRegion(Region.getRegion(Regions.EU_WEST_1));
		map.put("snsClient", this.snsClient);
		map.put("notificationService", this.notificationService);
		
		JobDetail job = newJob(ReplyToPost_NotificationsSender_Job.class)
			    .withIdentity("job1", "group1").setJobData(map)
			    .build();
		
		Trigger trigger = newTrigger().forJob(job)
			    .withIdentity("trigger1", "group1")
			    .startNow()
		    	.withSchedule(simpleSchedule()
		                .withIntervalInSeconds(20)
		                .repeatForever())
			    .build();
		
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
	}
	
	

}
