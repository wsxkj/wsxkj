package com.zpj.sys.service.impl;

import com.zpj.common.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zpj.common.BaseDao;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.service.LogInfoService;

import java.util.HashMap;
import java.util.Map;

@Service
public class LogInfoServiceImpl implements LogInfoService {
	@Autowired
	private BaseDao<LogInfo> logDao;
	
	public void saveLog(LogInfo log){
		logDao.add(log);
	}

	public MyPage findPageData(Integer page, Integer limit){
		Map<String,Object> param=new HashMap<String,Object>();
		Map px=new HashMap();
		px.put("createtime", "desc");
		return logDao.findPageDateSqlT("sys_log_info","", param,px , page, limit, LogInfo.class);
	}

	public LogInfo findById(String id){
		return logDao.get(id,LogInfo.class);
	}

}
