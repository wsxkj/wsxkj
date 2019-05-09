package com.zpj.materials.service.impl;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.materials.entity.Customer;
import com.zpj.materials.entity.Goods;
import com.zpj.materials.entity.GoodsType;
import com.zpj.materials.service.GoodsTypeService;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * @ClassName: GoodsTypeServiceImpl
 * @Description: TODO(商品分类)
 * @author zpj
 * @date 2019/4/18 16:54
*/
@Service
public class GoodsTypeServiceImpl implements GoodsTypeService {
    @Autowired
    private BaseDao<GoodsType> goodsTypeDao;

    private String tablename="jl_material_goods_type_info";
    
    @Autowired
    private BaseDao<LogInfo> logDao;


    public MyPage findPageData(Map canshu, Integer page, Integer limit) {
        Map param=new HashMap();
        if(null!=canshu.get("name")&&!"".equalsIgnoreCase((String)canshu.get("name"))){
            param.put("name-like", canshu.get("name"));
        }
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            param.put("userId-eq", canshu.get("userId"));
        }

        Map px=new HashMap();
        px.put("updateTime", "desc");
        return goodsTypeDao.findPageDateSqlT(tablename, param,px , page, limit, GoodsType.class);
    }


	@Override
	public void saveInfo(GoodsType gt) {
		goodsTypeDao.add(gt);
	}
	
	public GoodsType findById(String id){
		return goodsTypeDao.get(id, GoodsType.class);
	}

	@Override
	public void delInfo(String id, User user) {
		GoodsType temp=this.findById(id);
    	if(null!=temp){
    		goodsTypeDao.delete(temp);
    		LogInfo loginfo=new LogInfo();
    		loginfo.setId(UUID.randomUUID().toString());
    		loginfo.setUsername(user.getId());
    		loginfo.setCreatetime(new Date());
    		loginfo.setType("删除商品类型信息");
    		loginfo.setDescription(temp.toString());
    		logDao.add(loginfo);
    	}
	}
}
