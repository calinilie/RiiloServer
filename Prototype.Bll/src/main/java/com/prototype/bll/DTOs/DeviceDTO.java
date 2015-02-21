package com.prototype.bll.DTOs;

public class DeviceDTO {

	public static enum Platform { Android, iOS }
	
	private String deviceId;
	private String snsRegsitrationId;
	private Platform platform;
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSnsRegsitrationId() {
		return snsRegsitrationId;
	}
	public void setSnsRegsitrationId(String snsRegsitrationId) {
		this.snsRegsitrationId = snsRegsitrationId;
	}
	public Platform getPlatform() {
		return platform;
	}
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
}
