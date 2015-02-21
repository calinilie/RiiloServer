package com.prototype.bll.dal;

import java.util.List;

import com.prototype.bll.entities.LocationHistory;

public class LocationHistoryService {

	private DataAccessObject dao;
	public void setDao(DataAccessObject dao) {
		this.dao = dao;
	}
	
	public List<LocationHistory> getLocationHistory(){
		return this.dao.getLocationHistory(3);
	}
	
	public boolean insertLocationHistory(LocationHistory locationHistory) {
		locationHistory.setLatitudeInRadians(Math.toRadians(locationHistory.getLatitude()));
		locationHistory.setLongitudeInRadians(Math.toRadians(locationHistory.getLongitude()));
		return this.dao.insertLocationHistory(locationHistory);
	}
}