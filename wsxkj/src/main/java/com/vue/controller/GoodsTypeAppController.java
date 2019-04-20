package com.vue.controller;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.jwt.JwtUtil;
import com.zpj.materials.service.GoodsTypeService;
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
import java.util.Map;

/*
 * @ClassName: GoodsTypeAppController
 * @Description: TODO(商品分类接口)
 * @author zpj
 * @date 2019/4/19 15:31
*/
@Controller
@RequestMapping("/vue/goodstype")
@Api(value = "/vue/goodstype",tags="商品分类功能", description = "商品分类功能接口")
public class GoodsTypeAppController extends BaseController {

    @Autowired
    private GoodsTypeService goodsTypeService;


    /*
     * @MethodName: findGoodsTypeList
     * @Description: TODO(商品分类)
     * @params [token, name, cpage, pagerow]
     * @return void
     * @author zpj
     * @date 2019/4/19 16:38
    */
    @RequestMapping("/findGoodsTypeList")
    @ResponseBody
    @ApiOperation(value = "商品分类列表", notes = "商品分类列表", httpMethod = "POST")
    public void findGoodsTypeList(@ApiParam(required = false, name = "token", value = "token")@RequestParam("token")String token,
                                  @ApiParam(required = false, name = "name", value = "名称")@RequestParam("name")String name,
                                  @ApiParam(required = false, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                  @ApiParam(required = false, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user= JwtUtil.getUserByJson(token);
            Map map=new HashMap();
            map.put("name",name);
            map.put("userId",user.getId());
            MyPage pagedata = goodsTypeService.findPageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
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
}
