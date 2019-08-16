package com.zpj.materials.service.impl;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.materials.entity.GoodsBrand;
import com.zpj.materials.entity.GoodsBrandPicture;
import com.zpj.materials.service.GoodsBrandService;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

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
    @Autowired
    private BaseDao<LogInfo> logDao;
    private String tablename="jl_material_goods_brand_info";

    @Autowired
    private BaseDao<GoodsBrandPicture> goodsBrandPictureBaseDao;

    private String tablename_picture="jl_material_goods_brand_picture_info";


    public MyPage findPageData(Map canshu, Integer page, Integer limit) {
        Map param=new HashMap();
        StringBuilder sql=new StringBuilder(200);
        sql.append(" select a.*,b.pictureURL from "+tablename+" a left join "+tablename_picture+" b on a.pictureId=b.id where 1=1 ");
        StringBuilder whereSql=new StringBuilder(200);
        if(null!=canshu.get("name")&&!"".equalsIgnoreCase((String)canshu.get("name"))){
            whereSql.append(" and name like '"+ canshu.get("name")+"%' ");
        }
        if(null!=canshu.get("typeId")&&!"".equalsIgnoreCase((String)canshu.get("typeId"))){
            whereSql.append(" and typeId = '"+ canshu.get("typeId")+"' ");
        }
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            whereSql.append(" and userId = '"+ canshu.get("userId")+"' ");
        }
        MyPage my=new MyPage();
        List list=goodsBrandDao.findMapObjBySql(sql.toString()+whereSql.toString(),null,page,limit);
        my.setData(list);
        List<Object[]> lt = goodsBrandDao.findBySql("select count(*) as num,1 from "+tablename+" where 1=1 "+whereSql);
        if (lt != null) {
            my.setCount(Integer.parseInt(((BigInteger) lt.get(0)[0]).toString()));
        }
        return my;
    }
    public GoodsBrand findById(String id){
        return goodsBrandDao.get(id, GoodsBrand.class);
    }
    public void saveInfo(GoodsBrand goodsBrand){
        GoodsBrand temp=findById(goodsBrand.getId());
        if(null!=temp){
            goodsBrandDao.merge(goodsBrand,goodsBrand.getId());
        }else{
            goodsBrandDao.add(goodsBrand);
        }
    }
    public void delInfo(String id, User user){
        GoodsBrand temp=this.findById(id);
        if(null!=temp){
            goodsBrandDao.delete(temp);
            LogInfo loginfo=new LogInfo();
            loginfo.setId(UUID.randomUUID().toString());
            loginfo.setUsername(user.getId());
            loginfo.setCreatetime(new Date());
            loginfo.setType("删除商品品牌信息");
            loginfo.setDescription(temp.toString());
            logDao.add(loginfo);
        }
    }


    public MyPage findGBrandPicturePageData(Map canshu, Integer page, Integer limit) {
        Map px=new HashMap();
        px.put("updateTime", "desc");
        return goodsBrandPictureBaseDao.findPageDateSqlT(tablename_picture,"", null,px , page, limit, GoodsBrandPicture.class);
    }

}
