package com.zpj.sys.service;

import java.util.Map;

import com.zpj.common.MyPage;

public interface LevelService {
	MyPage findPageData(Map param, Integer page, Integer limit);
}
