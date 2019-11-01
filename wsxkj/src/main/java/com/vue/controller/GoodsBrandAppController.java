package com.vue.controller;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.jwt.JwtUtil;
import com.zpj.materials.entity.GoodsBrand;
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

import java.util.Date;
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
    @RequestMapping("/findGoodsBrandList")
    @ResponseBody
    @ApiOperation(value = "商品品牌列表", notes = "商品品牌列表", httpMethod = "POST")
    public void findGoodsBrandList(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                                  @ApiParam(required = false, name = "name", value = "名称")@RequestParam(value="name",required=false)String name,
                                  @ApiParam(required = false, name = "typeid", value = "分类id")@RequestParam(value="typeid",required=false)String typeid,
                                  @ApiParam(required = false, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                  @ApiParam(required = false, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
            map.put("name",filterStr(name));
            map.put("typeId", filterStr(typeid));
            map.put("userId",user.getId());
            MyPage pagedata = goodsBrandService.findPageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
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
    @ApiOperation(value = "商品品牌新增接口", notes = "商品品牌新增接口", httpMethod = "POST",response = GoodsBrand.class)
    public void saveInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                         @ApiParam(required = false, name = "id", value = "id")@RequestParam(value="id",required=false)String id,
                         @ApiParam(required = false, name = "name", value = "名称")@RequestParam(value="name",required=false)String name,
                         @ApiParam(required = false, name = "typeid", value = "分类id")@RequestParam(value="typeid",required=false)String typeid,
                         @ApiParam(required = false, name = "pictureid", value = "图片表id")@RequestParam(value = "pictureid",required=false)String pictureid){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            GoodsBrand goodsBrand=new GoodsBrand();
            if(judgeStr(id)){
                goodsBrand.setId(id);
            }
            goodsBrand.setTypeId(typeid);
            goodsBrand.setPictureId(pictureid);
            goodsBrand.setName(name);
            goodsBrand.setUpdateTime(new Date());
            goodsBrand.setUserId(user.getId());
            goodsBrandService.saveInfo(goodsBrand);
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
    @ApiOperation(value = "商品品牌删除接口", notes = "商品品牌删除接口", httpMethod = "POST",response = GoodsBrand.class)
    public void delInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                         @ApiParam(required = false, name = "id", value = "id")@RequestParam(value="id",required=false)String id){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            int ret=goodsBrandService.delInfo(id,user);
            rd.setCode(200);
            if(ret==1){
            	rd.setMsg("删除成功");
            }else{
            	rd.setCode(500);
            	rd.setMsg("品牌有管理的商品信息，不能删除");
            }
        }catch (JwtException e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("token转码失败，token过期");
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("删除失败");
        }
        this.jsonWrite2(rd);
    }

    @RequestMapping("/findBrandPictureList")
    @ResponseBody
    @ApiOperation(value = "品牌图片列表", notes = "品牌图片列表", httpMethod = "POST")
    public void findBrandPictureList(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                                    @ApiParam(required = false, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                   @ApiParam(required = false, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
            map.put("userId",user.getId());
            MyPage pagedata = goodsBrandService.findGBrandPicturePageData(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
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
