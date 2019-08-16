package com.zpj.materials.service;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.GoodsBrand;
import com.zpj.sys.entity.User;

import java.util.Map;

public interface GoodsBrandService {

    public MyPage findPageData(Map canshu, Integer page, Integer limit);

    public void saveInfo(GoodsBrand goodsBrand);

    void delInfo(String id, User user);

    public MyPage findGBrandPicturePageData(Map canshu, Integer page, Integer limit);
}
