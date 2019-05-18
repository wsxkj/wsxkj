package com.zpj.materials.service;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.Customer;
import com.zpj.sys.entity.User;

import java.util.Map;

/*
 * @ClassName: CustomerService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zpj
 * @date 2019/5/5 11:31
*/
public interface CustomerService {
    MyPage findPageData(Map param, Integer page, Integer limit);

    void saveInfo(Customer customer);
    
    void delInfo(Customer customer);
    
    Customer findById(String id);
}
