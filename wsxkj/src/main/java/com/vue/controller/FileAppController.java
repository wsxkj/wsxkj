package com.vue.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zpj.common.BaseController;
import com.zpj.common.FileHelper;
import com.zpj.common.ResultData;
import com.zpj.jwt.JwtUtil;
import com.zpj.sys.entity.User;
import com.zpj.sys.service.UploadfileService;

import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
@Controller
@RequestMapping("/app/file")
@Api(value = "/app/file",tags="附件功能", description = "附件品功能接口")
public class FileAppController extends BaseController{
	@Autowired
	private UploadfileService uploadfileService;
	
	@RequestMapping("/upload")
	@ResponseBody
	@ApiOperation(value = "图片保存", notes = "图片保存", httpMethod = "POST")
	public void file(HttpServletRequest request,MultipartFile file,
					@ApiParam(required = false, name = "id", value = "该条信息的主键（没有就填空）")@RequestParam(value="id",required=false)String id,
					@ApiParam(required = false, name = "modeltype", value = "图片所属的模块分类（随便取）")@RequestParam(value="modeltype",required=false)String modeltype) {				
		ResultData rd=new ResultData();
        try{
            jsonData = uploadfileService.uploadFile(request,file,id,modeltype);
            if(jsonData.equalsIgnoreCase("")){
            	rd.setCode(500);
                rd.setData(jsonData);
                rd.setMsg("操作失败");
            }else{
            	rd.setCode(200);
                rd.setData(jsonData);
                rd.setMsg("上传成功");
            }
            
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("操作失败");
        }
        this.jsonWrite2(rd);
	}
	
	@RequestMapping("/del")
	@ResponseBody
	@ApiOperation(value = "图片删除", notes = "图片删除", httpMethod = "POST")
	public void del(@ApiParam(required = false, name = "path", value = "图片路径")@RequestParam(value="path",required=false)String path) {				
		ResultData rd=new ResultData();
        try{
            uploadfileService.delFileByPath(path);
            String realPath = request.getSession().getServletContext().getRealPath("/");
			FileHelper.delFile( realPath+path);
            rd.setCode(200);
            rd.setMsg("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("操作失败");
        }
        this.jsonWrite2(rd);
	}
	
	
	@RequestMapping("/v1_1_0/upload")
	@ResponseBody
	@ApiOperation(value = "v1_1_0图片保存", notes = "v1_1_0图片保存", httpMethod = "POST")
	public void file_v1_1_0(HttpServletRequest request,MultipartFile file,
			@ApiParam(required = true, name = "token", value = "token")@RequestParam(value="token",required=true)String token,
			@ApiParam(required = false, name = "id", value = "该条信息的主键（没有就填空）")@RequestParam(value="id",required=false)String id,
					@ApiParam(required = false, name = "modeltype", value = "图片所属的模块分类（随便取）")@RequestParam(value="modeltype",required=false)String modeltype) {				
		ResultData rd=new ResultData();
        try{
//        	String token=request.getParameter("token");
    		User user =null;
    		if(null!=token&&!"".equalsIgnoreCase(token)){
    			user= JwtUtil.getUserByJson(token);
    			
    		}
    		if(user!=null){
    			jsonData = uploadfileService.uploadFile_v1_1_0(request,file,id,modeltype,user.getId());
    		}else{
    			jsonData="";
    		}
            if(jsonData.equalsIgnoreCase("")){
            	rd.setCode(500);
                rd.setData(jsonData);
                rd.setMsg("操作失败");
            }else{
            	rd.setCode(200);
                rd.setData(jsonData);
                rd.setMsg("上传成功");
            }
            
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("操作失败");
        }
        this.jsonWrite2(rd);
	}
	
	@RequestMapping("/v1_1_0/viewImage")
	@ResponseBody
	@ApiOperation(value = "v1_1_0图片查看功能，带备份还原功能", notes = "v1_1_0图片查看功能，带备份还原功能", httpMethod = "POST")
	public void viewImage_v1_1_0(HttpServletRequest request,MultipartFile file,
			@ApiParam(required = true, name = "token", value = "token")@RequestParam(value="token",required=true)String token,
			@ApiParam(required = false, name = "url", value = "存放图片的路径地址，多个用逗号分割")@RequestParam(value="url",required=false)String url) {				
		ResultData rd=new ResultData();
        try{
    		User user =null;
    		if(null!=token&&!"".equalsIgnoreCase(token)){
    			user= JwtUtil.getUserByJson(token);
    		}
    		if(user!=null){
    			uploadfileService.getImagePaths_v1_1_0(request,url);
    		}
    		jsonData=url;
            if(jsonData.equalsIgnoreCase("")){
            	rd.setCode(500);
                rd.setData(jsonData);
                rd.setMsg("操作失败");
            }else{
            	rd.setCode(200);
                rd.setData(jsonData);
                rd.setMsg("操作成功");
            }
            
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("操作失败");
        }
        this.jsonWrite2(rd);
	}
}
