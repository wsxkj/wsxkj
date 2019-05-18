package com.vue.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.zpj.materials.entity.Goods;
import com.zpj.materials.entity.OrderGoodsInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.materials.entity.OrderInfo;
import com.zpj.materials.entity.Store;
import com.zpj.materials.service.GoodsService;
import com.zpj.materials.service.OrderGoodsService;
import com.zpj.materials.service.OrderService;
import com.zpj.materials.service.StoreService;
import com.zpj.sys.entity.User;

import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/app/order")
@Api(value = "/app/order",tags="订单功能", description = "订单功能接口")
public class OrderAppController extends BaseController{
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderGoodsService orderGoodsService;
 	
	@RequestMapping("/findOrderList")
    @ResponseBody
    @ApiOperation(value = "订单列表", notes = "订单列表", httpMethod = "POST",response = OrderInfo.class )
    public void findOrderList(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
    		@ApiParam(required = false, name = "state", value = "订单状态0,1,2,3")@RequestParam(value="state",required = false)String state,
            @ApiParam(required = true, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                   @ApiParam(required = true, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user= (User)request.getSession().getAttribute("jluser");
            Map map=new HashMap();
            map.put("userId",user.getId());
            map.put("state", filterStr(state));
            MyPage pagedata = orderService.findPageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
            List<OrderInfo> list= (List<OrderInfo>)pagedata.data;
            OrderInfo toi=null;
            MyPage tp=null;
            for(int i=0;i<list.size();i++){
            	toi =list.get(i);
            	map =new HashMap();
            	map.put("orderId", toi.getId());
            	tp = orderGoodsService.findPageData(map,1,20);
            	toi.setGoodsList((List<OrderGoodsInfo>)tp.data);
            }
            rd.setData(list);
            rd.setCount(pagedata.count);
            rd.setCode(200);
            rd.setMsg("查询成功");
        }catch (JwtException e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("token转码失败，token过期");
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("查询失败");
        }
        this.jsonWrite2(rd);
    }

//	@RequestMapping("/findOrderGoodsList")
//    @ResponseBody
//    @ApiOperation(value = "订单商品列表", notes = "订单商品列表", httpMethod = "POST")
//    public void findOrderGoodsList(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
//						    		@ApiParam(required = false, name = "orderid", value = "订单id")@RequestParam(value="orderid",required=false)String orderid,
//						            @ApiParam(required = true, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
//                                   @ApiParam(required = true, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
//        ResultData rd=new ResultData();
//        try{
//            User user= (User)request.getSession().getAttribute("jluser");
//            Map map=new HashMap();
//            map.put("userId",user.getId());
//            map.put("orderId", filterStr(orderid));
//            MyPage pagedata = orderGoodsService.findPageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
//            rd.setData(pagedata.data);
//            rd.setCount(pagedata.count);
//            rd.setCode(200);
//            rd.setMsg("查询成功");
//        }catch (JwtException e){
//            e.printStackTrace();
//            rd.setCode(500);
//            rd.setMsg("token转码失败，token过期");
//        }catch (Exception e){
//            e.printStackTrace();
//            rd.setCode(500);
//            rd.setMsg("查询失败");
//        }
//        this.jsonWrite2(rd);
//    }

    @RequestMapping("/deleteOrderInfo")
    @ResponseBody
    @ApiOperation(value = "删除订单信息", notes = "删除订单信息", httpMethod = "POST")
    public void deleteOrderInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                                @ApiParam(required = true, name = "id", value = "订单id")@RequestParam("id")String id) {
        ResultData rd=new ResultData();
        try{
            orderService.deleteInfo(id);
            orderGoodsService.deleteOrderGoodsInfoByOrderId(id);
            rd.setCode(200);
            rd.setMsg("删除成功");
        }catch (JwtException e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("token转码失败，token过期");
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("删除失败");
        }

    }

    @RequestMapping("/saveOrder")
    @ResponseBody
    @ApiOperation(value = "保存订单", notes = "保存订单", httpMethod = "POST")
    public void saveOrder(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                                      @ApiParam(required = false, name = "id", value = "订单id")@RequestParam(value="customerid")String id,
                                      @ApiParam(required = true, name = "customerid", value = "客户id")@RequestParam(value="customerid")String customerid,
						    		@ApiParam(required = false, name = "state", value = "订单状态0,1,2,3")@RequestParam(value="state",required = false)String state,
						    		@ApiParam(required = false, name = "trackingNo", value = "物流单号")@RequestParam(value="trackingNo",required = false)String trackingNo,
                                    @ApiParam(required = false, name = "orderGoods", value = "商品订单json数据")@RequestParam(value="orderGoods",required = false)String orderGoods,
                                    @ApiParam(required = false, name = "postage", value = "邮费")@RequestParam(value="postage",required = false)String postage
						            ){
        ResultData rd=new ResultData();
        try{
            User user= (User)request.getSession().getAttribute("jluser");
            orderService.saveOrderMultiInfo(id,customerid,state,trackingNo,orderGoods,postage,user);
            rd.setCode(200);
            rd.setMsg("保存成功");
        }catch (JwtException e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("token转码失败，token过期");
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("操作失败");
        }
        this.jsonWrite2(rd);
    }


    @RequestMapping("/deleteOrderGoodsInfo")
    @ResponseBody
    @ApiOperation(value = "删除订单商品信息", notes = "删除订单商品信息", httpMethod = "POST")
    public void deleteOrderGoodsInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                                @ApiParam(required = true, name = "id", value = "订单商品id")@RequestParam("id")String id) {
        ResultData rd=new ResultData();
        try{
            orderGoodsService.deleteInfo(id);
            rd.setCode(200);
            rd.setMsg("删除成功");
        }catch (JwtException e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("token转码失败，token过期");
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("删除失败");
        }

    }



//    public static void main(String[] args) {
//        String orderGoods="[{id:\"1\",goodsId:\"1\",storeId:\"asdfd\",soldNum:\"1\",soldPrice:100,soldTotalPrice:200,paidMoney:50,unpaidMoney:150},{id:\"2\",goodsId:\"2\",storeId:\"df\",soldNum:\"2\",soldPrice:\"300\",soldTotalPrice:\"600\",paidMoney:\"500\",unpaidMoney:\"100\"}]";
//        //订单商品信息保存
//        JSONArray jsonArray=JSONArray.fromObject(orderGoods);
//        System.out.println("123123");
//    }
	
}
