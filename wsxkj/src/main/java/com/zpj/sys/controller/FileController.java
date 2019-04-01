package com.zpj.sys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zpj.common.BaseController;
import com.zpj.common.FileHelper;
import com.zpj.sys.service.UploadfileService;


@Controller
@RequestMapping("file")
public class FileController extends BaseController{

	@Autowired
	private UploadfileService uploadfileService;
	
	@RequestMapping("toFile")
	public String toFile(ModelMap modelMap){
		
		return "/test/file";
	}
	
	
	@RequestMapping(value="doFile")
	public String doFile(MultipartFile file,HttpServletRequest request,
			ModelMap modelMap){
		//上传附件		
		String path = uploadfileService.uploadFile(request,file,null,null);
		
		modelMap.addAttribute("path", path);
		return "/test/file";
	}
	
	
	
	@RequestMapping("upload")
	@ResponseBody
	public void file(HttpServletRequest request,MultipartFile file,String tableid,String modeltype) {				
		jsonData = uploadfileService.uploadFile(request,file,tableid,modeltype);
		this.jsonWrite(jsonData);
	}
	
	@RequestMapping("uploadMultiply")
	@ResponseBody
	public void uploadMultiply(HttpServletRequest request,MultipartFile file,String tableid,String modeltype) {				
		Map map = uploadfileService.uploadMultiply(request,file,tableid,modeltype);
		map.put("code", 0);
		this.jsonWrite2(map);
	}
	
	@RequestMapping("findFiles")
	@ResponseBody
	public void findFiles(String tableid,String modeltype) {				
		String path = uploadfileService.findFiles(tableid,modeltype);
		this.jsonWrite(path);
	}
	/**
	 * 动态表单中多附件上传的后台
	 * @Title findFilesModeltypes
	 * @param tableid
	 * @param modeltype
	 * @author zpj
	 * @time 2018年5月14日 下午2:20:10
	 */
	@RequestMapping("findFilesModeltypes")
	@ResponseBody
	public void findFilesModeltypes(String tableid,String modeltype) {		
		String[] types=modeltype.split(",");
		Map map=new HashMap();
		for(int m=0;m<types.length;m++){
			String path = uploadfileService.findFiles(tableid,types[m]);
			map.put(types[m], path);
		}
		this.jsonWrite2(map);
	}
	
	
	
	@RequestMapping("delFile")
	@ResponseBody
	public void delFile(String fileid,String filepath) {
		try{
			//删除数据
			uploadfileService.delFile(fileid);
			//删除文件
			String path = request.getSession().getServletContext().getRealPath("/");
			FileHelper.delFile(path+filepath);
			this.jsonWrite("0");
		}catch (Exception e) {
			e.printStackTrace();
			this.jsonWrite("1");
		}
	}
	
	
	@RequestMapping("downloadFile")
	@ResponseBody
	public void download(String filepath,String filename) {
		
			String path = request.getSession().getServletContext().getRealPath("/");
			path = path+filepath;
			FileHelper.downloadFile(path, filename, response);			
		
	}
	
}
