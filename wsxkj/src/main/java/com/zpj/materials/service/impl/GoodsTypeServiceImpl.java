package com.zpj.materials.service.impl;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.common.aop.Log;
import com.zpj.materials.entity.Goods;
import com.zpj.materials.entity.GoodsBrand;
import com.zpj.materials.entity.GoodsType;
import com.zpj.materials.service.GoodsTypeService;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        return goodsTypeDao.findPageDateSqlT(tablename,"", param,px , page, limit, GoodsType.class);
    }


    @Log(type="保存",remark="保存商品类型信息")
	public void saveInfo(GoodsType gt) {
		GoodsType temp=findById(gt.getId());
		if(null!=temp){
			goodsTypeDao.merge(gt,gt.getId());
		}else{
			goodsTypeDao.add(gt);
		}
	}
	
	public GoodsType findById(String id){
		return goodsTypeDao.get(id, GoodsType.class);
	}

	@Override
	public int delInfo(String id, User user) {
		GoodsType temp=this.findById(id);
    	if(null!=temp){
    		List  list =goodsTypeDao.findMapObjBySqlNoPage(" select id,typeId from jl_material_goods_brand_info where typeId='"+id+"'");
    		if(list!=null&&list.size()>0){
    			// 该分类有与之关联的品牌信息
    			return 2;
    		}else{
    			List list1=goodsTypeDao.findMapObjBySqlNoPage(" select id,goodsType from jl_material_goods_info where goodsType ='"+id+"' ");
    			if(list1!=null&&list1.size()>0){
    				//该分类有与之关联的商品信息
    				return 3;
    			}else{
    				goodsTypeDao.delete(temp);
    				LogInfo loginfo=new LogInfo();
    	    		loginfo.setId(UUID.randomUUID().toString());
    	    		loginfo.setUsername(user.getId());
    	    		loginfo.setCreatetime(new Date());
    	    		loginfo.setType("删除商品类型信息");
    	    		loginfo.setDescription(temp.toString());
    	    		logDao.add(loginfo);
    	    		return 1;
    			}
    			
    		}
    	}else{
    		return 1;
    	}
	}
}
