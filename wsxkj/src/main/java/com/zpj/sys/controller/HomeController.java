package com.zpj.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zpj.common.BaseController;
@Controller
@RequestMapping("/home")
public class HomeController extends BaseController{
	
	@RequestMapping("/toIndex")
	public String toIndex(){
		return "/sys/home/index";
	}
	
	@RequestMapping("/test")
	public String test(){
		return "/sys/home/test";
	}
	
	@RequestMapping("/toMain")
	public String toMain(){
		return "sys/home/main";
	}
}
