package com.zpj.sys.service;

import java.util.Map;

import com.zpj.common.MyPage;
import com.zpj.sys.entity.Level;

public interface LevelService {
	MyPage findPageData(Map param, Integer page, Integer limit);

	public void saveInfo(Level level);
	
	public Level findInfoById(Integer level);
}
