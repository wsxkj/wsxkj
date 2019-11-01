package com.zpj.materials.service;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.GoodsBrand;
import com.zpj.sys.entity.User;

import java.util.Map;

public interface GoodsBrandService {

    public MyPage findPageData(Map canshu, Integer page, Integer limit);

    public void saveInfo(GoodsBrand goodsBrand);

    /**
     * 删除品牌信息，1返回删除成功，0已使用不能删除
     * @Title delInfo
     * @param id
     * @param user
     * @return
     * @author zpj
     * @time 2019年10月23日 下午2:43:00
     */
    public int delInfo(String id, User user);

    public MyPage findGBrandPicturePageData(Map canshu, Integer page, Integer limit);
}
