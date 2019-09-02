package com.zpj.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zpj.jwt.JwtUtil;
import com.zpj.sys.entity.User;

import io.jsonwebtoken.JwtException;
import net.sf.json.JSONObject;

public class BaseController {
	//返回的json变量
    public String jsonData="";
    
    @Autowired  
	public  HttpServletRequest request;

	@Autowired(required=false)
	public  HttpServletResponse response;
	
	public Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	public Integer page = 1; // 当前页数
	public Integer limit = 10; // 行数
	
	
	  
	//参数封装  
	public Map<String,Object> param=new HashMap<String,Object>();
	

	public Integer getPage() {
		return page;
	}




	public void setPage(Integer page) {
		this.page = page;
	}




	public Integer getLimit() {
		return limit;
	}




	public void setLimit(Integer limit) {
		this.limit = limit;
	}




	public HttpSession getSession() {
		return request.getSession();
	}

	
    
    
	/**
	 * springmvc 对表单的日期类型的特殊处理
	 * @param binder
	 */
    @InitBinder    
    public void initBinder(WebDataBinder binder) {
    	binder.registerCustomEditor(Date.class, new DateEditor());
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");    
//            dateFormat.setLenient(false);    
//            binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));    
    }
    /*
	 * 获取request参数
	 */
	public String getParameter(String key){
		return request.getParameter(key);
	}
	public Integer getParameterInt(String key){
		return Integer.parseInt(request.getParameter(key));
	}

	/*
	 * 设置request参数
	 */
	public void setRequstAttribute(String key,Object value){
		request.setAttribute(key, value);
	}
	


	public HashMap<String, String> getRequestMap() {  
        HashMap<String, String> conditions = new HashMap<String, String>();  
        Map map = request.getParameterMap();  
        for (Object o : map.keySet()) {  
            String key = (String) o;  
            if(key.equalsIgnoreCase("page")){
            	String val=((String[]) map.get(key))[0];
            	if(val!=null&&!"".equals(val)){
        			page=Integer.parseInt(val);
        		}
            	conditions.put(key, page+"");
            }if(key.equalsIgnoreCase("limit")){
            	String val=((String[]) map.get(key))[0];
            	if(val!=null&&!"".equals(val)){
            		limit=Integer.parseInt(val);
        		}
            	conditions.put(key, limit+"");
            }else{
            	conditions.put(key, ((String[]) map.get(key))[0]);  
            }
        }  
        return conditions;  
    }  
	
	/**
	 * 类转json
	 * @param object
	 * @return
	 */
	public String tojson(Object object){	
		Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); 
		String str = gson.toJson(object);
		return str;
	}
	/**
	 * 输出字符
	 * @param str
	 */
	public void write(String str){	
		try{
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");	
		response.getWriter().write(str);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 传入json格式字符
	 * @param str
	 */
	public void jsonWrite(String str){		
		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("json/application;charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
			response.getWriter().write(str);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 传入list，实体等
	 * @param object
	 */
	public void jsonWrite2(Object object){		
		try{
			Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); 
			String str = gson.toJson(object);
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");	
			response.setHeader("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
			response.getWriter().write(str);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void jsonWrite3(Object object){		
		try{
//			Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); 
//			SerializeConfig config = new SerializeConfig();
//			config.put(Date.class, new DateJsonSerializer());
			com.alibaba.fastjson.JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
			String str=com.alibaba.fastjson.JSON.toJSONString(object, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteDateUseDateFormat);
			//			String str = gson.toJson(object);
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");	
			response.setHeader("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
			response.getWriter().write(str);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isNotNullObject(String str){
		if(null!=str&&!"".equalsIgnoreCase(str)&&!"null".equalsIgnoreCase(str)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 过滤空数据
	 * @Title filterStr
	 * @param str
	 * @return
	 * @author zpj
	 * @time 2019年5月8日 上午11:10:00
	 */
	public String filterStr(String str){
		if(null!=str&&!"".equalsIgnoreCase(str)&&!"null".equalsIgnoreCase(str)){
			return str;
		}else{
			return "";
		}
	}
	/**
	 * 判断空字符串
	 * @Title judgeStr
	 * @param str
	 * @return
	 * @author zpj
	 * @time 2019年5月14日 上午9:42:51
	 */
	public boolean judgeStr(String str){
		if(null!=str&&!"".equalsIgnoreCase(str)&&!"null".equalsIgnoreCase(str)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 返回当前用户
	 * @Title getCurrentUser
	 * @return
	 * @author zpj
	 * @time 2019年5月27日 下午4:09:40
	 */
	public User getCurrentUser() throws JwtException{
		String token=request.getParameter("token");
		User user =null;
		if(null!=token&&!"".equalsIgnoreCase(token)){
			user= JwtUtil.getUserByJson(token);
			if(user.getLevel()>0&&user.getLevel()<4){
				Date endtime=user.getEndTime();
				Date now=new Date();
				if(now.compareTo(endtime)<0){
					user.setIsExpire("0");
				}else{
					user.setIsExpire("1");
				}
			}
			if(null==user){
				throw new JwtException("用户未登陆");
			}
		}
		return user;
	}
}
