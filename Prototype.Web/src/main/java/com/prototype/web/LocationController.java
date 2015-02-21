package com.prototype.web;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prototype.bll.dal.LocationHistoryService;
import com.prototype.bll.entities.LocationHistory;

@Controller
@RequestMapping("/location")
public class LocationController {

	private LocationHistoryService service;
	
	@Inject
	public LocationController(LocationHistoryService service){
		this.service = service;
	}
	
	@RequestMapping(
			value= "/history",
			method = RequestMethod.GET,
			produces="application/json; charset=utf-8")
	public @ResponseBody List<LocationHistory> getLocationHistory(final HttpServletResponse response){
		response.setHeader("Cache-Control", "public, max-age=60");
		return this.service.getLocationHistory();
	}
	
	@RequestMapping(
			value="/insert",
			method=RequestMethod.POST,
			consumes="application/json; charset=utf-8")
	public @ResponseBody boolean  insertLocationHistory(
			@RequestBody LocationHistory locationHistory) {
		return this.service.insertLocationHistory(locationHistory);
	}
	
}
