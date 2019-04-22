package com.zpj.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zpj.jwt.JwtUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zpj.sys.entity.User;


public class SetCharacterFilter implements Filter{

	protected String endcoding = "UTF-8";
	protected FilterConfig filterConfig = null;
	
	
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
			if(str_href.equalsIgnoreCase("/")||str_href.equalsIgnoreCase("/checkLogin")||str_href.equalsIgnoreCase("/logOut")){
					chain.doFilter(request, response);
	        }else{
	        	User curruser= (User) req.getSession().getAttribute("jluser");
	        	if(null!=curruser){
					chain.doFilter(request, response);
				}else{
					req.getRequestDispatcher("/404.jsp").forward(req, res);
				}
	        } 
		}else{
			//手机请求
			if(str_href.indexOf("app")>-1){
				if(str_href.indexOf("app/login")>-1){
					chain.doFilter(request, response);
				}else{
					String token=req.getParameter("token");
					User user= JwtUtil.getUserByJson(token);
					if(null!=user){
						req.getSession().setAttribute("jluser",user);
						chain.doFilter(request, response);
					}else{
						req.getRequestDispatcher("/404.jsp").forward(req, res);
					}
				}
			}else{
				chain.doFilter(request, response);
			}
		}
	}
	public boolean judgeIsPass(String spath){
		String[] urls = {"downloadApp","controller.jsp","file","v2","swagger","druid","app","404","500",".js",".css",".ico",".jpeg",".bmp",".jpg",".png",".gif",".htm",".html",".woff",".woff2",".ttf",".mp3",".mp4",".mov",".avi"};
        boolean flag = true;
    	for (String str : urls) {
            if (spath.indexOf(str) != -1) {
                flag =false;
                break;
            }
        }
    	return flag;
        
	}
	
    public void init(FilterConfig config) throws ServletException {
    	ApplicationContext ac = WebApplicationContextUtils
    			.getWebApplicationContext(config.getServletContext());
    	this.filterConfig=config;
		this.endcoding = filterConfig.getInitParameter("encoding");
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
