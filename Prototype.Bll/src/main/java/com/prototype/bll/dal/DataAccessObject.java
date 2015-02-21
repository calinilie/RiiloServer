package com.prototype.bll.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.prototype.bll.DTOs.DeviceDTO;
import com.prototype.bll.DTOs.NotificationDTO;
import com.prototype.bll.DTOs.DeviceDTO.Platform;
import com.prototype.bll.entities.Device;
import com.prototype.bll.entities.GeoLocation;
import com.prototype.bll.entities.LocationHistory;
import com.prototype.bll.entities.Notification;
import com.prototype.bll.entities.Post;
import com.protptype.bll.Helpers;

public class DataAccessObject {

	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private static final String SQL_GET_POST = "SELECT * FROM posts where postId = :postId";
	
	private static final String SQL_GET_LATEST_POSTS = "SELECT * from posts order by Priority desc, CreatedDate desc limit :start, :limit";
	
	private static final String SQL_GET_POSTS_ON_MAP = " select * "+
														" from( "+
														"	SELECT * "+
														"	from posts "+
														"	order by CreatedDate desc) t "+
														" group by conversationId "+
														" order by CreatedDate desc "+
														" limit :start, :limit";
	
	private static final String SQL_GET_CONVERSATION = 
													" select * " +
													" from posts " +
													" where ConversationId = :conversationId ";
	
	private static final String SQL_GET_NOTIFICATIONS_BY_USERID = 
			"select "+	
			"	p.postId, "+
			"	p.UserId, "+
			"	p.ReplyToPostId, "+ 
			"	p.InAdditionToPostId, "+ 
			"	p.Latitude, "+
			"	p.Longitude, "+
			"	p.Accuracy, "+
			"	p.IsUserAtLocation, "+ 
			"	p.Message, "+
			"	p.PictureUri, "+ 
			"	p.CreatedDate, "+
			"	p.ConversationId, "+ 
			"	p.Priority, "+
			"	p.AchievementId, "+ 
			"	p.IsAnouncement, "+
			"	p.Alias "+
			"from notifications n1 "+
			"inner join "+
			"	(select max(notificationId) as notificationId "+
			"	from notifications "+
			"	where IsObsolete=0 "+
			"	and userId = :userId "+
			"	group by ConversationId) n2 "+
			"	on n1.notificationId = n2.notificationId "+
			"join posts p "+
			"	on n1.postId = p.postId";
	
	private static final String SQL_SILENCE_NOTIFICATION_OLD = "INSERT INTO notifications (postId, userId) " +
															"VALUES(:postId, :userId)";
	
	private static final String SQL_SILENCE_NOTIFICATIONS = "update notifications "+
															"set IsObsolete = 1 "+
															"where ConversationId = :conversationId "+
															"and userid = :userId ";
	
	private static final String SQL_INSERT_NOTIFICATIONS = "INSERT INTO notifications "+
															" (PostId, "+
															"  UserId, "+
															"  ConversationId) "+
															" VALUES "+
															" (:postId, "+
															"  :userId, "+
															"  :conversationId) ";
	 
	private static final String SQL_GET_LOCATION_HOSTORY = "select * "+
															" from locationHistory h1 "+
															" where "+
															" (select count(*) "+
															" 	from locationHistory h2 "+
															" 	where h2.userId = h1.userId "+
															" 	and h2.CreatedDate >= h1.createdDate "+
															" )<= :limit "+
															" order by userId desc, createdDate desc ";
	
	private static final String SQL_INSERT_LOCATION_HISTORY = "insert into locationHistory(userId, Latitude, Longitude, LatitudeInRadians, LongitudeInRadians) "+
																"values(:userId, :latitude, :longitude, :latitudeInRadians, :longitudeInRadians)";
																
	private static final String SQL_INSERT_DEVICE_RECORD = "insert into devices (DeviceId, SNSRegistrationId, Platform) "+
															"values(:deviceId, :regId, :platform)";
	
	private static final String SQL_SELECT_ALL_USERS_IN_CONVERSATION = "select distinct(UserId) from posts where ConversationId = :conversationId";
		
	 public List<Post> getConversationFromConversationId(int conversationId){
		 SqlParameterSource parameterSource = new MapSqlParameterSource()
		 	.addValue("conversationId", conversationId, Types.INTEGER);
		 
		 return jdbcTemplate.query(SQL_GET_CONVERSATION, parameterSource, new PostMapper());
	 }
	
	 public Post getPost(long postId){
		 return jdbcTemplate.queryForObject(SQL_GET_POST, new MapSqlParameterSource().addValue("postId", postId), new PostMapper());
	 }
	 
	 public List<Post> getLatestPosts(int start, int limit){
		 SqlParameterSource params = new MapSqlParameterSource()
		 	.addValue("start", start, Types.INTEGER)
		 	.addValue("limit", limit, Types.INTEGER);
		 
		 return jdbcTemplate.query(SQL_GET_LATEST_POSTS, params, new PostMapper());
	 }
	 
	 public List<Post> getPostOnMap(int start, int limit){
		 SqlParameterSource params = new MapSqlParameterSource()
		 	.addValue("start", start, Types.INTEGER)
	 		.addValue("limit", limit, Types.INTEGER);
		 
		 return jdbcTemplate.query(SQL_GET_POSTS_ON_MAP, params, new PostMapper());
	 }
	 
	 public List<Post> getNotificationsAsPostsByUserId(String userId){
		 SqlParameterSource params = new MapSqlParameterSource()
		 	.addValue("userId", userId, Types.VARCHAR);
		 
		 return jdbcTemplate.query(SQL_GET_NOTIFICATIONS_BY_USERID, params, new PostMapper());
	 }
	 
	 public List<NotificationDTO> getNotifications(){
		 String sql = " select * "+
					  " from  "+
					  "		notifications n1 "+
					  "	inner join "+
					  "		(select max(notificationId) as notificationId, count(postId) as newPostsNumber "+
					  "		from notifications "+
					  "		where IsObsolete=0 "+
					  "			and PushSentDate is null "+
					  "		group by ConversationId, userId) n2 "+
					  "	on n1.notificationId = n2.notificationId; ";
		 
		 return jdbcTemplate.query(sql, new NotificationDTOMapper());
	 }
	 
	 public List<Post> getNearbyPostsByLocation(double distanceInKm, double radius, boolean meridian180WithinDistance, GeoLocation[] boundingCoordinates, GeoLocation location){
		 SqlParameterSource params = new MapSqlParameterSource()
		 .addValue("boundingLatitudeMin", boundingCoordinates[0].getLatitudeInRadians())
		 .addValue("boundingLatitudeMax", boundingCoordinates[1].getLatitudeInRadians())
		 .addValue("boundingLongitudeMin", boundingCoordinates[0].getLongitudeInRadians())
		 .addValue("boundingLongitudeMax", boundingCoordinates[1].getLongitudeInRadians())
		 .addValue("locationLatitude", location.getLatitudeInRadians())
		 .addValue("locationLongitude", location.getLongitudeInRadians())
		 .addValue("r", radius);
		 
		 /*String nearbyPostsQuery = "SELECT * FROM posts WHERE (LatitudeInRadians >= :boundingLatitudeMin AND LatitudeInRadians <= :boundingLatitudeMax) AND (LongitudeInRadians >= :boundingLongitudeMin " +
		(meridian180WithinDistance ? "OR" : "AND") + " LongitudeInRadians <= :boundingLongitudeMax) AND " +
									"acos(sin(:locationLatitude) * sin(LatitudeInRadians) + cos(:locationLatitude) * cos(LatitudeInRadians) * cos(LongitudeInRadians - :locationLongitude)) <= :r";*/
		 
		 String nearbyPostsQuery = "  SELECT * FROM posts " +
		 							" WHERE (LatitudeInRadians >= :boundingLatitudeMin AND LatitudeInRadians <= :boundingLatitudeMax) AND (LongitudeInRadians >= :boundingLongitudeMin " +
		 							(meridian180WithinDistance ? "OR" : "AND") + " LongitudeInRadians <= :boundingLongitudeMax) AND " +
									" acos(sin(:locationLatitude) * sin(LatitudeInRadians) + cos(:locationLatitude) * cos(LatitudeInRadians) * cos(LongitudeInRadians - :locationLongitude)) <= :r ";
		 
		 return jdbcTemplate.query(nearbyPostsQuery, params, new PostMapper());
	 }
	 
	 @Deprecated
	 public void silenceNotifications_old(List<Integer> postIds, String userId) {
		 SqlParameterSource[] params = new MapSqlParameterSource[postIds.size()];
		 int length = postIds.size();
		 for(int i=0; i<length; i++) {
			 params[i] = new MapSqlParameterSource()
		 			.addValue("postId", postIds.get(i), Types.BIGINT)
					.addValue("userId", userId, Types.VARCHAR);
		 }
		 
		 jdbcTemplate.batchUpdate(SQL_SILENCE_NOTIFICATION_OLD, params);
	 }
	 
	 public boolean silenceNotifications(long conversationId, String userId) {
		 SqlParameterSource params = new MapSqlParameterSource()
			 .addValue("conversationId", conversationId)
			 .addValue("userId", userId);
		 
		 return jdbcTemplate.update(SQL_SILENCE_NOTIFICATIONS, params) > 0;
	 }
	
	 public List<LocationHistory> getLocationHistory(int limit){
		 SqlParameterSource parameterSource = new MapSqlParameterSource()
		 	.addValue("limit", limit);
		 
		 return jdbcTemplate.query(SQL_GET_LOCATION_HOSTORY, parameterSource, new LocationHistoryMapper());
	 }
	 
	 public List<String> getAllUsersInConversation(long conersationId){
		 SqlParameterSource params = new MapSqlParameterSource()
		 	.addValue("conversationId", conersationId);
		 
		 return jdbcTemplate.queryForList(SQL_SELECT_ALL_USERS_IN_CONVERSATION, params, String.class);
	 }
	 
	 public DeviceDTO getLatestValidDevice(String deviceid) {
		 String sql = "SELECT * FROM devices WHERE deviceId = :deviceId ORDER BY CreatedDate DESC LIMIT 0, 1";
		 SqlParameterSource params = new MapSqlParameterSource()
		 	.addValue("deviceId", deviceid);
		 
		 List<DeviceDTO> result = jdbcTemplate.query(sql, params, new DeviceDTOMapper());
		 return result.size() == 1 ? result.get(0) : null;
	 }

	 public boolean insertLocationHistory(LocationHistory locationHistory) {
		 SqlParameterSource params = new MapSqlParameterSource()
		 	.addValue("userId", locationHistory.getUserId())
		 	.addValue("latitude", locationHistory.getLatitude())
		 	.addValue("longitude", locationHistory.getLongitude())
		 	.addValue("latitudeInRadians", locationHistory.getLatitudeInRadians())
		 	.addValue("longitudeInRadians", locationHistory.getLongitudeInRadians());
		 
		 return jdbcTemplate.update(SQL_INSERT_LOCATION_HISTORY, params) == 1;
	 }
	 
	 public boolean insertDevice(Device device) {
		 SqlParameterSource params = new MapSqlParameterSource()
		 	.addValue("deviceId", device.getDeviceId())
		 	.addValue("regId", device.getRegId())
		 	.addValue("platform", device.getPlatform());
		 System.err.println(device.getPlatform());
		 return jdbcTemplate.update(SQL_INSERT_DEVICE_RECORD, params) == 1;
	 }
	 
	 public int[] insertNotifications(List<Notification> notificationsList) {
		 int length = notificationsList.size();
		 SqlParameterSource[] params = new SqlParameterSource[length];
		 
		 for (int i=0; i<length; i++) {
			 Notification n = notificationsList.get(i);
			 params[i] = new MapSqlParameterSource()
					 			.addValue("postId", n.getPostId())
								.addValue("conversationId", n.getConversationId())
								.addValue("userId", n.getUserId());
		 }
		 return jdbcTemplate.batchUpdate(SQL_INSERT_NOTIFICATIONS, params);  
	 }
	 
	 public int[] update_PushNotificationSent(String userId, List<Long> conversationIds) {
		 String sql = "update notifications set PushSentDate = CURRENT_TIMESTAMP where UserId = :userId and conversationId = :conversationId and PushSentDate is NULL";
		 SqlParameterSource[] params = new SqlParameterSource[conversationIds.size()];
		 
		 for (int i = 0; i < params.length; i++) {
			params[i] = new MapSqlParameterSource()
							.addValue("conversationId", conversationIds.get(i))
							.addValue("userId", userId);
		}
		 
		 return jdbcTemplate.batchUpdate(sql, params);
	 }
	 
	 private class PostMapper implements RowMapper<Post>{
		public Post mapRow(ResultSet resultSet, int lineNumber) throws SQLException {
			Post retVal = new Post();
			retVal.setPostId(resultSet.getInt("PostId"));
			retVal.setUserId(resultSet.getString("UserId"));
			retVal.setRepliesToPostId(resultSet.getInt("ReplyToPostId"));
			retVal.setInAdditionToPostId(resultSet.getInt("InAdditionToPostId"));
			retVal.setLatitude(resultSet.getDouble("Latitude"));
			retVal.setLongitude(resultSet.getDouble("Longitude"));
			retVal.setAccuracy(resultSet.getFloat("Accuracy"));
			retVal.setIsUserAtLocation(resultSet.getBoolean("IsUserAtLocation"));
			retVal.setMessage(resultSet.getString("Message"));
			retVal.setPictureUri(resultSet.getString("PictureUri"));
			retVal.setCreatedDate(resultSet.getTimestamp("CreatedDate"));
			retVal.setConversationId(resultSet.getInt("ConversationId"));
			retVal.setCreatedDateAsString(Helpers.dateToString(retVal.getCreatedDate()));
			retVal.setPriority(resultSet.getInt("Priority"));
			retVal.setAchievementId(resultSet.getInt("AchievementId"));
			retVal.setAnouncement(resultSet.getBoolean("IsAnouncement"));
			retVal.setAlias(resultSet.getString("Alias"));
			return retVal;
		}
	 };
		
	 private class LocationHistoryMapper implements RowMapper<LocationHistory>{
		public LocationHistory mapRow(ResultSet resultSet, int lineNumber) throws SQLException{
			LocationHistory retVal= new LocationHistory();
			retVal.setLocationHistoryId(resultSet.getLong("LocationHistoryId"));
			retVal.setLatitude(resultSet.getDouble("Latitude"));
			retVal.setLongitude(resultSet.getDouble("Longitude"));
			retVal.setCreateDate(resultSet.getTimestamp("CreatedDate"));
			retVal.setCreatedDateAsString(Helpers.dateToString(retVal.getCreateDate()));
			
			return retVal;
		}
	}
	 
	 private class NotificationDTOMapper implements RowMapper<NotificationDTO>{
		public NotificationDTO mapRow(ResultSet resultSet, int lineNumber) throws SQLException {
			NotificationDTO retVal = new NotificationDTO();
			retVal.setNotificationId(resultSet.getLong("NotificationId"));
			retVal.setConversationId(resultSet.getLong("ConversationId"));
			retVal.setCreatedDate(resultSet.getTimestamp("CreatedDate"));
			retVal.setNewPostsCount(resultSet.getInt("newPostsNumber"));
			retVal.setUserId(resultSet.getString("UserId"));
			retVal.setPostId(resultSet.getLong("PostId"));
			
			return retVal;
		}
	 }
	 
	 private class DeviceDTOMapper implements RowMapper<DeviceDTO>{

		public DeviceDTO mapRow(ResultSet resultSet, int lineNumber) throws SQLException {
			DeviceDTO retVal = new DeviceDTO();
			retVal.setDeviceId(resultSet.getString("DeviceId"));
			retVal.setSnsRegsitrationId(resultSet.getString("SNSRegistrationId"));
			Platform platform = resultSet.getString("Platform").equalsIgnoreCase("android") ? Platform.Android : Platform.iOS;
			retVal.setPlatform(platform);
			
			return retVal;
		}
		 
	 }
}
