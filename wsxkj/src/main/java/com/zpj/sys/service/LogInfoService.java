package com.zpj.sys.service;

import com.zpj.common.MyPage;
import com.zpj.sys.entity.LogInfo;

public interface LogInfoService {
	
	public void saveLog(LogInfo log);

	/**
	 * 查询分页
	 * @param page
	 * @param limit
	 * @return
	 */
	public MyPage findPageData(Integer page, Integer limit);

	/**
	 * 根据id查询对象
	 * @param id
	 * @return
	 */
	public LogInfo findById(String id);

}
