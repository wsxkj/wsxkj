package com.zpj.common.aop;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.zpj.common.SpringContext;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.service.LogInfoService;
import com.zpj.sys.service.UploadfileService;

/**
 * 日志管理器
 */
public class LogManager {

    //日志记录操作延时
    private final int OPERATE_DELAY_TIME = 10;

    //异步操作记录日志的线程池
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
    

    private LogManager() {
    }

    public static LogManager logManager = new LogManager();

    public static LogManager me() {
        return logManager;
    }

    public void executeLog(LogInfoService logService,LogInfo info) {
        executor.schedule(this.bussinessLog(logService,info), OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }
    
    public static TimerTask bussinessLog(final LogInfoService logService,final LogInfo info) {
        return new TimerTask() {
        @Override
	        public void run() {
	            try {
	            	logService.saveLog(info);
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
	        }
	    };
    }
    
    
}
