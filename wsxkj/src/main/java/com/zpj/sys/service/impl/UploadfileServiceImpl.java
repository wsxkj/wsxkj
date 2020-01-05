package com.zpj.sys.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zpj.common.BaseDao;
import com.zpj.common.DateHelper;
import com.zpj.common.FileUtil;
import com.zpj.sys.entity.SysUploadFile;
import com.zpj.sys.service.UploadfileService;


@Service("uploadfileService")
public class UploadfileServiceImpl implements UploadfileService{

	@Autowired
	private BaseDao<SysUploadFile> filedao;
	
	public Map uploadMultiply(HttpServletRequest request, MultipartFile file,String tableid,String modeltype){
		Map retMap=new HashMap();
		try{
			String fileUrl="";
			if(file.getSize()>0){
				String originalname = file.getOriginalFilename();
				String filetype = originalname.substring(originalname.lastIndexOf(".")+1);
				String filename = DateHelper.getDateString(new Date(), "YYYYMMddHHmmssSSS")+"."+filetype;
				String path = request.getSession().getServletContext().getRealPath("ueditor");
				
				File targetFile = new File(path, filename);  
		        if(!targetFile.exists()){  
		            targetFile.mkdirs();  
		        }
	        
	        	//上传文件
	            file.transferTo(targetFile);
	            //保存到数据库
	            SysUploadFile fi = new SysUploadFile();
	            fileUrl= request.getContextPath()+"/ueditor/"+filename;
	            fi.setFileId(UUID.randomUUID().toString());
	            fi.setFileName(filename);
				fi.setFileAlias(originalname); //文件原始名称
				fi.setFileUrl(fileUrl);
				fi.setFileSize((int) file.getSize()); //文件大小
				fi.setFileType(filetype); //文件类型
				fi.setTableid(tableid);
				fi.setModeltype(modeltype);
				fi.setCreateDate(new Date());
				filedao.add(fi);
				retMap.put("path", fileUrl);
				retMap.put("id", fi.getFileId());
			}
            return retMap;
	} catch (Exception e) {
		e.printStackTrace();
	}
	        return null;
	}
	
	
	public String uploadFile(HttpServletRequest request, MultipartFile file,String tableid,String modeltype) {
		try{
			String fileUrl="";
			if(file.getSize()>0){
				String originalname = file.getOriginalFilename();
				String filetype = originalname.substring(originalname.lastIndexOf(".")+1);
				String filename = DateHelper.getDateString(new Date(), "YYYYMMddHHmmssSSS")+"."+filetype;
				String path = request.getSession().getServletContext().getRealPath("ueditor");
				
				File targetFile = new File(path, filename);  
		        if(!targetFile.exists()){
		            targetFile.mkdirs();  
		        }
	        
	        	//上传文件
	            file.transferTo(targetFile);
	            //保存到数据库
	            SysUploadFile fi = new SysUploadFile();
	            fileUrl= request.getContextPath()+"/ueditor/"+filename;
	            fi.setFileId(UUID.randomUUID().toString());
	            fi.setFileName(filename);
				fi.setFileAlias(originalname); //文件原始名称
				fi.setFileUrl(fileUrl);
				fi.setFileSize((int) file.getSize()); //文件大小
				fi.setFileType(filetype); //文件类型
				fi.setTableid(tableid);
				fi.setModeltype(modeltype);
				fi.setCreateDate(new Date());
				filedao.add(fi);
			}
	            return fileUrl;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String uploadFile_v1_1_0(HttpServletRequest request, MultipartFile file,String tableid,String modeltype,String userid){
		try{
			String fileUrl="";
			if(file.getSize()>0){
				String originalname = file.getOriginalFilename();
				String filetype = originalname.substring(originalname.lastIndexOf(".")+1);
				String filename = DateHelper.getDateString(new Date(), "YYYYMMddHHmmssSSS")+"."+filetype;
				String path = request.getSession().getServletContext().getRealPath("ueditor")+"/"+userid;
				System.out.println(path);
				File targetFile = new File(path, filename);  
		        if(!targetFile.exists()){
		            targetFile.mkdirs();  
		        }
	        	//上传文件
	            file.transferTo(targetFile);
	            String file_id=UUID.randomUUID().toString();
	            //备份数据
	            FileUtil.getInstance().backupFile(file_id,path, filename);
	            
	            //保存到数据库
	            SysUploadFile fi = new SysUploadFile();
	            fileUrl= request.getContextPath()+"/ueditor/"+userid+"/"+filename;
	            fi.setFileId(file_id);
	            fi.setFileName(filename);
				fi.setFileAlias(originalname); //文件原始名称
				fi.setFileUrl(fileUrl);
				fi.setFileSize((int) file.getSize()); //文件大小
				fi.setFileType(filetype); //文件类型
				fi.setTableid(tableid);
				fi.setModeltype(modeltype);
				fi.setCreateDate(new Date());
				fi.setUserid(userid);
//				fi.setBackup(flag?"1":"0");
				filedao.add(fi);
			}
	            return fileUrl;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public void getImagePaths_v1_1_0(HttpServletRequest request,String url){
		String path = request.getSession().getServletContext().getRealPath("");
		String[] str= url.split(",");
		for(int i=0;i<str.length;i++){
			FileUtil.getInstance().restore(path, str[i]);
		}
	}
	
	
	public String findFiles(String tableid, String modeltype) {
		String sql = "select * from SYS_UploadFile  where tableid='"+tableid+"' ";
		if(StringUtils.isNotEmpty(modeltype)){
			sql +=" and  modeltype ='"+modeltype+"'";
		}
		sql +=" order by createdate  ";
		List<SysUploadFile> list = filedao.findBySqlT(sql, SysUploadFile.class);
		if(list!=null&&list.size()>0){
			StringBuffer str =new StringBuffer("[");
			for(int i=0;i<list.size();i++){
				str.append("{\"name\":\"").append(list.get(i).getFileAlias()).append("\",\"url\":\"")
				.append(list.get(i).getFileUrl()).append("\",\"id\":\"").append(list.get(i).getFileId())
				.append("\"},");
			}
			str.deleteCharAt(str.length()-1);
			str.append("]");
			return str.toString();
		}
		return "[]";
	}

	public void delFileByPath(String path){
		String sql = "delete from SYS_UploadFile where fileurl='"+path+"'";
		filedao.executeSql(sql);
	}
	
	public void delFile(String fileid) {
		//删除数据库数据
		String sql = "delete from SYS_UploadFile where fileid='"+fileid+"'";
		filedao.executeSql(sql);
		
	}
	
	public void updateBackupField(String fileid,boolean flag){
		String sql = "update SYS_UploadFile set backup="+(flag?"1":"0")+" where fileid='"+fileid+"'";
		filedao.executeSql(sql);
	}

}
