package com.zpj.materials.service;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.GoodsType;
import com.zpj.sys.entity.User;

import java.util.Map;

public interface GoodsTypeService {

    public MyPage findPageData(Map canshu, Integer page, Integer limit);
    
    void saveInfo(GoodsType gt);
    
    public int delInfo(String id,User user);

    public GoodsType findById(String id);
}
