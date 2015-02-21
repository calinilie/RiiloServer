package com.prototype.bll.dal;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.prototype.bll.DTOs.PostInsertedDTO;
import com.prototype.bll.entities.Post;

public class SPDataAccessObject {

	private DataSource dataSource;
	private InsertPostWithConversationSP insertPostSP;
	
	public SPDataAccessObject(DataSource dataSource) {
		this.dataSource = dataSource;
		this.insertPostSP = new InsertPostWithConversationSP(dataSource);
	}
	
	public PostInsertedDTO insertPost(Post post) {
		return insertPostSP.execute(post);
	}
	
	private class InsertPostWithConversationSP extends StoredProcedure{
		
		private static final String SP_NAME = "insert_post_with_conversation";
		
		public InsertPostWithConversationSP(DataSource dataSource){
			super(dataSource, SP_NAME);
			declareParameter(new SqlParameter("in_userId", Types.VARCHAR));
			declareParameter(new SqlParameter("in_replyToPostId", Types.INTEGER));
			declareParameter(new SqlParameter("in_conversationId", Types.BIGINT));
			declareParameter(new SqlParameter("in_inAdditionToPostId", Types.INTEGER));
			declareParameter(new SqlParameter("in_latitude", Types.DOUBLE));
			declareParameter(new SqlParameter("in_longitude", Types.DOUBLE));
			declareParameter(new SqlParameter("in_latitude_radians", Types.DOUBLE));
			declareParameter(new SqlParameter("in_longitude_radians", Types.DOUBLE));
			declareParameter(new SqlParameter("in_origin_latitude", Types.DOUBLE));
			declareParameter(new SqlParameter("in_origin_longitude", Types.DOUBLE));
			declareParameter(new SqlParameter("in_accuracy", Types.FLOAT));
			declareParameter(new SqlParameter("in_isUserAtLocation", Types.TINYINT));
			declareParameter(new SqlParameter("in_message", Types.VARCHAR));
			declareParameter(new SqlParameter("in_pictureUri", Types.VARCHAR));
			declareParameter(new SqlOutParameter("out_newPostId", Types.BIGINT));
			declareParameter(new SqlOutParameter("out_conversationId", Types.BIGINT));
		}
		
		public PostInsertedDTO execute(Post post) {
			Map<String, Object> inParemeters = new HashMap<String, Object>();
			inParemeters.put("in_userId", post.getUserId());
			inParemeters.put("in_replyToPostId", post.getRepliesToPostId());
			inParemeters.put("in_conversationId", post.getConversationId());
			inParemeters.put("in_inAdditionToPostId", post.getInAdditionToPostId());
			inParemeters.put("in_latitude", post.getLatitude());
			inParemeters.put("in_longitude", post.getLongitude());
			inParemeters.put("in_latitude_radians", post.getLatitudeRadians());
			inParemeters.put("in_longitude_radians", post.getLongitudeRadians());
			inParemeters.put("in_origin_latitude", post.getOriginLatitude());
			inParemeters.put("in_origin_longitude", post.getOriginLongitude());
			inParemeters.put("in_accuracy", post.getAccuracy());
			inParemeters.put("in_isUserAtLocation", post.isUserAtLocation());
			inParemeters.put("in_message", post.getMessage());
			inParemeters.put("in_pictureUri", post.getPictureUri());
			Map<String, Object> queryResult = super.execute(inParemeters);
			
			long newPostId = (Long) queryResult.get("out_newPostId");
			long converstionId = (Long)queryResult.get("out_conversationId");
			
			PostInsertedDTO retVal = new PostInsertedDTO();
			retVal.setSuccess(newPostId!=0);
			retVal.setPostId(newPostId);
			retVal.setConversationId(converstionId);
			
			return retVal;
		}
	}
	
	@SuppressWarnings("unused")
	private class InsertPostSP extends StoredProcedure{
		
		private static final String SP_NAME = "insert_post";
		
		public InsertPostSP(DataSource dataSource){
			super(dataSource, SP_NAME);
			declareParameter(new SqlParameter("in_userId", Types.VARCHAR));
			declareParameter(new SqlParameter("in_replyToPostId", Types.INTEGER));
			declareParameter(new SqlParameter("in_inAdditionToPostId", Types.INTEGER));
			declareParameter(new SqlParameter("in_latitude", Types.DOUBLE));
			declareParameter(new SqlParameter("in_longitude", Types.DOUBLE));
			declareParameter(new SqlParameter("in_accuracy", Types.FLOAT));
			declareParameter(new SqlParameter("in_isUserAtLocation", Types.TINYINT));
			declareParameter(new SqlParameter("in_message", Types.VARCHAR));
			declareParameter(new SqlParameter("in_pictureUri", Types.VARCHAR));
			declareParameter(new SqlOutParameter("out_newPostId", Types.INTEGER));
		}
		
		public int execute(Post post) {
			Map<String, Object> inParemeters = new HashMap<String, Object>();
			inParemeters.put("in_userId", post.getUserId());
			inParemeters.put("in_replyToPostId", post.getRepliesToPostId());
			inParemeters.put("in_inAdditionToPostId", post.getInAdditionToPostId());
			inParemeters.put("in_latitude", post.getLatitude());
			inParemeters.put("in_longitude", post.getLongitude());
			inParemeters.put("in_accuracy", post.getAccuracy());
			inParemeters.put("in_isUserAtLocation", post.isUserAtLocation());
			inParemeters.put("in_message", post.getMessage());
			inParemeters.put("in_pictureUri", post.getPictureUri());
			Map<String, Object> queryResult = super.execute(inParemeters);
			return (Integer) queryResult.get("out_newPostId");
		}
	}
	
}
