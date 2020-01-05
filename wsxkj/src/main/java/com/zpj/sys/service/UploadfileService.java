package com.zpj.sys.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public interface UploadfileService {

	String uploadFile(HttpServletRequest request, MultipartFile file,String tableid,String modeltype);
	
	/**
	 * v1_1_0版本增加用户id信息，以及分文件夹为以后管理考虑
	 * @Title uploadFile_v1_1_0
	 * @param request
	 * @param file
	 * @param tableid
	 * @param modeltype
	 * @return
	 * @author zpj
	 * @time 2019年12月16日 下午1:25:09
	 */
	String uploadFile_v1_1_0(HttpServletRequest request, MultipartFile file,String tableid,String modeltype,String userid);
	
	
	/**
	 * 获取图片路径带备份还原
	 * @Title getImagePaths_v1_1_0
	 * @param url
	 * @return
	 * @author zpj
	 * @time 2019年12月17日 下午3:22:23
	 */
	void getImagePaths_v1_1_0(HttpServletRequest request,String url);
	
	
	
	
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
	
	/**
	 * 根据路径删除附件对象
	 * @Title delFileByPath
	 * @param path
	 * @author zpj
	 * @time 2019年5月9日 上午11:28:21
	 */
	public void delFileByPath(String path);
	
	/**
	 * 延迟更新是否备份字段
	 * @Title updateBackupField
	 * @param fileid
	 * @param flag
	 * @author zpj
	 * @time 2020年1月2日 上午10:04:46
	 */
	public void updateBackupField(String fileid,boolean flag);

}
