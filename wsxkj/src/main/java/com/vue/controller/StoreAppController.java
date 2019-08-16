package com.vue.controller;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.materials.entity.Goods;
import com.zpj.materials.service.StoreService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zpj
 * @ClassName: StoreAppController
 * @Description: TODO(进货库存)
 * @date 2019/7/31
 */
@Controller
@RequestMapping("/app/store")
@Api(value = "/app/store",tags="库存功能", description = "库存功能接口")
public class StoreAppController extends BaseController {
    @Autowired
    private StoreService storeService;

    @RequestMapping("/findStoreList")
    @ResponseBody
    @ApiOperation(value = "进货列表", notes = "商品进货列表", httpMethod = "POST",response = Goods.class )
    public void findStoreList(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                              @ApiParam(required = false, name = "mdate", value = "月份格式'2019-07'")@RequestParam(value="mdate",required = false)String mdate,
                              @ApiParam(required = true, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                              @ApiParam(required = true, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user=getCurrentUser();
            Map map=new HashMap();
            map.put("userId",user.getId());
            map.put("mdate", filterStr(mdate));
            List list = storeService.findMultiData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
            rd.setData(list);
            rd.setCount(0);
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


    @RequestMapping("/findStoreStatistics")
    @ResponseBody
    @ApiOperation(value = "进货进货记录统计", notes = "进货进货记录统计", httpMethod = "POST",response = Goods.class )
    public void findStoreStatistics(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                              @ApiParam(required = false, name = "mdate", value = "月份格式'2019-07'")@RequestParam(value="mdate",required = false)String mdate){
        ResultData rd=new ResultData();
        try{
            User user=getCurrentUser();
            Map map=new HashMap();
            map.put("userId",user.getId());
            map.put("mdate", filterStr(mdate));
            Map lm = storeService.findStatisticsData(map);
            rd.setData(lm);
            rd.setCount(0);
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
