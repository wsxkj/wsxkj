package com.zpj.materials.service.impl;

import java.util.*;

import com.zpj.sys.entity.LogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.common.aop.Log;
import com.zpj.materials.entity.Goods;
import com.zpj.materials.service.GoodsService;
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private BaseDao<Goods> goodsDao;
	@Autowired
	private BaseDao<LogInfo> logDao;

	private String tablename="jl_material_goods_info";
	
	@Override
	public MyPage findPageData(Map canshu, Integer page, Integer limit) {
		Map param=new HashMap();
		if(null!=canshu.get("name")&&!"".equalsIgnoreCase((String)canshu.get("name"))){
			param.put("name-like", canshu.get("name"));
		}
		if(null!=canshu.get("typeId")&&!"".equalsIgnoreCase((String)canshu.get("typeId"))){
			param.put("goodsType-like", canshu.get("typeId"));
		}
		if(null!=canshu.get("brandId")&&!"".equalsIgnoreCase((String)canshu.get("brandId"))){
			param.put("goodsBrand-like", canshu.get("brandId"));
		}
		if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
			param.put("userId-eq", canshu.get("userId"));
		}
		Map px=new HashMap();
	    px.put("createtime", "desc");
		return goodsDao.findPageDateSqlT(tablename,"", param,px , page, limit, Goods.class);
	}
	
	public List findMultiGoods(Map canshu, Integer page, Integer limit){
		StringBuffer sql=new StringBuffer(500);
		sql.append(" select g.id,g.name,t1.minp,t1.maxp,g.storeNum,g.picture from jl_material_goods_info g LEFT JOIN (select MIN(outPrice) as minp,MAX(outPrice) as maxp,goodsId from jl_material_store_info GROUP BY goodsId) t1 on t1.goodsId=g.id where 1=1 ");
		if(null!=canshu.get("name")&&!"".equalsIgnoreCase((String)canshu.get("name"))){
			sql.append(" and g.name like '"+canshu.get("name")+"%' ");
		}
		if(null!=canshu.get("typeId")&&!"".equalsIgnoreCase((String)canshu.get("typeId"))){
			sql.append(" and g.goodsType='"+canshu.get("typeId")+"'");
		}
		if(null!=canshu.get("brandId")&&!"".equalsIgnoreCase((String)canshu.get("brandId"))){
			sql.append(" and g.goodsBrand='"+canshu.get("brandId")+"'");
		}
		if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
			sql.append(" and g.userId='"+ canshu.get("userId")+"'");
		}
		List list=goodsDao.findMapObjBySql(sql.toString(),null , page, limit);
		return list;
	}
	

	@Log(type="保存",remark="保存商品信息")
	public void saveInfo(Goods info) {
			Goods gg=this.findById(info.getId());
			if(null!=gg){
				goodsDao.merge(info, String.valueOf(info.getId()));
			}else{
				goodsDao.add(info);
			}
	}

//	@Log(type="删除",remark="删除商品信息")
	public void deleteInfo(String deleteID) {
		String[] ids=deleteID.split(",");
		StringBuffer sb=new StringBuffer(500);
		for (int m=0;m<ids.length;m++) {
			if(m>0){
				sb.append(",");
			}
			sb.append("'"+ids[m]+"'");
		}

		List list=goodsDao.findBySqlT(" select *  from "+tablename+" where id in ("+sb+")", Goods.class);
		Goods goods=null;
		if(null!=list&&list.size()>0) {
			for (int m = 0; m < list.size(); m++) {
				goods = (Goods) list.get(m);
				LogInfo loginfo = new LogInfo();
				loginfo.setId(UUID.randomUUID().toString());
				loginfo.setUsername("朱培军");
				loginfo.setCreatetime(new Date());
				loginfo.setType("删除商品记录");
				loginfo.setDescription(goods.toString());
				logDao.add(loginfo);
			}
		}
		goodsDao.executeSql(" delete from "+tablename+" where id in ("+sb+")");
	}

	public Goods findById(String id) {
		Goods g=goodsDao.get(id,Goods.class);
		//售价最高价和最低价
		StringBuffer sql=new StringBuffer(100).append("select * from (select MIN(outPrice) as minp,MAX(outPrice) as maxp,goodsId from jl_material_store_info GROUP BY goodsId ) t  where t.goodsId='"+id+"' ");
		List<Map> list=goodsDao.findMapObjBySqlNoPage(sql.toString());
		if(null!=list&&list.size()>0){
			g.setMaxOutPrice((double)list.get(0).get("maxp"));
			g.setMinOutPrice((double)list.get(0).get("minp"));
		}
		//进价最高价和最低价
		sql=new StringBuffer(100).append("select * from (select MIN(inPrice) as minp,MAX(inPrice) as maxp,goodsId from jl_material_store_info GROUP BY goodsId ) t  where t.goodsId='"+id+"' ");
		list=goodsDao.findMapObjBySqlNoPage(sql.toString());
		if(null!=list&&list.size()>0){
			g.setMaxInPrice((double)list.get(0).get("maxp"));
			g.setMinInPrice((double)list.get(0).get("minp"));
		}
		//进货总数
		sql=new StringBuffer(100).append("select sum(inNum) as totalInNum,goodsId from jl_material_store_info   where goodsId='"+id+"' ");
		list=goodsDao.findMapObjBySqlNoPage(sql.toString());
		if(null!=list&&list.size()>0){
			g.setTotalInNum((double)list.get(0).get("totalInNum"));
		}
		//销售总数量和总销售额
		sql=new StringBuffer(100).append("select sum(soldNum) as totalSoldNum,sum(soldTotalPrice) as totalSoldPrice,goodsId from jl_material_order_goods_info  where goodsId='"+id+"' ");
		list=goodsDao.findMapObjBySqlNoPage(sql.toString());
		if(null!=list&&list.size()>0){
			g.setTotalSoldNum((double)list.get(0).get("totalSoldNum"));
			g.setTotoalSoldPrice((double)list.get(0).get("totalSoldPrice"));
		}
		return g;
	}	

}
