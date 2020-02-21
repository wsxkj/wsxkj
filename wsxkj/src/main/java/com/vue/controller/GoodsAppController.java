package com.vue.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.common.DateHelper;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.materials.entity.Customer;
import com.zpj.materials.entity.Goods;
import com.zpj.materials.entity.Store;
import com.zpj.materials.service.GoodsService;
import com.zpj.materials.service.StoreService;
import com.zpj.sys.entity.User;
import com.zpj.sys.service.UserService;

import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/*
 * @ClassName: GoodsAppController
 * @Description: TODO(商品信息接口)
 * @author zpj
 * @date 2019/4/18 16:26
*/
@Controller
@RequestMapping("/app/goods")
@Api(value = "/app/goods",tags="商品功能", description = "商品功能接口")
public class GoodsAppController extends BaseController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private StoreService storeService;
    @Autowired
	public UserService userService;
    
    @RequestMapping("/saveGoodsInfo")
    @ResponseBody
    @ApiOperation(value = "商品编辑保存", notes = "商品编辑保存", httpMethod = "POST" ,response = Goods.class)
    public void saveGoodsInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
    					 @ApiParam(required = true, name = "id", value = "商品id")@RequestParam("id")String id,
                         @ApiParam(required = true, name = "name", value = "名称")@RequestParam("name")String name,
                         @ApiParam(required = false, name = "qrCode", value = "条形码")@RequestParam(value="qrCode",required = false)String qrCode,
                         @ApiParam(required = false, name = "picture", value = "图片路径")@RequestParam(value="picture",required = false)String picture,
                         @ApiParam(required = true, name = "goodsType", value = "商品类型")@RequestParam("goodsType")String goodsType,
                         @ApiParam(required = true, name = "goodsBrand", value = "商品品牌")@RequestParam("goodsBrand")String goodsBrand){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
//            double innum=0;

            //保存商品信息
            Goods goods = goodsService.findById(id);
            goods.setUpdateTime(new Date());
            goods.setGoodsBrand(goodsBrand);
            goods.setGoodsType(goodsType);
            goods.setName(name);
            goods.setPicture(picture);
            goods.setQrCode(qrCode);
//            goods.setStoreNum(innum);
            goods.setUserId(user.getId());
            
            goodsService.saveInfo(goods);
            
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
    



    @RequestMapping("/saveNewInfo")
    @ResponseBody
    @ApiOperation(value = "新商品保存", notes = "新商品保存", httpMethod = "POST" ,response = Goods.class)
    public void saveNewInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                         @ApiParam(required = true, name = "name", value = "名称")@RequestParam("name")String name,
                         @ApiParam(required = false, name = "qrCode", value = "条形码")@RequestParam(value="qrCode",required = false)String qrCode,
                         @ApiParam(required = false, name = "picture", value = "图片路径")@RequestParam(value="picture",required = false)String picture,
                         @ApiParam(required = true, name = "goodsType", value = "商品类型")@RequestParam("goodsType")String goodsType,
                         @ApiParam(required = true, name = "goodsBrand", value = "商品品牌")@RequestParam("goodsBrand")String goodsBrand,
                         @ApiParam(required = true, name = "inNum", value = "进货数量")@RequestParam("inNum")String inNum,
                         @ApiParam(required = true, name = "inPrice", value = "进货价格")@RequestParam("inPrice")String inPrice,
                         @ApiParam(required = true, name = "outPrice", value = "出货价格")@RequestParam("outPrice")String outPrice,
                         @ApiParam(required = false, name = "sureDate", value = "保质日期")@RequestParam(value="sureDate",required = false)String sureDate){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
            double innum=0;
            if(null!=inNum) innum=Double.parseDouble(inNum);

            //保存新商品信息
            Goods goods=new Goods();
            goods.setUpdateTime(new Date());
            goods.setGoodsBrand(goodsBrand);
            goods.setGoodsType(goodsType);
            goods.setName(name);
            goods.setPicture(picture);
            goods.setQrCode(qrCode);
            goods.setStoreNum(innum);
            goods.setUserId(user.getId());
            goodsService.saveInfo(goods);
            //保存库存信息
            if(innum>0){
            	Store store=new Store();
            	store.setGoodsId(goods.getId());
            	store.setUserId(user.getId());
            	store.setInDate(new Date());
            	store.setInNum(innum);
            	store.setStoreNum(innum);
            	store.setInPrice(Double.parseDouble(inPrice));
            	store.setOutPrice(Double.parseDouble(outPrice));
            	if(judgeStr(sureDate)){
            		store.setSureDate(DateHelper.getStringDate(sureDate,"yyyy-MM-dd"));
            	}
            	store.setUpdateTime(new Date());
            	storeService.saveInfo(store);
            }
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

    @RequestMapping("/saveOldInfo")
    @ResponseBody
    @ApiOperation(value = "老商品保存", notes = "老商品保存", httpMethod = "POST")
    public void saveOldInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                         @ApiParam(required = true, name = "goodsId", value = "商品id")@RequestParam("goodsId")String goodsId,
                         @ApiParam(required = true, name = "inNum", value = "进货数量")@RequestParam("inNum")String inNum,
                         @ApiParam(required = true, name = "inPrice", value = "进货价格")@RequestParam("inPrice")String inPrice,
                         @ApiParam(required = true, name = "outPrice", value = "出货价格")@RequestParam("outPrice")String outPrice,
                         @ApiParam(required = false, name = "sureDate", value = "保质日期")@RequestParam(value="sureDate",required = false)String sureDate){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
            double innum=0;
            if(null!=inNum) innum=Double.parseDouble(inNum);

            //保存商品信息
            Goods goods=goodsService.findById(goodsId);
            goods.setStoreNum(goods.getStoreNum()+innum);
            goodsService.saveInfo(goods);
            //保存库存信息
            if(innum>0){
            	Store store=new Store();
            	store.setGoodsId(goodsId);
            	store.setUserId(user.getId());
            	store.setInDate(new Date());
            	store.setInNum(innum);
            	store.setStoreNum(innum);
            	store.setInPrice(Double.parseDouble(inPrice));
            	store.setOutPrice(Double.parseDouble(outPrice));
            	if(judgeStr(sureDate)){
            		store.setSureDate(DateHelper.getStringDate(sureDate,"yyyy-MM-dd"));
            	}
            	store.setUpdateTime(new Date());
            	storeService.saveInfo(store);
            }
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
    @ApiOperation(value = "删除商品信息", notes = "删除商品信息", httpMethod = "POST" ,response = Customer.class)
    public void deleteInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                            @ApiParam(required = true, name = "id", value = "商品id主键")@RequestParam("id")String id
                           ){
        ResultData rd=new ResultData();
        try{

        	goodsService.deleteInfo(id);
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
    
    
    @RequestMapping("/findGoodsList")
    @ResponseBody
    @ApiOperation(value = "商品列表", notes = "商品列表", httpMethod = "POST",response = Goods.class )
    public void findGoodsList(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
    		@ApiParam(required = false, name = "typeId", value = "商品分类id")@RequestParam(value="typeId",required = false)String typeId,
    		@ApiParam(required = false, name = "brandId", value = "商品品牌id")@RequestParam(value="brandId",required = false)String brandId,
    		@ApiParam(required = true, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                   @ApiParam(required = true, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user=getCurrentUser();
            Map map=new HashMap();
            map.put("userId",user.getId());
            map.put("typeId", filterStr(typeId));
            map.put("brandId", filterStr(brandId));
            List list = goodsService.findMultiGoods_v1_1_0(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
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
    
    @RequestMapping("/searchGoodsList")
    @ResponseBody
    @ApiOperation(value = "查询商品列表", notes = "查询商品列表", httpMethod = "POST",response = Goods.class )
    public void searchGoodsList(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
    		@ApiParam(required = false, name = "key", value = "名称")@RequestParam(value="key",required = false)String key,
    		@ApiParam(required = true, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                   @ApiParam(required = true, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
            map.put("userId",user.getId());
            map.put("name", filterStr(key));
            List list = goodsService.findMultiGoods_v1_1_0(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
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
    
    
    @RequestMapping("/findGoodsById")
    @ResponseBody
    @ApiOperation(value = "查询商品详细信息", notes = "查询商品详细信息", httpMethod = "POST",response = Goods.class )
    public void findGoodsById(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
    		@ApiParam(required = false, name = "id", value = "商品Id主键")@RequestParam(value="id")String id){
    	ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
        	Goods goods = goodsService.findById(id);
            rd.setData(goods);
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
    
    
    @RequestMapping("/findStoreInfoByGoodsId")
    @ResponseBody
    @ApiOperation(value = "查询商品批次信息", notes = "查询商品批次信息", httpMethod = "POST",response = Goods.class )
    public void findStoreInfoByGoodsId(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
    		@ApiParam(required = false, name = "id", value = "商品Id主键")@RequestParam(value="id")String id){
    	ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
            map.put("userId",user.getId());
            map.put("goodsId", filterStr(id));
        	MyPage mp = storeService.findPageData(map, page, limit);
            rd.setData(mp.data);
            rd.setCount(mp.count);
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
    
    
 /**************************v1_1_0版本新接口**开始*****************************/
    
    @RequestMapping("/v1_1_0/saveGoodsInfo")
    @ResponseBody
    @ApiOperation(value = "商品编辑保存", notes = "商品编辑保存", httpMethod = "POST" ,response = Goods.class)
    public void saveGoodsInfo_v1_1_0(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
    					 @ApiParam(required = true, name = "id", value = "商品id")@RequestParam("id")String id,
                         @ApiParam(required = true, name = "name", value = "名称")@RequestParam("name")String name,
                         @ApiParam(required = false, name = "qrCode", value = "条形码")@RequestParam(value="qrCode",required = false)String qrCode,
                         @ApiParam(required = false, name = "picture", value = "图片路径")@RequestParam(value="picture",required = false)String picture,
                         @ApiParam(required = true, name = "goodsType", value = "商品类型")@RequestParam("goodsType")String goodsType,
                         @ApiParam(required = true, name = "goodsBrand", value = "商品品牌")@RequestParam("goodsBrand")String goodsBrand,
                         @ApiParam(required = false, name = "effect", value = "功效")@RequestParam(value="effect",required = false)String effect,
                         @ApiParam(required = false, name = "suitableForSkin", value = "适用肤质")@RequestParam(value="suitableForSkin",required = false)String suitableForSkin,
                         @ApiParam(required = false, name = "detail", value = "详细")@RequestParam(value="detail",required = false)String detail,
                         @ApiParam(required = false, name = "isPublish", value = "是否发布1发布，0不发布")@RequestParam(value="isPublish",required = false)String isPublish,
                         @ApiParam(required = false, name = "soldPrice", value = "售价")@RequestParam(value="soldPrice",required = false)double soldPrice
                         
    		){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
//            double innum=0;

            //保存商品信息
            Goods goods = goodsService.findById(id);
            goods.setUpdateTime(new Date());
            goods.setGoodsBrand(goodsBrand);
            goods.setGoodsType(goodsType);
            goods.setName(name);
            goods.setPicture(picture);
            goods.setQrCode(qrCode);
            goods.setEffect(effect);
            goods.setSoldPrice(soldPrice);
            goods.setSuitableForSkin(suitableForSkin);
            goods.setDetail(detail);
            goods.setIsPublish(isPublish);
//            goods.setStoreNum(innum);
            goods.setUserId(user.getId());
            
            goodsService.saveInfo(goods);
            
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
    
    
    @RequestMapping("/v1_1_0/saveNewInfo")
    @ResponseBody
    @ApiOperation(value = "新商品保存", notes = "新商品保存", httpMethod = "POST" ,response = Goods.class)
    public void saveNewInfo_v1_1_0(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                         @ApiParam(required = true, name = "name", value = "名称")@RequestParam("name")String name,
                         @ApiParam(required = false, name = "qrCode", value = "条形码")@RequestParam(value="qrCode",required = false)String qrCode,
                         @ApiParam(required = false, name = "picture", value = "图片路径")@RequestParam(value="picture",required = false)String picture,
                         @ApiParam(required = true, name = "goodsType", value = "商品类型")@RequestParam("goodsType")String goodsType,
                         @ApiParam(required = true, name = "goodsBrand", value = "商品品牌")@RequestParam("goodsBrand")String goodsBrand,
                         @ApiParam(required = true, name = "inNum", value = "进货数量")@RequestParam("inNum")String inNum,
                         @ApiParam(required = true, name = "inPrice", value = "进货价格")@RequestParam("inPrice")String inPrice,
                         @ApiParam(required = true, name = "outPrice", value = "出货价格")@RequestParam("outPrice")String outPrice,
                         @ApiParam(required = false, name = "sureDate", value = "保质日期")@RequestParam(value="sureDate",required = false)String sureDate,
                         @ApiParam(required = false, name = "effect", value = "功效")@RequestParam(value="effect",required = false)String effect,
                         @ApiParam(required = false, name = "suitableForSkin", value = "适用肤质")@RequestParam(value="suitableForSkin",required = false)String suitableForSkin,
                         @ApiParam(required = false, name = "detail", value = "详细")@RequestParam(value="detail",required = false)String detail,
                         @ApiParam(required = false, name = "isPublish", value = "是否发布1发布，0不发布")@RequestParam(value="isPublish",required = false)String isPublish,
                         @ApiParam(required = false, name = "soldPrice", value = "售价")@RequestParam(value="soldPrice",required = false)double soldPrice
    		){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
            double innum=0;
            if(null!=inNum) innum=Double.parseDouble(inNum);

            //保存新商品信息
            Goods goods=new Goods();
            goods.setUpdateTime(new Date());
            goods.setGoodsBrand(goodsBrand);
            goods.setGoodsType(goodsType);
            goods.setName(name);
            goods.setPicture(picture);
            goods.setQrCode(qrCode);
            goods.setStoreNum(innum);
            goods.setUserId(user.getId());
            goods.setEffect(effect);
            goods.setSoldPrice(soldPrice);
            goods.setSuitableForSkin(suitableForSkin);
            goods.setDetail(detail);
            goods.setIsPublish(isPublish);
            goodsService.saveInfo(goods);
            //保存库存信息
            if(innum>0){
            	Store store=new Store();
            	store.setGoodsId(goods.getId());
            	store.setUserId(user.getId());
            	store.setInDate(new Date());
            	store.setInNum(innum);
            	store.setStoreNum(innum);
            	store.setInPrice(Double.parseDouble(inPrice));
            	store.setOutPrice(Double.parseDouble(outPrice));
            	if(judgeStr(sureDate)){
            		store.setSureDate(DateHelper.getStringDate(sureDate,"yyyy-MM-dd"));
            	}
            	store.setUpdateTime(new Date());
            	storeService.saveInfo(store);
            }
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
    @RequestMapping("/v1_1_0/findGoodsByIdH5")
    @ResponseBody
    @ApiOperation(value = "查询商品详细信息 增加浏览次数，同时不用token可以直接访问", notes = "查询商品详细信息", httpMethod = "POST",response = Goods.class )
    public void findGoodsById_v1_1_0(
    		@ApiParam(required = false, name = "id", value = "商品Id主键")@RequestParam(value="id")String id){
    	ResultData rd=new ResultData();
        try{
//            User user= getCurrentUser();
        	Goods goods = goodsService.findById(id);
        	goods.setGlanceTimes(goods.getGlanceTimes()+1);
        	goodsService.saveInfo(goods);
            rd.setData(goods);
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
    
    @RequestMapping("/v1_1_0/setIsPublishByGoodsId")
    @ResponseBody
    @ApiOperation(value = "根据商品id设置是否发布", notes = "根据商品id设置是否发布", httpMethod = "POST",response = Goods.class )
    public void setIsPublishByGoodsId_v1_1_0(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
    		@ApiParam(required = false, name = "id", value = "商品Id主键")@RequestParam(value="id")String id,
    		@ApiParam(required = false, name = "isPublish", value = "是否发布，1发布，0不发布")@RequestParam(value="isPublish",required = false)String isPublish){
    	ResultData rd=new ResultData();
        try{
//            User user= getCurrentUser();
        	Goods goods = goodsService.findById(id);
        	if(isPublish!=null&&!isPublish.equalsIgnoreCase("")){
        		goods.setIsPublish(isPublish);
        	}else{
        		goods.setIsPublish("0");
        	}
        	goodsService.saveInfo(goods);
            rd.setData(goods);
            rd.setCode(200);
            rd.setMsg("设置成功");
        }catch (JwtException e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("token转码失败，token过期");
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("设置失败");
        }
        this.jsonWrite2(rd);
    }
    
    @RequestMapping("/v1_1_0/searchGoodsList")
    @ResponseBody
    @ApiOperation(value = "查询商品列表带发布选项", notes = "查询商品列表", httpMethod = "POST",response = Goods.class )
    public void searchGoodsList_v1_1_0(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
    		@ApiParam(required = false, name = "key", value = "名称")@RequestParam(value="key",required = false)String key,
    		@ApiParam(required = false, name = "isPublish", value = "是否发布，1发布，0不发布")@RequestParam(value="isPublish",required = false)String isPublish,
    		@ApiParam(required = true, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                   @ApiParam(required = true, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Map map=new HashMap();
            map.put("userId",user.getId());
            map.put("name", filterStr(key));
            map.put("isPublish",isPublish);
            List list = goodsService.findMultiGoods_v1_1_0(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
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
    @RequestMapping("/v1_1_0/searchGoodsListH5")
    @ResponseBody
    @ApiOperation(value = "查询商品列表带发布选项", notes = "查询商品列表", httpMethod = "POST",response = Goods.class )
    public void searchGoodsList1_v1_1_0(@ApiParam(required = true, name = "userid", value = "userid")@RequestParam("userid")String userid,
    		@ApiParam(required = false, name = "key", value = "名称")@RequestParam(value="key",required = false)String key,
    		@ApiParam(required = false, name = "isPublish", value = "是否发布，1发布，0不发布")@RequestParam(value="isPublish",required = false)String isPublish,
    		@ApiParam(required = true, name = "cpage", value = "当前页")@RequestParam("cpage")String cpage,
                                   @ApiParam(required = true, name = "pagerow", value = "pagerow")@RequestParam("pagerow")String pagerow){
        ResultData rd=new ResultData();
        try{
        	
            User user= userService.findById(userid);
            if(user!=null){
            	 Map map=new HashMap();
                 map.put("userId",user.getId());
                 map.put("name", filterStr(key));
                 map.put("isPublish",isPublish);
                 List list = goodsService.findMultiGoods_v1_1_0(map,Integer.parseInt(cpage),Integer.parseInt(pagerow));
                 rd.setData(list);
                 rd.setCount(0);
                 rd.setCode(200);
                 rd.setMsg("查询成功");
            }else{
            	rd.setData(new ArrayList());
                rd.setCount(0);
                rd.setCode(500);
                rd.setMsg("没有关联到用户数据");
            }
           
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
    
    /**************************v1_1_0版本新接口**结束*****************************/
    
    /**************************v1_3_0版本新接口**开始*****************************/
    @RequestMapping("/v1_3_0/findGoodsByQrcode")
    @ResponseBody
    @ApiOperation(value = "查询商品详细信息", notes = "查询商品详细信息", httpMethod = "POST",response = Goods.class )
    public void findGoodsByQrcode(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
    		@ApiParam(required = false, name = "qrCode", value = "条形码")@RequestParam(value="qrCode")String qrCode){
    	ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            Goods goods1=goodsService.findByQrcode(qrCode,user.getId());
            if(null!=goods1){
            	Goods goods = goodsService.findById_v1_1_0(goods1.getId());
            	rd.setData(goods);
            }else{
            	rd.setData("");
            }
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
    /**************************v1_3_0版本新接口**结束*****************************/
}
