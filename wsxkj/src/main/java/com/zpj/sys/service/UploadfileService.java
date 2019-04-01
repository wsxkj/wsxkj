package com.zpj.sys.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface UploadfileService {

	String uploadFile(HttpServletRequest request, MultipartFile file,String tableid,String modeltype);
	
	
	/**
	 * 多附件上传
	 * @Title uploadMultiply
	 * @param request
	 * @param file
	 * @param tableid
	 * @param modeltype
	 * @return
	 * @author zpj
	 * @time 2018年7月23日 下午2:11:18
	 */
	Map uploadMultiply(HttpServletRequest request, MultipartFile file,String tableid,String modeltype);
	

	String findFiles(String tableid, String modeltype);

	void delFile(String fileid);

}
