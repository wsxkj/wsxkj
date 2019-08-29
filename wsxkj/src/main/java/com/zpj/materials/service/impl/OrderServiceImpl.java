package com.zpj.materials.service.impl;

import java.math.BigInteger;
import java.util.*;

import com.zpj.common.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.common.aop.Log;
import com.zpj.materials.entity.Goods;
import com.zpj.materials.entity.OrderGoodsInfo;
import com.zpj.materials.entity.OrderInfo;
import com.zpj.materials.entity.Store;
import com.zpj.materials.service.GoodsService;
import com.zpj.materials.service.OrderGoodsService;
import com.zpj.materials.service.OrderService;
import com.zpj.materials.service.StoreService;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.entity.User;

import io.jsonwebtoken.JwtException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private BaseDao<OrderInfo> orderDao;
	private String tablename="jl_material_order_info";
    @Autowired
    private BaseDao<LogInfo> logDao;
    @Autowired
	private OrderGoodsService orderGoodsService;
	@Autowired
    private StoreService storeService;
	@Autowired
    private GoodsService goodsService;
	
    public MyPage findPageData(Map canshu, Integer page, Integer limit) {
        Map param=new HashMap();
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            param.put("userId-eq", canshu.get("userId"));
        }
        if(null!=canshu.get("state")&&!"".equalsIgnoreCase((String)canshu.get("state"))){
            param.put("state-eq", canshu.get("state"));
        }
        Map px=new HashMap();
        px.put("updateTime", "desc");
        return orderDao.findPageDateSqlT(tablename,"", param,px , page, limit, OrderInfo.class);
    }
    
    public MyPage findPageDataMuti(Map canshu, Integer page, Integer limit){
    	StringBuffer sql=new StringBuffer(200).append("select o.*,c.address as customerAddress,c.nickname as customerNickname,c.phone as customerPhone ");
    	StringBuffer sqlcount =new StringBuffer(100).append("select count(*) as num,1  ");
    	StringBuffer wheresql=new StringBuffer(100);
    	wheresql.append("  from "+tablename +" o left join jl_material_customer_info c on o.customerId=c.id where 1=1 ");
    	if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
    		wheresql.append(" and o.userId='"+canshu.get("userId")+"' ");
        }

        if(null!=canshu.get("time")&&!"".equalsIgnoreCase((String)canshu.get("time"))){
            wheresql.append(" and o.updateTime like '"+canshu.get("time")+"%' ");
        }

        if(null!=canshu.get("state")&&!"".equalsIgnoreCase((String)canshu.get("state"))){
        	String state=(String)canshu.get("state");
        	if(state.equalsIgnoreCase("12")){
        		wheresql.append(" and ( o.state ='1' or o.state ='2' ) ");
        	}else{
        		wheresql.append(" and o.state ='"+canshu.get("state")+"' ");
        	}
        }
        sql.append(wheresql).append(" order by o.updateTime desc ");
    	List list=orderDao.findMapObjBySql(sql.toString(), null, page, limit);
    	
    	sqlcount.append(wheresql);
		List<Object[]> lt = orderDao.findBySql(sqlcount.toString());
		int num=0;
		if (lt != null) {
			num=Integer.parseInt(((BigInteger) lt.get(0)[0]).toString());
		}
    	
    	MyPage my=new MyPage();
    	my.setData(list);
    	my.setCount(num);
    	return my;
    }
    
    public Map findPageDataMutiGroupByMonth(Map canshu){
    	Map retmap=new HashMap();
    	StringBuilder sql=new StringBuilder(200);
    	
		sql.append("select select sum(t.stp) as xse,DATE_FORMAT(t.updateTime,'%Y-%m') as time,sum(itp) as jlr from ( select a.soldTotalPrice as stp,a.updateTime,(a.soldNum*b.inPrice) as itp  from jl_material_order_goods_info a left join  jl_material_store_info b on a.storeId=b.id  where 1=1 ");
		if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
			sql.append(" and b.userId='"+canshu.get("userId")+"'") ;
		}
		if(null!=canshu.get("time")&&!"".equalsIgnoreCase((String)canshu.get("time"))){
			sql.append(" and a.updateTime like '"+canshu.get("time")+"%'") ;
		}
		sql.append(") t group by DATE_FORMAT(updateTime,'%Y-%m') ");
		List list=orderDao.findMapObjBySqlNoPage(sql.toString());
		if(null!=list&&list.size()>0){
			retmap=(Map)list.get(0);
		}else{
			retmap.put("xse", "0");
			retmap.put("time", canshu.get("time"));
			retmap.put("jlr", "0");
		}
		
    	return retmap;
    }


    public Map findMutiSumDataAll(Map canshu){
    	List retList=new ArrayList<>();
    	StringBuilder sql=new StringBuilder(200);
    	//查询总销售额，总净利润，总出售件数
		sql.append(" select sum(a.soldTotalPrice) as soldmoney,sum(a.soldNum*b.inPrice) as inmoney , sum(a.soldNum) as outnum  from jl_material_order_goods_info a left join  jl_material_store_info b on a.storeId=b.id  where 1=1 ");
		if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
			sql.append(" and b.userId='"+canshu.get("userId")+"'") ;
		}
		List<Map> list=orderDao.findMapObjBySqlNoPage(sql.toString());
		//进货件数
		sql=new StringBuilder(200);
		sql.append("select SUM(inNum) as innum from jl_material_store_info ");
		if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
			sql.append(" and b.userId='"+canshu.get("userId")+"'") ;
		}
		List<Map> list2=orderDao.findMapObjBySqlNoPage(sql.toString());
		Map retMap=new HashMap();
		if(null!=list&&list.size()>0){
			retMap.put("soldmoney",list.get(0).get("soldmoney"));
			retMap.put("inmoney",list.get(0).get("inmoney"));
			retMap.put("outnum",list.get(0).get("outnum"));
		}else{
			retMap.put("soldmoney","0");
			retMap.put("inmoney","0");
			retMap.put("outnum","0");
		}
		if(null!=list2&&list2.size()>0){
			retMap.put("innum",list2.get(0).get("innum"));
		}else{
			retMap.put("innum","0");
		}
		return retMap;
    }
    
    public List findPageDataMutiGroupByMonthDay(Map canshu){
    	List retList=new ArrayList<>();
    	StringBuilder sql=new StringBuilder(200);
    	//查询销售额，日期，净利润，出售件数
		sql.append(" select sum(t.stp) as soldmoney,DATE_FORMAT(t.updateTime,'%Y-%m-%d') as time,sum(itp) as inmoney,sum(t.chl) as outnum from ( select a.soldTotalPrice as stp,a.updateTime,(a.soldNum*b.inPrice) as itp , a.soldNum as chl  from jl_material_order_goods_info a left join  jl_material_store_info b on a.storeId=b.id  where 1=1 ");
		if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
			sql.append(" and b.userId='"+canshu.get("userId")+"'") ;
		}
		if(null!=canshu.get("time")&&!"".equalsIgnoreCase((String)canshu.get("time"))){
			sql.append(" and a.updateTime like '"+canshu.get("time")+"%'") ;
		}
		sql.append(") t group by DATE_FORMAT(updateTime,'%Y-%m-%d') ");
		List<Map> list=orderDao.findMapObjBySqlNoPage(sql.toString());
		//进货件数
		sql=new StringBuilder(200);
		sql.append("select DATE_FORMAT(inDate,'%Y-%m-%d') as time ,SUM(inNum) as innum from jl_material_store_info where 1=1 ");
		if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
			sql.append(" and userId='"+canshu.get("userId")+"'") ;
		}
		if(null!=canshu.get("time")&&!"".equalsIgnoreCase((String)canshu.get("time"))){
			sql.append(" and updateTime like '"+canshu.get("time")+"%'") ;
		}
		sql.append(" GROUP BY DATE_FORMAT(inDate,'%Y-%m-%d') ");
		
		List<Map> list2=orderDao.findMapObjBySqlNoPage(sql.toString());
		
		//获取某个月每一天的日期list
		String time=(String)canshu.get("time");
	    int year=Integer.parseInt(time.split("-")[0]);
	    String ms=time.split("-")[1];
	    if(ms.substring(0,1).equalsIgnoreCase("0")){
	       ms=ms.substring(1,2);
	    }
	    int month=Integer.parseInt(ms);
	    List<String> dayList=DateHelper.getDayByMonth(year,month);
	    
	    //根据每一天进行循环
	    Map retMap=new HashMap();
	    for(int n=0;n<dayList.size();n++){
	    	retMap=new HashMap();
	    	retMap.put("date", dayList.get(n));
	    	if(null!=list&&list.size()>0){
	    		boolean flag=false;
	    		for(int p=0;p<list.size();p++){
		    		if(null!=list.get(p).get("time")&&dayList.get(n).equals((String)list.get(p).get("time"))){
		    			retMap.put("soldmoney",list.get(p).get("soldmoney"));
						retMap.put("inmoney",list.get(p).get("inmoney"));
						retMap.put("outnum",list.get(p).get("outnum"));
						flag=true;
						break;
		    		}
		    	}
	    		if(!flag){
	    			retMap.put("soldmoney","0");
					retMap.put("inmoney","0");
					retMap.put("outnum","0");
	    		}
	    		
	    	}else{
	    		retMap.put("soldmoney","0");
				retMap.put("inmoney","0");
				retMap.put("outnum","0");
	    	}
	    	if(null!=list2&&list2.size()>0){
	    		boolean flag=false;
	    		for(int q=0;q<list2.size();q++){
	    			if(null!=list2.get(q).get("time")&&dayList.get(n).equals((String)list2.get(q).get("time"))){
	    				retMap.put("innum",list2.get(q).get("innum"));
	    				flag=true;
	    				break;
	    			}
	    		}
	    		if(!flag){
	    			retMap.put("innum","0");
	    		}
	    	}else{
	    		retMap.put("innum","0");
	    	}
	    	retList.add(retMap);
	    }
		return retList;
		
		//循环请求的方法.....慢
//        List retList=new ArrayList();
//        String time=(String)canshu.get("time");
//        int year=Integer.parseInt(time.split("-")[0]);
//        String ms=time.split("-")[1];
//        if(ms.substring(0,1).equalsIgnoreCase("0")){
//            ms=ms.substring(1,2);
//        }
//        int month=Integer.parseInt(ms);
//        List list=DateHelper.getDayByMonth(Integer.parseInt(time.split("-")[0]),month);
//        Map tempMap=new HashMap();
//        for(int m=0;m<list.size();m++){
//            tempMap=new HashMap();
//            tempMap.put("userId",canshu.get("userId"));
//            tempMap.put("time",list.get(m));
//            retList.add(orderGoodsService.findFourData(tempMap));
//        }
//		return retList;
    }

    
    @Log(type="保存",remark="保存订单信息")
    public void saveInfo(OrderInfo info){
    	orderDao.add(info);
    }

    public OrderInfo findById(String id){
        return orderDao.get(id,OrderInfo.class);
    }

    public void deleteInfo(String id){
        OrderInfo oi=orderDao.get(id,OrderInfo.class);
        if(null!=oi)
            orderDao.delete(oi);
    }
    
    public void saveOrderMultiInfo(String id,String customerid,String state,String trackingNo,String orderGoods,String postage,User user) throws RuntimeException{
    	try{

            boolean updateflag=false;
            //订单信息保存
            OrderInfo oi=null;
            if(judgeStr(id)){
            	oi=this.findById(id);
            }

            if(null!=oi){
                updateflag=true;
            }else{
                oi=new OrderInfo();
                updateflag=false;
            }

            oi.setCustomerId(customerid);
            oi.setUserId(user.getId());
            oi.setState(state);
            oi.setPostage(Double.valueOf(filterStr(postage)));
            oi.setTrackingNo(trackingNo);
            oi.setUpdateTime(new Date());
            //订单表保存
            this.saveInfo(oi);
//            orderGoods="[{\"id\":\"\",\"orderId\":\"\",\"goodsId\":\"GOODS_19513174669990\",\"storeId\":\"STORE_19513212995202\",\"soldNum\":\"1\",\"soldPrice\":\"100\",\"soldTotalPrice\":\"100\",\"paidMoney\":\"100\",\"unpaidMoney\":\"0\"}]";
//            orderGoods="[{id:\"1\",goodsId:\"1\",storeId:\"asdfd\",soldNum:\"1\",soldPrice:\"100\",soldTotalPrice:\"200\",paidMoney:\"50\",unpaidMoney:\"150\"},{id:\"2\",goodsId:\"2\",storeId:\"df\",soldNum:\"2\",soldPrice:\"300\",soldTotalPrice:\"600\",paidMoney:\"500\",unpaidMoney:\"100\"}]";
            JSONArray jsonArray=JSONArray.fromObject(orderGoods);
            JSONObject jsonObject;
//            StringBuffer sql=new StringBuffer(2000);
            //订单商品信息保存
            if(!updateflag){          
                //新增订单
                for (int n=0;n<jsonArray.size();n++){
                    jsonObject=(JSONObject)jsonArray.get(n);
                    OrderGoodsInfo ogi=new OrderGoodsInfo();
                    ogi.setGoodsId(String.valueOf(jsonObject.get("goodsId")));
                    ogi.setOrderId(oi.getId());
                    ogi.setStoreId(String.valueOf(jsonObject.get("storeId")));
                    ogi.setSoldNum(Double.parseDouble(String.valueOf(jsonObject.get("soldNum"))));
                    ogi.setSoldPrice(Double.parseDouble(String.valueOf(jsonObject.get("soldPrice"))));
                    ogi.setSoldTotalPrice(Double.parseDouble(String.valueOf(jsonObject.get("soldTotalPrice"))));
                    ogi.setPaidMoney(Double.parseDouble(String.valueOf(jsonObject.get("paidMoney"))));
                    ogi.setUnpaidMoney(Double.parseDouble(String.valueOf(jsonObject.get("unpaidMoney"))));
                    ogi.setUpdateTime(new Date());
                    orderGoodsService.saveInfo(ogi);


                    if(judgeStr(String.valueOf(jsonObject.get("goodsId")))){
                        double soldNum=Double.parseDouble(String.valueOf(jsonObject.get("soldNum")));
                        orderDao.executeSql(" update jl_material_goods_info set storeNum=(storeNum-"+soldNum+") where id='"+String.valueOf(jsonObject.get("goodsId"))+"' ");
                        
                    }
                    if(judgeStr(String.valueOf(jsonObject.get("storeId")))){
                        double soldNum=Double.parseDouble(String.valueOf(jsonObject.get("soldNum")));
                        orderDao.executeSql(" update jl_material_store_info set storeNum=(storeNum-"+soldNum+") where id='"+String.valueOf(jsonObject.get("storeId"))+"' ");
                        
                    }
                    
                }
            }else{
                //修改订单

                Map param=new HashMap();
                param.put("userId",user.getId());
                param.put("orderId",id);
                MyPage mp=orderGoodsService.findPageData(param,1,50);
                List<OrderGoodsInfo> list =(List<OrderGoodsInfo>)mp.getData();
                for(int m=0;m<list.size();m++){
                    if(judgeStr(list.get(m).getStoreId())){
                        double soldNum=list.get(m).getSoldNum();
                        orderDao.executeSql(" update jl_material_store_info set storeNum=storeNum+"+soldNum+" where id='"+list.get(m).getStoreId()+"' ");
                    }
                    if(judgeStr(list.get(m).getGoodsId())){
                        double soldNum=list.get(m).getSoldNum();
                        orderDao.executeSql(" update jl_material_goods_info set storeNum=storeNum+"+soldNum+" where id='"+list.get(m).getGoodsId()+"' ");
                    }
                }
                //修改库存
                //原先的ordergoods数据删除
                orderGoodsService.deleteOrderGoodsInfoByOrderId(id);



                for (int n=0;n<jsonArray.size();n++){
                    jsonObject=(JSONObject)jsonArray.get(n);

                    OrderGoodsInfo ogi=new OrderGoodsInfo();
                    ogi.setGoodsId(String.valueOf(jsonObject.get("goodsId")));
                    ogi.setOrderId(oi.getId());
                    ogi.setStoreId(String.valueOf(jsonObject.get("storeId")));
                    ogi.setSoldNum(Double.parseDouble(String.valueOf(jsonObject.get("soldNum"))));
                    ogi.setSoldPrice(Double.parseDouble(String.valueOf(jsonObject.get("soldPrice"))));
                    ogi.setSoldTotalPrice(Double.parseDouble(String.valueOf(jsonObject.get("soldTotalPrice"))));
                    ogi.setPaidMoney(Double.parseDouble(String.valueOf(jsonObject.get("paidMoney"))));
                    ogi.setUnpaidMoney(Double.parseDouble(String.valueOf(jsonObject.get("unpaidMoney"))));
                    ogi.setUpdateTime(new Date());
                    orderGoodsService.saveInfo(ogi);


                    if(judgeStr(String.valueOf(jsonObject.get("storeId")))){
                        double soldNum=Double.parseDouble(String.valueOf(jsonObject.get("soldNum")));
                        orderDao.executeSql(" update jl_material_store_info set storeNum=storeNum-"+soldNum+" where id='"+String.valueOf(jsonObject.get("storeId"))+"' ");
                    }
                    if(judgeStr(String.valueOf(jsonObject.get("goodsId")))){
                        double soldNum=Double.parseDouble(String.valueOf(jsonObject.get("soldNum")));
                        orderDao.executeSql(" update jl_material_goods_info set storeNum=storeNum-"+soldNum+" where id='"+String.valueOf(jsonObject.get("goodsId"))+"' ");
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    /**
	 * 过滤空数据
	 * @Title filterStr
	 * @param str
	 * @return
	 * @author zpj
	 * @time 2019年5月8日 上午11:10:00
	 */
	public String filterStr(String str){
		if(null!=str&&!"".equalsIgnoreCase(str)&&!"null".equalsIgnoreCase(str)){
			return str;
		}else{
			return "";
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
