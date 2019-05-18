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
    public void findGoodsBrandList(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                                   @ApiParam(required = false, name = "name", value = "名称")@RequestParam(value="name",required=false)String name,
                                   @ApiParam(required = true, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                   @ApiParam(required = true, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user= (User)request.getSession().getAttribute("jluser");
            Map map=new HashMap();
            map.put("name",filterStr(name));
            map.put("userId",user.getId());
            MyPage pagedata = customerService.findPageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
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

    @RequestMapping("/saveInfo")
    @ResponseBody
    @ApiOperation(value = "保存客户", notes = "保存客户", httpMethod = "POST" ,response = Customer.class)
    public void saveInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
				    		@ApiParam(required = false, name = "id", value = "客户主键id")@RequestParam(value="id",required=false)String id,
				            @ApiParam(required = true, name = "nickname", value = "微信昵称")@RequestParam("nickname")String nickname,
                            @ApiParam(required = false, name = "wxh", value = "微信号")@RequestParam(value="wxh",required=false)String wxh,
                            @ApiParam(required = false, name = "receiver", value = "收货姓名")@RequestParam(value="receiver",required=false)String receiver,
                            @ApiParam(required = false, name = "address", value = "收货地址")@RequestParam(value="address",required=false)String address,
                            @ApiParam(required = false, name = "phone", value = "手机号码")@RequestParam(value="phone",required=false)String phone
                           ){
        ResultData rd=new ResultData();
        try{
            User user= (User)request.getSession().getAttribute("jluser");
            Map map=new HashMap();
            Customer customer=null;
            //保存新客户信息
            if(judgeStr(id)){
            	customer=customerService.findById(id);
            }else{
            	customer=new Customer();
            }
            customer.setUserId(user.getId());
            customer.setAddress(address);
            customer.setNickname(nickname);
            customer.setWxh(wxh);
            customer.setPhone(phone);
            customer.setReceiver(receiver);
            customer.setUpdateTime(new Date());

            customerService.saveInfo(customer);
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
    
    @RequestMapping("/delInfo")
    @ResponseBody
    @ApiOperation(value = "删除客户", notes = "删除客户", httpMethod = "POST" ,response = Customer.class)
    public void delInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                            @ApiParam(required = true, name = "id", value = "客户id主键")@RequestParam("id")String id
                           ){
        ResultData rd=new ResultData();
        try{
            Customer c=customerService.findById(id);
            if(null!=c)
            	customerService.delInfo(c);
            rd.setCode(200);
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
