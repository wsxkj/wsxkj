package com.vue.controller;

import com.zpj.common.BaseController;
import com.zpj.common.DateHelper;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.materials.entity.Customer;
import com.zpj.materials.entity.Goods;
import com.zpj.materials.entity.Store;
import com.zpj.materials.service.CustomerService;
import com.zpj.sys.entity.User;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * @ClassName: CustomerAppController
 * @Description: TODO(客户)
 * @author zpj
 * @date 2019/5/5 11:17
*/
@Controller
@RequestMapping("/app/customer")
@Api(value = "/app/customer",tags="客户功能", description = "客户功能接口")
public class CustomerAppController extends BaseController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping("/findCustomerList")
    @ResponseBody
    @ApiOperation(value = "客户列表", notes = "客户列表", httpMethod = "POST")
    public void findGoodsBrandList(@ApiParam(required = false, name = "token", value = "token")@RequestParam("token")String token,
                                   @ApiParam(required = false, name = "name", value = "名称")@RequestParam("name")String name,
                                   @ApiParam(required = false, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                   @ApiParam(required = false, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user= (User)request.getSession().getAttribute("jluser");
            Map map=new HashMap();
            map.put("name",name);
            map.put("userId",user.getId());
            MyPage pagedata = customerService.findPageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
            rd.setData(pagedata);
            rd.setCode(200);
            rd.setToken(token);
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

    @RequestMapping("/saveInfo")
    @ResponseBody
    @ApiOperation(value = "保存客户", notes = "保存客户", httpMethod = "POST" ,response = Customer.class)
    public void saveInfo(@ApiParam(required = false, name = "token", value = "token")@RequestParam("token")String token,
                            @ApiParam(required = false, name = "nickname", value = "微信昵称")@RequestParam("nickname")String nickname,
                            @ApiParam(required = false, name = "wxh", value = "微信号")@RequestParam("wxh")String wxh,
                            @ApiParam(required = false, name = "receiver", value = "收货姓名")@RequestParam("receiver")String receiver,
                            @ApiParam(required = false, name = "address", value = "收货地址")@RequestParam("address")String address,
                            @ApiParam(required = false, name = "phone", value = "手机号码")@RequestParam("phone")String phone
                           ){
        ResultData rd=new ResultData();
        try{
            User user= (User)request.getSession().getAttribute("jluser");
            Map map=new HashMap();
            //保存新商品信息
            Customer customer=new Customer();
            customer.setUserId(user.getId());
            customer.setAddress(address);
            customer.setNickname(nickname);
            customer.setWxh(wxh);

            customerService.saveInfo(customer);
            rd.setCode(200);
            rd.setToken(token);
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
    
    @RequestMapping("/delInfo")
    @ResponseBody
    @ApiOperation(value = "删除客户", notes = "删除客户", httpMethod = "POST" ,response = Customer.class)
    public void delInfo(@ApiParam(required = false, name = "token", value = "token")@RequestParam("token")String token,
                            @ApiParam(required = false, name = "id", value = "客户id主键")@RequestParam("id")String id
                           ){
        ResultData rd=new ResultData();
        try{
            User user= (User)request.getSession().getAttribute("jluser");
            customerService.delInfo(id,user);
            rd.setCode(200);
            rd.setToken(token);
            rd.setMsg("删除成功");
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
}
