package com.zpj.materials.service;

import java.util.List;
import java.util.Map;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.Store;

public interface StoreService {

    /*
     * @MethodName: findStatisticsData
     * @Description: TODO(根据月份查询总进货数，总金额)
     * @params [canshu]
     * @return java.util.Map
     * @author zpj
     * @date 2019/8/6 10:53
    */
    public Map findStatisticsData(Map canshu);
    List findMultiData(Map param, Integer page, Integer limit);

	MyPage findPageData(Map param, Integer page, Integer limit);
    /*
     * @MethodName: saveInfo
     * @Description: TODO(保存库存信息)
     * @params [store]
     * @return void
     * @author zpj
     * @date 2019/4/19 17:17
    */
    public void saveInfo(Store store);
    
    
    public Store findById(String id);
    
    /**
     * 查询库存数量
     * @Title findStoreInCount
     * @param canshu
     * @return
     * @author zpj
     * @time 2019年5月21日 下午4:44:26
     */
    public int findStoreInCount(Map canshu);
}
