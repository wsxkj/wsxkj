package com.vue.controller;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.jwt.JwtUtil;
import com.zpj.materials.entity.Customer;
import com.zpj.materials.entity.Goods;
import com.zpj.materials.entity.GoodsType;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * @ClassName: GoodsTypeAppController
 * @Description: TODO(商品分类接口)
 * @author zpj
 * @date 2019/4/19 15:31
*/
@Controller
@RequestMapping("/app/goodstype")
@Api(value = "/app/goodstype",tags="商品分类功能", description = "商品分类功能接口")
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
    public void findGoodsTypeList(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                                  @ApiParam(required = false, name = "name", value = "名称")@RequestParam(value="name",required=false)String name,
                                  @ApiParam(required = true, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                  @ApiParam(required = true, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
            map.put("name",filterStr(name));
            map.put("userId",user.getId());
            MyPage pagedata = goodsTypeService.findPageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
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
    @ApiOperation(value = "保存商品类型", notes = "保存商品类型", httpMethod = "POST" ,response = GoodsType.class)
    public void saveInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                        @ApiParam( required = false ,name = "id", value = "类型id主键")@RequestParam(value="id",required = false)String id,
                         @ApiParam( required = true ,name = "name", value = "名称")@RequestParam(value="name",required = true)String name
    ){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            GoodsType goodsType=new GoodsType();
            if(judgeStr(id)){
                goodsType.setId(id);
            }
            goodsType.setName(name);
            goodsType.setUpdateTime(new Date());
            goodsType.setUserId(user.getId());
            goodsTypeService.saveInfo(goodsType);

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
    
    @RequestMapping("/delInfo")
    @ResponseBody
    @ApiOperation(value = "删除类型", notes = "删除类型", httpMethod = "POST" ,response = GoodsType.class)
    public void delInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                            @ApiParam(required = true, name = "id", value = "类型id主键")@RequestParam(value="id",required = true)String id
                           ){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            goodsTypeService.delInfo(id,user);
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
