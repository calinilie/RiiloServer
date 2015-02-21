package com.prototype.bll.entities;

import java.util.Date;

public class LocationHistory {
	
	private long locationHistoryId;
	private String userId;
	private double latitude;
	private double longitude;
	private double latitudeInRadians;
	private double longitudeInRadians;
	private Date createDate;
	private String createdDateAsString;
	
	
	
	public long getLocationHistoryId() {
		return locationHistoryId;
	}
	public void setLocationHistoryId(long id) {
		this.locationHistoryId = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreatedDateAsString() {
		return createdDateAsString;
	}
	public void setCreatedDateAsString(String createdDateAsString) {
		this.createdDateAsString = createdDateAsString;
	}
	public double getLatitudeInRadians() {
		return latitudeInRadians;
	}
	public void setLatitudeInRadians(double latitudeInRadians) {
		this.latitudeInRadians = latitudeInRadians;
	}
	public double getLongitudeInRadians() {
		return longitudeInRadians;
	}
	public void setLongitudeInRadians(double longitudeInRadians) {
		this.longitudeInRadians = longitudeInRadians;
	}
	
}
