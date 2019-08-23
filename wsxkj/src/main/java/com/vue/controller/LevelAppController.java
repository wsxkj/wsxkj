package com.vue.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zpj.sys.entity.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.common.DateHelper;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.jwt.JwtUtil;
import com.zpj.sys.entity.User;
import com.zpj.sys.service.LevelService;
import com.zpj.sys.service.UserService;

import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/app/level")
@Api(value = "/app/level",tags="用户等级接口", description = "用户等级接口")
public class LevelAppController extends BaseController{
	
	@Autowired
	private LevelService levelService;
	@Autowired
	private UserService userService;
	
	@RequestMapping("/setUserLevel")
    @ResponseBody
    @ApiOperation(value = "设置会员等级", notes = "设置会员等级", httpMethod = "POST")
    public void buyMember(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                          @ApiParam(required = true, name = "level", value = "等级")@RequestParam("level")Integer level){
        ResultData rd=new ResultData();
        try{
        	User u= getCurrentUser();
        	User user=userService.findById(u.getId());
        	Level le=levelService.findInfoById(level);
        	user.setLevel(level);
        	user.setLevelId(le.getId());
        	user.setMaxtime(le.getMaxtime());
        	user.setStartTime(new Date());
        	if(level==0){
        		//普通会员
        	}else if(level==1){
        		//月度会员
        		user.setEndTime(DateHelper.NextMonthToday(1));
        	}else if(level==2){
        		//季度会员
        		user.setEndTime(DateHelper.NextMonthToday(3));
        	}else if(level==3){
        		//年度会员
        		user.setEndTime(DateHelper.NextMonthToday(12));
        	}else if(level==4){
        		//终身会员
        		user.setEndTime(DateHelper.NextMonthToday(12*100));
        	}
        	//不过期
        	user.setIsExpire("0");
        	userService.saveInfo(user);
        	String new_tokens=JwtUtil.buildJsonByUser(user);
        	user.setToken(new_tokens);
        	rd.setData(user);
            rd.setCode(200);
            rd.setMsg("设置会员等级成功,返回新的token");
        }catch (JwtException e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("token转码失败，token过期");
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("设置会员等级失败");
        }
        this.jsonWrite2(rd);
    }

	
	
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

    @RequestMapping("/saveInfo")
    @ResponseBody
    @ApiOperation(value = "保存等级信息", notes = "保存等级信息", httpMethod = "POST")
    public void saveInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                                   @ApiParam(required = false, name = "id", value = "等级表主键")@RequestParam(value="id",required = false)String id,
                                   @ApiParam(required = true, name = "level", value = "level")@RequestParam("level")Integer level,
                         @ApiParam(required = true, name = "name", value = "name")@RequestParam("name")String name,
                         @ApiParam(required = true, name = "maxtime", value = "maxtime")@RequestParam("maxtime")Integer maxtime,
                         @ApiParam(required = true, name = "money", value = "money")@RequestParam("money")Integer money
                         ){
        ResultData rd=new ResultData();
        try{
            Map map=new HashMap();
            Level level1=new Level();
            if(id!=null && !id.equalsIgnoreCase("")){
                level1.setId(id);
            }
            level1.setLevel(level);
            level1.setDays(0);
            level1.setMaxtime(maxtime);
            level1.setName(name);
            level1.setMoney(money);
            levelService.saveInfo(level1);
            rd.setCode(200);
            rd.setMsg("保存成功");
        }catch (JwtException e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("token转码失败，token过期");
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("保存失败");
        }
        this.jsonWrite2(rd);
    }


}
