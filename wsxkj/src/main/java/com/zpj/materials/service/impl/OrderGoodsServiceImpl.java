package com.zpj.materials.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zpj.materials.entity.Store;
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
	private BaseDao<OrderInfo> orderDao;
	private String tablename_order="jl_material_order_info";


    @Autowired
    private BaseDao<LogInfo> logDao;
	@Autowired
	private BaseDao<Store> storeBaseDao;
    /*
     * @MethodName: findFourData
     * @Description: TODO(获取今日销售额，净利润，进货件数，出售件数)
     * @params [canshu]
     * @return java.util.Map
     * @author zpj
     * @date 2019/8/13 17:11
    */
	public Map findFourData(Map canshu){
		StringBuilder sql=new StringBuilder(200);
		sql.append("select a.soldNum,a.soldPrice,a.soldTotalPrice,b.inPrice,b.outPrice  from "+tablename +" a left join  jl_material_store_info b on a.storeId=b.id left join jl_material_order_info c on c.id=a.orderId where 1=1 ");
		if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
			sql.append(" and b.userId='"+canshu.get("userId")+"'") ;
		}
		if(null!=canshu.get("time")&&!"".equalsIgnoreCase((String)canshu.get("time"))){
			sql.append(" and c.updateTime like '"+canshu.get("time")+"%'") ;
		}
		float totalSoldMoney=0;//今日销售总额
		float inMoney=0;//净利润
		int inNum=0;//进货数量
		int outNum=0;//出货数量
		Map tempMap=new HashMap();
		List  list=orderGoodsDao.findMapObjBySqlNoPage(sql.toString());
		if(list!=null&&list.size()>0){

			for (int i = 0; i < list.size(); i++) {
				tempMap=(Map)list.get(i);
				totalSoldMoney+=((Double)tempMap.get("soldTotalPrice"));
				/* 潘 删除 
				Double totalbuyMoney=((Double)tempMap.get("soldNum"))*((Double)tempMap.get("inPrice"));
				*/
				/* 潘 新增  */
				Double inMoneyOne=((Double)tempMap.get("soldNum"))*(((Double)tempMap.get("soldPrice")) - ((Double)tempMap.get("inPrice")));
				
				inMoney+=inMoneyOne;
				outNum+=((Double)tempMap.get("soldNum"));
			}
		}
		sql=new StringBuilder(200);
		sql.append(" select inNum,inDate from jl_material_store_info where 1=1 ");
		if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
			sql.append(" and userId='"+canshu.get("userId")+"'") ;
		}
		if(null!=canshu.get("time")&&!"".equalsIgnoreCase((String)canshu.get("time"))){
			sql.append(" and inDate like '"+canshu.get("time")+"%'") ;
		}
		List  list1 =storeBaseDao.findMapObjBySqlNoPage(sql.toString());
		for (int k = 0; k <list1.size() ; k++) {
			tempMap=(Map)list1.get(k);
			inNum+=(Double)tempMap.get("inNum");
		}

		Map retMap=new HashMap();
		retMap.put("soldmoney",totalSoldMoney);
		retMap.put("inmoney",inMoney);
		retMap.put("innum",inNum);
		retMap.put("outnum",outNum);
		retMap.put("date",canshu.get("time"));
		return retMap;
	}

    public int  findOrderGoodsOutCount(Map canshu){
        Map param=new HashMap();
        StringBuilder sql=new StringBuilder(100);
        sql.append("select sum(a.soldNum) as sn,b.updatetime as ordertime from "+tablename +" a left join jl_material_order_info b on a.orderId=b.id where 1=1 ");
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            sql.append(" and b.userId='"+canshu.get("userId")+"'") ;
        }
        if(null!=canshu.get("startTime")&&!"".equalsIgnoreCase((String)canshu.get("startTime"))){
            sql.append(" and b.updateTime>='"+canshu.get("startTime")+"'") ;
        }
        if(null!=canshu.get("endTime")&&!"".equalsIgnoreCase((String)canshu.get("endTime"))){
            sql.append(" and b.updateTime<='"+canshu.get("endTime")+"'") ;
        }
        List<Map> list=orderGoodsDao.findMapObjBySqlNoPage(sql.toString());
        if(list!=null&&list.size()>0){
        	return Integer.parseInt(list.get(0).get("sn").toString());
        }
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
		StringBuffer sql=new StringBuffer(200).append("select o.*,c.name as goodsName,c.picture as goodsPicture,s.updateTime as storeTime,s.inDate as inDate ");
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
		
		sql.append(" select t.id,c.nickname,a.sn as soldNum,t.updateTime from  jl_material_order_info t LEFT JOIN jl_material_customer_info c on c.id=t.customerId INNER JOIN ( select SUM(soldNum) as sn ,orderId  from jl_material_order_goods_info where goodsId='"+canshu.get("goodsId")+"' GROUP BY orderId ) a on t.id=a.orderId  where 1=1 ");
		if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
			sql.append(" and t.userId='"+canshu.get("userId")+"'");
        }
		sql.append(" order by t.updateTime desc");
		List list=orderGoodsDao.findMapObjBySql(sql.toString(), null, page, limit);
        return list;
	}
	
	@Log(type="保存",remark="保存订单商品信息")
    public void saveInfo(OrderGoodsInfo orderGoodsInfo){
        orderGoodsDao.add(orderGoodsInfo);
    }
	
    public void deleteOrderGoodsInfoByOrderId(String orderId) throws RuntimeException{
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
	                if(judgeStr(list.get(m).getStoreId())){
	                	orderGoodsDao.executeSql(" update jl_material_goods_info set storeNum=storeNum+"+soldNum+" where id='"+list.get(m).getGoodsId()+"' ");
	                }
	            }
	        }
	        //修改库存
	        //原先的ordergoods数据删除jl_material_order_goods_info
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
