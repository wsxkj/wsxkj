package com.zpj.common;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zpj.jwt.JwtUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zpj.sys.entity.User;

import io.jsonwebtoken.JwtException;


public class SetCharacterFilter implements Filter{

	protected String endcoding = "UTF-8";
	protected FilterConfig filterConfig = null;
	public Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create(); 
	
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		req.setCharacterEncoding(endcoding);


		/*res.setHeader("Access-Control-Allow-Origin", "*"); 
		res.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
		res.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");*/
		String str_href = this.getCurrentURL(req);	
		boolean flag=judgeIsPass(str_href);
		if(flag){
			//直接通过
			chain.doFilter(request, response);
		}else{
//			//手机请求
			if(str_href.indexOf("app")>-1){
				if(str_href.indexOf("app/login")>-1||str_href.indexOf("app/file")>-1||str_href.indexOf("v1_1_0/findGoodsByIdH5")>-1||str_href.indexOf("v1_1_0/searchGoodsListH5")>-1){
					chain.doFilter(request, response);
				}else{
					User user=getCurrentUser(req);
					if(user==null){
						ResultData rd=new ResultData();
						rd.setCode(500);
			            rd.setMsg("token转码失败，token过期，请重新登陆。");
			            res.setCharacterEncoding("utf-8");
			            res.setContentType("text/html;charset=UTF-8");	
			            res.setHeader("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
			            res.setHeader("Access-Control-Allow-Origin", "*");
			            res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
			            res.getWriter().write(gson.toJson(rd));
					}else{
						req.getSession().setAttribute("jluser",user);
						chain.doFilter(request, response);
					}
				}
			}else{
				chain.doFilter(request, response);
			}
		}
	}
	public boolean judgeIsPass(String spath){
		String[] urls = {"downloadApp","controller.jsp","file","v2","api-docs","swagger","druid","404","500",".js",".css",".ico",".jpeg",".bmp",".jpg",".png",".gif",".htm",".html",".woff",".woff2",".ttf",".mp3",".mp4",".mov",".avi"};
        boolean flag = false;
    	for (String str : urls) {
            if (spath.indexOf(str) != -1) {
                flag =true;
                break;
            }
        }
    	return flag;
        
	}
	public User getCurrentUser(HttpServletRequest request) throws JwtException{
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
			
		}
//		if(null==user){
//			throw new JwtException("用户未登陆");
//		}
		return user;
	}
    public void init(FilterConfig config) throws ServletException {
    	ApplicationContext ac = WebApplicationContextUtils
    			.getWebApplicationContext(config.getServletContext());
    	this.filterConfig=config;
		this.endcoding = filterConfig.getInitParameter("encoding");
//		PropertyHelper.init();
	}
    private String getCurrentURL(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append(request.getServletPath());
		String queryString = request.getQueryString();
		if (queryString != null && !queryString.equals("")) {
			sb.append("?");
			sb.append(queryString);
		}
		return sb.toString();
	}
	public void destroy() {
		this.endcoding = null;
		this.filterConfig = null;
	}
	
}
