package com.zpj.sys.service;

import java.util.List;
import java.util.Map;

import com.zpj.common.MyPage;
import com.zpj.sys.entity.DictionaryItem;
import com.zpj.sys.entity.DictionaryType;

public interface DictionaryService {
		
	

	/**
	 * 查询树子节点
	 * @Title findJson
	 * @param param
	 * @return
	 * @author zpj
	 * @time 2018年4月26日 下午3:06:31
	 */
	public String findJson(Map<String, String> param);
	
	/**
	 * 查询树顶层结构
	 * @Title findTopJson
	 * @return
	 * @author zpj
	 * @time 2018年4月26日 下午3:06:20
	 */
	public String findTopJson();
	
	
	/**
	 * 初始化字典时查询所有类型数据
	 * @Title findAllDictionaryType
	 * @return
	 * @author zpj
	 * @time 2018年7月6日 下午4:25:50
	 */
	public List<DictionaryType> findAllDictionaryType();
	/**
	 * 根据id查询分类类型
	 * @Title findDicTypeId
	 * @param id
	 * @return
	 * @author zpj
	 * @time 2018年7月4日 下午2:28:53
	 */
	public DictionaryType findDicTypeId(String id);
	
	
	/**
	 * 根据编码查询dictype对象
	 * @Title findDicTypeByCode
	 * @param code
	 * @return
	 * @author zpj
	 * @time 2018年8月2日 下午4:07:52
	 */
	public DictionaryType findDicTypeByCode(String code);
	
	/**
	 * 保存字典类型
	 * @Title saveDictionaryType
	 * @param dt
	 * @author zpj
	 * @time 2018年7月4日 下午3:16:41
	 */
	public void saveDictionaryType(DictionaryType dt);
	
	/**
	 * 手机上保存
	 * @Title saveDictionaryTypeByPhone
	 * @param dt
	 * @author zpj
	 * @time 2018年8月2日 下午4:27:20
	 */
	public void saveDictionaryTypeByPhone(DictionaryType dt,String oldCode);
	/**
	 * 删除字典类型
	 * @Title delDictionaryType
	 * @param id
	 * @author zpj
	 * @time 2018年7月4日 下午4:06:13
	 */
	public void delDictionaryType(String id);
	
	
	/**
	 * 初始化item列表页面
	 * @Title findPageData
	 * @param typeCode
	 * @param page
	 * @param limit
	 * @return
	 * @author zpj
	 * @time 2018年7月5日 下午5:20:30
	 */
	MyPage findPageData(String typeCode, Integer page, Integer limit);
	
	/**
	 * 保存item
	 * @Title saveItem
	 * @param di
	 * @author zpj
	 * @time 2018年7月6日 下午1:45:28
	 */
	public void saveItem(DictionaryItem di);
	
	/**
	 * 查找id
	 * @Title findItem
	 * @param id
	 * @return
	 * @author zpj
	 * @time 2018年7月6日 下午1:49:45
	 */
	public DictionaryItem findItem(String id);
	
	/**
	 * 删除item
	 * @Title doDelItem
	 * @param ids
	 * @author zpj
	 * @time 2018年7月6日 下午3:07:06
	 */
	public void delItem(String ids);
	
	/**
	 * 初始化查询所有字典item数据
	 * @Title findAllDictionaryItem
	 * @return
	 * @author zpj
	 * @time 2018年7月6日 下午4:34:14
	 */
	public List<DictionaryItem> findAllDictionaryItem();
	
	
	
	/**
	 * 根据code编码查询当前节点下的子节点
	 * @Title findDictionaryTypeByCode
	 * @param code
	 * @return
	 * @author zpj
	 * @time 2018年12月10日 下午1:13:56
	 */
	public List findChlidrenDictionaryTypeByCode(String code);
	
	
	/**
	 * 更新缓存
	 * @Title updateDictionaryCache
	 * @author zpj
	 * @time 2018年12月11日 下午4:32:11
	 */
	public void updateDictionaryCache();
	
}
