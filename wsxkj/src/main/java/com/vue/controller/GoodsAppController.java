package com.vue.controller;

import com.zpj.common.BaseController;
import com.zpj.common.DateHelper;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.jwt.JwtUtil;
import com.zpj.materials.entity.Goods;
import com.zpj.materials.entity.Store;
import com.zpj.materials.service.GoodsService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * @ClassName: GoodsAppController
 * @Description: TODO(商品信息接口)
 * @author zpj
 * @date 2019/4/18 16:26
*/
@Controller
@RequestMapping("/vue/goods")
@Api(value = "/vue/goods",tags="商品功能", description = "商品功能接口")
public class GoodsAppController extends BaseController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private StoreService storeService;




    @RequestMapping("/saveNewInfo")
    @ResponseBody
    @ApiOperation(value = "新商品保存", notes = "新商品保存", httpMethod = "POST" ,response = Goods.class)
    public void saveNewInfo(@ApiParam(required = false, name = "token", value = "token")@RequestParam("token")String token,
                         @ApiParam(required = false, name = "name", value = "名称")@RequestParam("name")String name,
                         @ApiParam(required = false, name = "qrCode", value = "条形码")@RequestParam("qrCode")String qrCode,
                         @ApiParam(required = false, name = "picture", value = "图片路径")@RequestParam("picture")String picture,
                         @ApiParam(required = false, name = "goodsType", value = "商品类型")@RequestParam("goodsType")String goodsType,
                         @ApiParam(required = false, name = "goodsBrand", value = "商品品牌")@RequestParam("goodsBrand")String goodsBrand,
                         @ApiParam(required = false, name = "inNum", value = "进货数量")@RequestParam("inNum")String inNum,
                         @ApiParam(required = false, name = "inPrice", value = "进货价格")@RequestParam("inPrice")String inPrice,
                         @ApiParam(required = false, name = "outPrice", value = "出货价格")@RequestParam("outPrice")String outPrice,
                         @ApiParam(required = false, name = "sureDate", value = "保质日期")@RequestParam("sureDate")String sureDate,
                         @ApiParam(required = false, name = "isSpot", value = "是否现货")@RequestParam("isSpot")String isSpot){
        ResultData rd=new ResultData();
        try{
            User user= JwtUtil.getUserByJson(token);
            Map map=new HashMap();
            double innum=0;
            if(null!=inNum) innum=Double.parseDouble(inNum);

            //保存新商品信息
            Goods goods=new Goods();
            goods.setCreateTime(new Date());
            goods.setGoodsBrand(goodsBrand);
            goods.setGoodsType(goodsType);
            goods.setName(name);
            goods.setPicture(picture);
            goods.setQrCode(qrCode);
            goods.setStoreNum(innum);
            goods.setUserId(user.getId());
            goodsService.saveInfo(goods);
            //保存库存信息
            Store store=new Store();
            store.setGoodsId(goods.getId());
            store.setUserId(user.getId());
            store.setInDate(new Date());
            store.setInNum(innum);
            store.setStoreNum(innum);
            store.setInPrice(Double.parseDouble(inPrice));
            store.setOutPrice(Double.parseDouble(outPrice));
            store.setSureDate(DateHelper.getStringDate(sureDate,"yyyy-MM-dd"));
            store.setIsSpot(isSpot);
            store.setUpdateTime(new Date());
            storeService.saveInfo(store);
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

    @RequestMapping("/saveOldInfo")
    @ResponseBody
    @ApiOperation(value = "老商品保存", notes = "老商品保存", httpMethod = "POST")
    public void saveOldInfo(@ApiParam(required = false, name = "token", value = "token")@RequestParam("token")String token,
                         @ApiParam(required = false, name = "goodsId", value = "商品id")@RequestParam("goodsId")String goodsId,
                         @ApiParam(required = false, name = "inNum", value = "进货数量")@RequestParam("inNum")String inNum,
                         @ApiParam(required = false, name = "inPrice", value = "进货价格")@RequestParam("inPrice")String inPrice,
                         @ApiParam(required = false, name = "outPrice", value = "出货价格")@RequestParam("outPrice")String outPrice,
                         @ApiParam(required = false, name = "sureDate", value = "保质日期")@RequestParam("sureDate")String sureDate,
                         @ApiParam(required = false, name = "isSpot", value = "是否现货")@RequestParam("isSpot")String isSpot){
        ResultData rd=new ResultData();
        try{
            User user= JwtUtil.getUserByJson(token);
            Map map=new HashMap();
            double innum=0;
            if(null!=inNum) innum=Double.parseDouble(inNum);

            //保存商品信息
            Goods goods=goodsService.findById(goodsId);
            goods.setStoreNum(goods.getStoreNum()+innum);
            goodsService.saveInfo(goods);
            //保存库存信息
            Store store=new Store();
            store.setGoodsId(goodsId);
            store.setUserId(user.getId());
            store.setInDate(new Date());
            store.setInNum(innum);
            store.setStoreNum(innum);
            store.setInPrice(Double.parseDouble(inPrice));
            store.setOutPrice(Double.parseDouble(outPrice));
            store.setSureDate(DateHelper.getStringDate(sureDate,"yyyy-MM-dd"));
            store.setIsSpot(isSpot);
            store.setUpdateTime(new Date());
            storeService.saveInfo(store);
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
