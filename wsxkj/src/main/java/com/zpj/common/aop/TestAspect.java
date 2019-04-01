package com.zpj.common.aop;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zpj.common.BaseDao;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.entity.User;
import com.zpj.sys.service.LogInfoService;




@Aspect
@Component
public class TestAspect {
	
	
//	public TestAspect(){
//		System.out.println("实例化TestAspect");
//	}
	@Autowired
	private LogInfoService logService;
	
	/**
	 * 配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
	 * （* com.evan.crm.service.*.*（..））中几个通配符的含义： 
		|第一个 * —— 通配 随便返回值类型| 
		|第二个 * —— 通配包com.evan.crm.service下的随便class| 
		|第三个 * —— 通配包com.evan.crm.service下的随便class的随便办法| 
		|第四个 .. —— 通配 办法可以有0个或多个参数| 
	 * @Title aspect
	 * @author zpj
	 * @time 2017-2-28 上午10:14:41
	 */
//	@Pointcut("@within(com.common.aop.AopMethod)")
//	@Pointcut("execution(* com.basesystem.service.impl.DynamicServiceImpl.*(..))")
//	@Pointcut("execution(* com.*.service.*.*(..))")
	@Pointcut(value="@annotation(com.zpj.common.aop.Log)")
	public void haha(){
	}
	
	/**
	 * 配置前置通知，使用在方法aspect()上注册的切入点
	 * 同时接受JoinPoint切入点对象可以没有该参数
	 * @Title before
	 * @param jp
	 * @author zpj
	 * @time 2017-2-28 上午10:14:51
	 */
	@Before("haha()")
	public void before(JoinPoint jp){
//		System.out.println("before:"+jp);
	}
	/**
	 * 配置环绕通知，使用在方法aspect()上注册的切入点
	 * @Title around
	 * @param joinPoint
	 * @return
	 * @author zpj
	 * @time 2017-5-26 下午4:45:46
	 */
	@Around("haha()")
	public Object around(JoinPoint joinPoint){
		Object result=null;
		ProceedingJoinPoint pjp=(ProceedingJoinPoint) joinPoint;
		try {
			result=pjp.proceed();
			insertLog(pjp,"操作成功");
		} catch (Throwable e) {
			try {
				insertLog(pjp,"操作失败");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}
	
	public void insertLog(ProceedingJoinPoint pjp,String result) throws Exception{
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		User userinfo=null;
        if(request!=null){
	         userinfo = (User) request.getSession().getAttribute("jluser");
        }
		Map tmap = getMthodRemark(pjp);
		if(userinfo!=null){
			LogInfo loginfo=new LogInfo();
			loginfo.setId(UUID.randomUUID().toString());
        	loginfo.setUsername(userinfo.getName());
        	loginfo.setCreatetime(new Date());
        	loginfo.setType((String)tmap.get("type"));
        	loginfo.setDescription(userinfo.getName()+"  在"+(String)tmap.get("remark")+","+result);
        	logService.saveLog(loginfo);
        }
	}
	
	 /**
	 * 自定义方法回去备注、和类型
	 * @Title getMthodRemark
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 * @author zpj
	 * @time 2017-5-26 下午5:15:50
	 */
	public static Map getMthodRemark(ProceedingJoinPoint joinPoint)  
	            throws Exception {  
	        String targetName = joinPoint.getTarget().getClass().getName();  
	        String methodName = joinPoint.getSignature().getName();  
	        Object[] arguments = joinPoint.getArgs();  
	  
	        Class targetClass = Class.forName(targetName);  
	        Method[] method = targetClass.getMethods();  
	        Map<String,String> retMap = new HashMap<String,String>();
	        for (Method m : method) {  
	            if (m.getName().equals(methodName)) {  
	                Class[] tmpCs = m.getParameterTypes();  
	                if (tmpCs.length == arguments.length) {  
	                	Log methodCache = m.getAnnotation(Log.class); 
	                    if(methodCache!=null){
	                    	retMap.put("remark", methodCache.remark());  
	                    	retMap.put("type", methodCache.type());
	                    }
	                    break;
	                }  
	            }  
	        }  
	        return retMap;  
	    }  
	    
	  
	
}
