package com.zpj.materials.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.common.aop.Log;
import com.zpj.materials.entity.OrderGoodsInfo;
import com.zpj.materials.entity.OrderInfo;
import com.zpj.materials.service.OrderGoodsService;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.entity.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class OrderGoodsServiceImpl implements OrderGoodsService {
	@Autowired
	private BaseDao<OrderGoodsInfo> orderGoodsDao;
	private String tablename="jl_material_order_goods_info";
    @Autowired
    private BaseDao<LogInfo> logDao;

    public int  findOrderGoodsOutCount(Map canshu){
        Map param=new HashMap();
        StringBuilder sql=new StringBuilder(100);
        sql.append("select sum(soldNum) as sn from "+tablename +" where 1=1 ");
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            sql.append(" and userId='"+canshu.get("userId")+"'") ;
        }
        if(null!=canshu.get("startTime")&&!"".equalsIgnoreCase((String)canshu.get("startTime"))){
            sql.append(" and updateTime>='"+canshu.get("startTime")+"'") ;
        }
        if(null!=canshu.get("endTime")&&!"".equalsIgnoreCase((String)canshu.get("endTime"))){
            sql.append(" and updateTime<='"+canshu.get("endTime")+"'") ;
        }
        List list=orderGoodsDao.findMapObjBySqlNoPage(sql.toString());
        return 0;
    }
	@Override
	public MyPage findPageData(Map canshu, Integer page, Integer limit) {
		Map param=new HashMap();
        if(null!=canshu.get("orderId")&&!"".equalsIgnoreCase((String)canshu.get("orderId"))){
            param.put("orderId-eq", canshu.get("orderId"));
        }
//        if(null!=canshu.get("goodsId")&&!"".equalsIgnoreCase((String)canshu.get("goodsId"))){
//            param.put("goodsId-eq", canshu.get("goodsId"));
//        }
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            param.put("userId-eq", canshu.get("userId"));
        }
        if(null!=canshu.get("startTime")&&!"".equalsIgnoreCase((String)canshu.get("startTime"))){
            param.put("updateTime-begin", canshu.get("startTime"));
        }
        if(null!=canshu.get("endTime")&&!"".equalsIgnoreCase((String)canshu.get("endTime"))){
            param.put("updateTime-end", canshu.get("endTime"));
        }
        Map px=new HashMap();
        px.put("updateTime", "desc");
        return orderGoodsDao.findPageDateSqlT(tablename,"", param,px , page, limit, OrderGoodsInfo.class);
	}
	
	public MyPage findPageDataMuti(Map canshu, Integer page, Integer limit){
		StringBuffer sql=new StringBuffer(200).append("select o.*,c.name as goodsName,c.picture as goodsPicture,s.updateTime as storeTime ");
    	StringBuffer sqlcount =new StringBuffer(100).append("select count(*) as num,1  ");
    	StringBuffer wheresql=new StringBuffer(100);
    	wheresql.append("  from "+tablename +" o left join jl_material_goods_info c on o.goodsId=c.id left join jl_material_store_info s on s.id=o.storeId where 1=1 ");
    	if(null!=canshu.get("orderId")&&!"".equalsIgnoreCase((String)canshu.get("orderId"))){
    		wheresql.append(" and o.orderId='"+canshu.get("orderId")+"' ");
        }
        
        sql.append(wheresql).append(" order by o.updateTime desc ");
    	List list=orderGoodsDao.findMapObjBySql(sql.toString(), null, page, limit);
    	
    	sqlcount.append(wheresql);
		List<Object[]> lt = orderGoodsDao.findBySql(sqlcount.toString());
		int num=0;
		if (lt != null) {
			num=Integer.parseInt(((BigInteger) lt.get(0)[0]).toString());
		}
    	MyPage my=new MyPage();
    	my.setData(list);
    	my.setCount(num);
    	return my;
		
	}
	
	
	public List findOrderListByGoodsId(Map canshu, Integer page, Integer limit){
		StringBuffer sql=new StringBuffer(500);
		
		sql.append(" select t.id,c.nickname,g.soldTotalPrice,t.updateTime from  jl_material_order_info t LEFT JOIN jl_material_customer_info c on c.id=t.customerId LEFT JOIN jl_material_order_goods_info g on g.orderId=t.id  where 1=1 ");
		if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
			sql.append(" and t.userId='"+canshu.get("userId")+"'");
        }
		if(null!=canshu.get("goodsId")&&!"".equalsIgnoreCase((String)canshu.get("goodsId"))){
			sql.append("and g.goodsId='"+canshu.get("goodsId")+"' ");
        }
		sql.append(" order by t.updateTime desc");
		List list=orderGoodsDao.findMapObjBySql(sql.toString(), null, page, limit);
        return list;
	}
	
	@Log(type="保存",remark="保存订单商品信息")
    public void saveInfo(OrderGoodsInfo orderGoodsInfo){
        orderGoodsDao.add(orderGoodsInfo);
    }
	
    public void deleteOrderGoodsInfoByOrderId(String orderId){
    	try{
	    	Map param=new HashMap();
	        param.put("orderId",orderId);
	        MyPage mp=this.findPageData(param,1,50);
	        List<OrderGoodsInfo> list =(List<OrderGoodsInfo>)mp.getData();
	        for(int m=0;m<list.size();m++){
	            if(judgeStr(list.get(m).getStoreId())){
	                double soldNum=list.get(m).getSoldNum();
	                orderGoodsDao.executeSql(" update jl_material_store_info set storeNum=storeNum+"+soldNum+" where id='"+list.get(m).getStoreId()+"' ");
	            }
	            if(judgeStr(list.get(m).getGoodsId())){
	                double soldNum=list.get(m).getSoldNum();
	                orderGoodsDao.executeSql(" update jl_material_goods_info set storeNum=storeNum+"+soldNum+" where id='"+list.get(m).getGoodsId()+"' ");
	            }
	        }
	        //修改库存
	        //原先的ordergoods数据删除
	        orderGoodsDao.executeSql("delete from "+tablename+" where orderId='"+orderId+"'");
	    }catch (Exception e){
	        e.printStackTrace();
	        throw new RuntimeException();
	    }
        
    }
    
    public void deleteInfo(String id){
	    OrderGoodsInfo ogi=orderGoodsDao.get(id,OrderGoodsInfo.class);
	    if(null!=ogi){
            orderGoodsDao.delete(ogi);
        }
    }
    
    /**
	 * 判断空字符串
	 * @Title judgeStr
	 * @param str
	 * @return
	 * @author zpj
	 * @time 2019年5月14日 上午9:42:51
	 */
	public boolean judgeStr(String str){
		if(null!=str&&!"".equalsIgnoreCase(str)&&!"null".equalsIgnoreCase(str)){
			return true;
		}else{
			return false;
		}
	}
    
    
}
