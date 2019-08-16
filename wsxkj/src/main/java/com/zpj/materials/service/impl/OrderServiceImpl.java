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
        	wheresql.append(" and o.state ='"+canshu.get("state")+"' ");
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
