package com.zpj.sys.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.zpj.common.BaseController;
import com.zpj.common.DateHelper;
import com.zpj.common.FileHelper;
import com.zpj.common.SpringContext;
import com.zpj.common.SystemContext;
import com.zpj.common.aop.Log;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.entity.User;
import com.zpj.sys.service.LogInfoService;
import com.zpj.sys.service.UserService;

@Controller
public class LoginController extends BaseController{
	@Autowired
	private UserService userService;
	@Autowired
	private LogInfoService logService;
	
	private User user;
	
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

	@RequestMapping("checkLogin")
	public String checkLogin(ModelMap modelMap ,String username, String password){
//		String username=null, password=null;
		Map map=getRequestMap();
		User uuser=(User)getSession().getAttribute("jluser");
		User user=null;
		param=new HashMap<String,Object>();
		if(!isNotNullObject(username)){
			username="";
		}
		if(!isNotNullObject(password)){
			password="";
		}
		if(username.equalsIgnoreCase("")&&uuser==null){
			 //服务器重启后页面非正常情况访问
			 return "forward:/login.jsp";
		}
		
		if(!username.equalsIgnoreCase("")){
			user=userService.checkLogin(username,password);
		}
		modelMap.addAttribute("url", "/");
		if(null!=user){
			getSession().setAttribute("jluser", user);
			getSession().setAttribute("USERINFO",gson.toJson(user));
			String loginIP=getIp2(request);
			LogInfo loginfo=new LogInfo();
			loginfo.setId(UUID.randomUUID().toString());
        	loginfo.setCreatetime(new Date());
        	loginfo.setType("登陆");
        	loginfo.setDescription(DateHelper.getToday("yyyy-MM-dd HH:mm:ss")+"   "+user.getPhone()+"  成功登陆系统"+",IP地址"+loginIP);
			
        	logService.saveLog(loginfo);
			
			
			
			return "/sys/home/index";
		}else{
			if(!username.equalsIgnoreCase("")){
				//说明登陆验证失败，不管session中的数据，同时置空session
				getSession().setAttribute("jluser", null);
				getSession().setAttribute("USERINFO",null);
				return "forward:/login.jsp";
			}else{
				if(uuser!=null){
					getSession().setAttribute("jluser", uuser);
					getSession().setAttribute("USERINFO",gson.toJson(uuser));
					return "/sys/home/index";
				}
			}
			return "forward:/login.jsp";
		}
	}
	public  String getIp2(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
	
	@RequestMapping("loginOut")
	public String exitLogin(){
		getSession().removeAttribute("jluser");
		getSession().removeAttribute("USERINFO");
		return "forward:/login.jsp";
	}
	
	
	
	@RequestMapping("downloadApp")
	@ResponseBody
	public void  downloadApp() throws Exception {
		WebApplicationContext webApplicationContext = ContextLoader
                .getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext
                .getServletContext();
        // 得到文件绝对路径
        String realPath = servletContext.getRealPath("/download")+"/店铺管理.apk";
        System.out.println(realPath);
		try{
			boolean flag= FileHelper.downloadFile(realPath, "店铺管理.apk", response);
			LogInfo loginfo=new LogInfo();
			loginfo.setId(UUID.randomUUID().toString());
			loginfo.setUsername("朱培军");
			loginfo.setCreatetime(new Date());
			loginfo.setType("下载app");
			loginfo.setDescription("下载文件路径为："+realPath);
			logService.saveLog(loginfo);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
	}
	
}
