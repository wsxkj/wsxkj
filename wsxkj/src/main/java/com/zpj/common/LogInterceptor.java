package com.zpj.common;

import org.springframework.stereotype.Component;

@Component
public class LogInterceptor {
    public void before(){  
        System.out.println("login start!");  
    }  
      
    public void after(){  
    	System.out.println("login end!");  
    }  
}
