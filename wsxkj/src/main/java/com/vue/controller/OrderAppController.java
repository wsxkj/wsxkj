package com.vue.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.materials.entity.OrderInfo;
import com.zpj.materials.service.OrderGoodsService;
import com.zpj.materials.service.OrderService;
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
    @ApiOperation(value = "订单列表", notes = "订单列表", httpMethod = "POST")
    public void findOrderList(@ApiParam(required = false, name = "token", value = "token")@RequestParam("token")String token,
    		@ApiParam(required = false, name = "state", value = "订单状态0,1,2,3")@RequestParam(value="state",required = false)String state,
            @ApiParam(required = false, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                   @ApiParam(required = false, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user= (User)request.getSession().getAttribute("jluser");
            Map map=new HashMap();
            map.put("userId",user.getId());
            map.put("state", filterStr(state));
            MyPage pagedata = orderService.findPageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
            rd.setData(pagedata.data);
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

	@RequestMapping("/findOrderGoodsList")
    @ResponseBody
    @ApiOperation(value = "订单商品列表", notes = "订单商品列表", httpMethod = "POST")
    public void findOrderGoodsList(@ApiParam(required = false, name = "token", value = "token")@RequestParam("token")String token,
						    		@ApiParam(required = false, name = "orderid", value = "订单id")@RequestParam("orderid")String orderid,
						            @ApiParam(required = false, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                   @ApiParam(required = false, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user= (User)request.getSession().getAttribute("jluser");
            Map map=new HashMap();
            map.put("userId",user.getId());
            map.put("orderId", filterStr(orderid));
            MyPage pagedata = orderGoodsService.findPageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
            rd.setData(pagedata.data);
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
	
	
	
	
	@RequestMapping("/saveOrder")
    @ResponseBody
    @ApiOperation(value = "保存订单", notes = "保存订单", httpMethod = "POST")
    public void saveOrder(@ApiParam(required = false, name = "token", value = "token")@RequestParam("token")String token,
						    		@ApiParam(required = false, name = "customerid", value = "客户id")@RequestParam(value="customerid",required = false)String customerid,
						    		@ApiParam(required = false, name = "postage", value = "邮费")@RequestParam(value="postage",required = false)String postage
						            ){
        ResultData rd=new ResultData();
        try{
            User user= (User)request.getSession().getAttribute("jluser");
            OrderInfo oi=new OrderInfo();
            oi.setCustomerId(customerid);
            oi.setUserId(user.getId());
            oi.setPostage(Double.valueOf(filterStr(postage)));
            orderService.saveInfo(oi);
//            rd.setData(pagedata.data);
//            rd.setCount(pagedata.count);
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
	
}
