package com.zpj.materials.service.impl;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.materials.entity.GoodsBrand;
import com.zpj.materials.service.GoodsBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/*
 * @ClassName: GoodsBrandServiceImpl
 * @Description: TODO(商品品牌)
 * @author zpj
 * @date 2019/4/19 16:34
*/
@Service
public class GoodsBrandServiceImpl implements GoodsBrandService {
    @Autowired
    private BaseDao<GoodsBrand> goodsBrandDao;

    private String tablename="jl_material_goods_brand_info";


    public MyPage findPageData(Map canshu, Integer page, Integer limit) {
        Map param=new HashMap();
        if(null!=canshu.get("name")&&!"".equalsIgnoreCase((String)canshu.get("name"))){
            param.put("name-like", canshu.get("name"));
        }
        if(null!=canshu.get("typeId")&&!"".equalsIgnoreCase((String)canshu.get("typeId"))){
            param.put("typeId-eq", canshu.get("typeId"));
        }
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            param.put("userId-eq", canshu.get("userId"));
        }

        Map px=new HashMap();
        px.put("updateTime", "desc");
        return goodsBrandDao.findPageDateSqlT(tablename,"", param,px , page, limit, GoodsBrand.class);
    }
}
