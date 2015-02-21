package com.prototype.bll.imagehandling;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.util.FileUtil;


@Service
public class ImageService {

	public String saveImage(MultipartFile image, String targetPath) {
		String path = targetPath.substring(6, targetPath.indexOf("WEB-INF"))+"resources/";

		return path;
	}
	
}
