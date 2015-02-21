package com.prototype.web;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.prototype.bll.DTOs.PostInsertedDTO;
import com.prototype.bll.dal.PostService;
import com.prototype.bll.entities.Post;
import com.prototype.web.DTOs.*;

@Controller
@RequestMapping("/posts")
public class PostsController{

	private PostService postService;
	
	@Inject
	public PostsController(PostService postService) {
		this.postService = postService;
	}
	
	@RequestMapping(
			value = "/latest/{start}/{limit}", 
			method = RequestMethod.GET, 
			produces="application/json; charset=utf-8")
	public @ResponseBody List<Post> getLatestPosts(
			@PathVariable int start,
			@PathVariable int limit,
	        final HttpServletResponse response){
		response.setHeader("Cache-Control", "public, max-age=20");
		return postService.getLatesPosts(start, limit);
	}
	
	@RequestMapping(
			value = "/conversation/{conversationId}", 
			method = RequestMethod.GET, 
			produces="application/json; charset=utf-8")
	public @ResponseBody List<Post> getConversationFormPostId(
			@PathVariable int conversationId,
			final HttpServletResponse response){
		response.setHeader("Cache-Control", "public, max-age=20");
		return postService.getConversationFromPostId(conversationId);
	}
	
	@RequestMapping(value = "/conversation/ids/{postId}",
					method = RequestMethod.GET,
					produces="application/json; charset=utf-8")
	public @ResponseBody List<Integer> getConversationPostIdsFormPostId(
			@PathVariable int postId){
		List<Post> posts = postService.getConversationFromPostId(postId);
		List<Integer> retVaList = new ArrayList<Integer>();
		for(Post post : posts) {
			retVaList.add(post.getPostId());
		}
		return retVaList;
	}
	
	@RequestMapping(value = "/nearby/{latitude}/{longitude}/{distance}/", produces="application/json; charset=utf-8")
	public @ResponseBody List<Post> getNearbyPostsByLocation(
			@PathVariable double latitude, 
			@PathVariable double longitude,
			@PathVariable double distance){
		return postService.getNearbypostsByLocation(latitude, longitude, distance);
	}
	
	@RequestMapping(value = "/nearby/{latitude}/{longitude}/{targetLatitude}/{targetLongitude}/", produces="application/json; charset=utf-8")
	public @ResponseBody List<Post> getNearbyPostsByLocation(
			@PathVariable double latitude, 
			@PathVariable double longitude,
			@PathVariable double targetLatitude,
			@PathVariable double targetLongitude){
		return postService.getNearbypostsByLocation(latitude, longitude, targetLatitude, targetLongitude);
	}
	
	@RequestMapping(value = "/new/{userId}", produces="application/json; charset=utf-8")
	public @ResponseBody List<Post> getNotificationsAsPosts(
			@PathVariable String userId){
		return this.postService.getNotificationsAsPostsByUserId(userId);
	}
	
	@RequestMapping(value="/upload", 
					produces="application/json; charset=utf-8", 
					consumes="application/json; charset=utf-8", 
					method = RequestMethod.POST)
	public @ResponseBody PostInsertedDTO handlePostUpload(
			@RequestBody Post model){
		PostInsertedDTO retVal = postService.insertPost(model);
		return retVal;
	}
	
	@RequestMapping(value="/silence/",  
					consumes="application/json; charset=utf-8",
					method = RequestMethod.POST)
	public @ResponseBody String silenceNotificationsOld(
			@RequestBody SilenceNotificationsDTO_Old model
			) {
		if (model.getPostIds()!=null 
			&& !model.getPostIds().isEmpty() 
			&& model.getUserId()!=null 
			&& !model.getUserId().isEmpty()) {
			postService.silencePostNotifications_Old(model.getPostIds(), model.getUserId());
		}
		return "OK";
	}
	
	@RequestMapping(value="/silenceV2/",  
			consumes="application/json; charset=utf-8",
			method = RequestMethod.POST)
	public @ResponseBody boolean silenceNotifications(
			@RequestBody SilenceNotificationsDTO model
			) {		
		return postService.silencePostNotifications(model.getConversationId(), model.getUserId());
	}
	
	@RequestMapping(value = "/onmap")
	public @ResponseBody List<Post> getAllPostsOnMap(final HttpServletResponse response){
		//60*60*24 = 24 hours
		response.setHeader("Cache-Control", "public, max-age=86400");
		return postService.getPostsOnMap();
	}
	
	/*@RequestMapping(
		value = "/latest", 
		method = RequestMethod.GET, 
		produces="application/json; charset=utf-8")
	public @ResponseBody List<Post> getLatestPosts(){
	return postService.getLatesPosts();
	}
	
	@RequestMapping(
		value = "/latest/{start}", 
		method = RequestMethod.GET, 
		produces="application/json; charset=utf-8")
	public @ResponseBody List<Post> getLatestPosts(
		@PathVariable int start){
	return postService.getLatesPosts(start);
	}*/
	
	/*private String saveImage(MultipartFile file){
		URI targetPath = null;		
		try {
			targetPath = HomeController.class.getProtectionDomain().getCodeSource().getLocation().toURI();
			File image = new File(GetFileName(targetPath));
			while (image.exists()) {
				image= new File(GetFileName(targetPath));
			}
			FileUtils.writeByteArrayToFile(image, file.getBytes());
			File[] files = {image};
			return FileUtils.toURLs(files)[0].toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	private URI GetFileName(URI targetPath) throws URISyntaxException {
		String s  = targetPath.toString();
		String path = s.substring(0, s.indexOf("WEB-INF"))+"resources/"+Calendar.getInstance().getTimeInMillis()+".jpg";;
		return new URI(path);
	}
	*/
}

