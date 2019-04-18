package com.zpj.materials.service;

import com.zpj.materials.entity.IdCodeInfo;

public interface IdCodeService {
	/**
	 * 保存数据
	 *
	 * @param ici
	 * @Title saveInfo
	 * @author zpj
	 * @time 2019年4月17日 下午2:28:52
	 */
	public void saveInfo(IdCodeInfo ici);

	/*
	 * @MethodName: findInfoByPhone
	 * @Description: TODO(根据手机号码查询验证码信息)
	 * @params [phone]
	 * @return com.zpj.materials.entity.IdCodeInfo
	 * @author zpj
	 * @date 2019/4/18 9:02
	 */
	public IdCodeInfo findInfoByPhone(String phone);
}
