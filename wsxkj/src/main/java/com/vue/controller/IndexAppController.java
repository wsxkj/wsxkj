package com.vue.controller;

import com.zpj.common.DateHelper;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.materials.entity.Customer;
import com.zpj.materials.service.OrderGoodsService;
import com.zpj.materials.service.OrderService;
import com.zpj.materials.service.StoreService;
import com.zpj.sys.entity.User;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zpj.common.BaseController;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app/index")
@Api(value = "/app/index",tags="首页功能", description = "首页功能接口")
public class IndexAppController extends BaseController{
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private StoreService storeService;

    @RequestMapping("/getCount")
    @ResponseBody
    @ApiOperation(value = "首页获取进货量和出货量", notes = "首页获取进货量和出货量", httpMethod = "POST" )
    public void getCount(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                        @ApiParam(required = false, name = "startTime", value = "开始时间")@RequestParam(value="startTime",required = false)String startTime,
                        @ApiParam(required = false, name = "endTime", value = "结束时间")@RequestParam(value="endTime",required = false)String endTime
    ){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            //出货量查询ordergoods表
            Map param=new HashMap();
            if(judgeStr(startTime)){
                param.put("startTime",startTime);
                if(judgeStr(endTime)){
                    param.put("endTime",endTime);
                }else{
                    param.put("endTime",startTime);
                }
            }
            param.put("userId",user.getId());

            int count=orderGoodsService.findOrderGoodsOutCount(param);
            
            Map retMap=new HashMap();
            //出货量
            retMap.put("outCount", count);
            //进货量
            storeService.findStoreInCount(param);
            retMap.put("inCount", count);
            
            rd.setData(retMap);
            rd.setCode(200);
            rd.setMsg("查询成功");
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


    @RequestMapping("/getFourData")
    @ResponseBody
    @ApiOperation(value = "首页获取今日销售额，净利润，进货件数，出售件数", notes = "首页获取今日销售额，净利润，进货件数，出售件数", httpMethod = "POST" )
    public void getFourData(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token
    ){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();

            Map param=new HashMap();
            param.put("today", DateHelper.getDateString(new Date(),"yyyy-MM-dd"));
            param.put("userId",user.getId());

            Map retMap=orderGoodsService.findFourData(param);
            rd.setData(retMap);
            rd.setCode(200);
            rd.setMsg("查询成功");
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

    @RequestMapping("/getOrderDataByTime")
    @ResponseBody
    @ApiOperation(value = "首页获取今日订单列表", notes = "首页获取今日订单列表", httpMethod = "POST" )
    public void getOrderDataByTime(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                                   @ApiParam(required = false, name = "state", value = "订单状态‘1’获取已发货订单，传‘23’获取未发货订单")@RequestParam(value="state",required = false)String state
    ){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
            map.put("userId",user.getId());
            map.put("state", filterStr(state));
            map.put("time",DateHelper.getDateString(new Date(),"yyyy-MM-dd"));
            MyPage pagedata = orderService.findPageDataMuti(map,1,3);
            List<Map> list= (List<Map>)pagedata.data;
            Map toi=null;
            MyPage tp=null;
            for(int i=0;i<list.size();i++){
                toi =list.get(i);
                map =new HashMap();
                map.put("orderId", toi.get("id"));
                tp = orderGoodsService.findPageDataMuti(map,1,20);
                toi.put("goodsList", tp.data);
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

}
