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
import com.zpj.sys.entity.User;
import com.zpj.sys.service.LevelService;

import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/app/level")
@Api(value = "/app/level",tags="用户等级接口", description = "用户接口")
public class LevelAppController extends BaseController{
	
	@Autowired
	private LevelService levelService;
	
	@RequestMapping("/findLevelList")
    @ResponseBody
    @ApiOperation(value = "等级列表", notes = "等级列表", httpMethod = "POST")
    public void findGoodsBrandList(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                                   @ApiParam(required = true, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                   @ApiParam(required = true, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            Map map=new HashMap();
            MyPage pagedata = levelService.findPageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
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
	
}
