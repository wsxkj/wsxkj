package com.zpj.sys.service;

import com.zpj.common.MyPage;
import com.zpj.sys.entity.User;

public interface UserService {
	MyPage findPageData(String username, Integer page, Integer limit);
	
	void saveInfo(User info);

	void deleteUser(String deleteID);
	
	public User findById(String id);
	
	/**
	 * 登陆验证
	 * @Title checkLogin
	 * @param username
	 * @param password
	 * @return
	 * @author zpj
	 * @time 2018年4月17日 下午2:32:12
	 */
	public User checkLogin(String username,String password);
	
}
