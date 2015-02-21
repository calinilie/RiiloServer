package com.prototype.web;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prototype.bll.dal.DeviceService;
import com.prototype.bll.entities.Device;

@Controller
@RequestMapping("/device")
public class DeviceController {

	private DeviceService deviceService;
	
	@Inject
	public DeviceController(DeviceService service) {
		this.deviceService = service;
	}
	
	@RequestMapping(
			value="/insert",
			method=RequestMethod.POST,
			consumes="application/json; charset=utf-8")
	public @ResponseBody boolean insertDevice(
			@RequestBody Device device) {
		return this.deviceService.insertDevice(device);
	}
	
}
