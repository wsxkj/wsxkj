package com.vue.controller;


import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.entity.User;
import com.zpj.sys.service.LogInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@Controller
@RequestMapping("/app/log")
@Api(value = "/app/log",tags="日志接口", description = "日志接口")
public class LogAppController extends BaseController {

    @Autowired
    private LogInfoService logInfoService;

    @RequestMapping("/findList")
    @ResponseBody
    @ApiOperation(value = "日志列表", notes = "日志列表", httpMethod = "GET")
    public void findList(String cpage,String pagerow,String loginId,String isAdmin){
        Map param=getRequestMap();
        User user=getCurrentUser();
        MyPage pagedata = logInfoService.findPageData(Integer.parseInt(cpage),Integer.parseInt(pagerow));
        Map map=new HashMap();
        if(null==pagedata.getData()){
            map.put("list", new ArrayList());
        }else{
            map.put("list", pagedata.getData());
        }
        int tot=(Integer)pagedata.getCount();
        double totalPage=Math.ceil((float)tot/Integer.parseInt(pagerow));
        map.put("totalPage", totalPage);
        map.put("count", tot);
        this.jsonWrite2(map);
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @ApiOperation(value = "日志详细信息", notes = "日志详细信息", httpMethod = "GET")
    @ResponseBody
    public void findById(@ApiParam(required = true, name = "id", value = "id") @RequestParam("id") String id){
        LogInfo u=logInfoService.findById(id);
        jsonWrite2(u);
    }
}
