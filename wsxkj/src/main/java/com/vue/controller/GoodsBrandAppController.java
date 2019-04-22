package com.vue.controller;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.jwt.JwtUtil;
import com.zpj.materials.service.GoodsBrandService;
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
 * @ClassName: GoodsBrandAppController
 * @Description: TODO(商品品牌)
 * @author zpj
 * @date 2019/4/19 15:36
*/
@Controller
@RequestMapping("/app/goodsbrand")
@Api(value = "/app/goodsbrand",tags="商品品牌功能", description = "商品品牌功能接口")
public class GoodsBrandAppController extends BaseController {

    @Autowired
    private GoodsBrandService goodsBrandService;


    /*
     * @MethodName: findGoodsBrandList
     * @Description: TODO(商品品牌列表)
     * @params [token, name, cpage, pagerow]
     * @return void
     * @author zpj
     * @date 2019/4/19 16:37
    */
    @RequestMapping("/findGoodsTypeList")
    @ResponseBody
    @ApiOperation(value = "商品品牌列表", notes = "商品品牌列表", httpMethod = "POST")
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
            MyPage pagedata = goodsBrandService.findPageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
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
