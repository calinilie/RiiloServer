package com.prototype.bll.dal;

import java.util.List;

import com.prototype.bll.DTOs.DeviceDTO;
import com.prototype.bll.entities.Device;

public class DeviceService {

	private DataAccessObject dao;
	public void setDao(DataAccessObject dao) {
		this.dao = dao;
	}
	
	public boolean insertDevice(Device device) {
		return dao.insertDevice(device);
	}

	public List<String> getAllUsersInConversation(long conersationId){
		return dao.getAllUsersInConversation(conersationId);
	}
	
	/**
	 * 
	 * @param deviceId same as userId- currently!
	 * @return
	 */
	public DeviceDTO getDeviceDTO(String deviceId) {
		return dao.getLatestValidDevice(deviceId);
	}
}
