package com.vue.controller;

import com.zpj.common.BaseController;
import com.zpj.common.DateHelper;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.materials.entity.Goods;
import com.zpj.materials.entity.Store;
import com.zpj.materials.service.GoodsService;
import com.zpj.materials.service.StoreService;
import com.zpj.sys.entity.User;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
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
    @Autowired
    private GoodsService goodsService;
    
    
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
    
    
    @RequestMapping("/updateStoreInfo")
    @ResponseBody
    @ApiOperation(value = "修改保存库存信息", notes = "修改保存库存信息", httpMethod = "POST")
    public void updateStoreInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                         @ApiParam(required = true, name = "storeId", value = "库存id")@RequestParam("storeId")String storeId,
                         @ApiParam(required = true, name = "inNum", value = "进货数量")@RequestParam("inNum")String inNum,
                         @ApiParam(required = true, name = "inPrice", value = "进货价格")@RequestParam("inPrice")String inPrice,
                         @ApiParam(required = true, name = "outPrice", value = "出货价格")@RequestParam("outPrice")String outPrice,
                         @ApiParam(required = false, name = "sureDate", value = "保质日期")@RequestParam(value="sureDate",required = false)String sureDate){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            boolean flag=false;//库存未被使用
            Store store=storeService.findById(storeId);
            double old_inNum=store.getInNum();
            String goodsId=store.getGoodsId();
            Goods goods=goodsService.findById(goodsId);
            Map map=new HashMap();
            double innum=0;
            double new_inNum=0;//经过计算后goods表中需要修改的值
            if(null!=inNum) innum=Double.parseDouble(inNum);
            new_inNum=goods.getStoreNum()-old_inNum+innum;
            if(new_inNum<0){
        		//说明该库存已经被使用过了
        		flag=true;
        	}
            if(flag){
            	rd.setCode(500);
            	rd.setMsg("该库存已经被使用过，不可修改。");
            }else{
            	goods.setStoreNum(new_inNum);
            	goodsService.saveInfo(goods);
            	//保存库存信息
            	if(innum>0){
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
                rd.setMsg("修改库存成功");
            }
            
        }catch (JwtException e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("token转码失败，token过期");
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("修改库存失败");
        }
        this.jsonWrite2(rd);
    }
    @Test
    @RequestMapping("/delStoreInfo")
    @ResponseBody
    @ApiOperation(value = "删除库存信息", notes = "删除库存信息", httpMethod = "POST")
    public void delStoreInfo(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
                         @ApiParam(required = true, name = "storeId", value = "库存id")@RequestParam("storeId")String storeId
                         ){
        ResultData rd=new ResultData();
        try{
            User user= getCurrentUser();
            boolean flag=false;//库存未被使用
            Store store=storeService.findById(storeId);
            double old_inNum=store.getInNum();
            String goodsId=store.getGoodsId();
            Goods goods=goodsService.findById(goodsId);
            Map map=new HashMap();
            double new_inNum=0;//经过计算后goods表中需要修改的值
            new_inNum=goods.getStoreNum()-old_inNum;
            if(new_inNum<0){
        		//说明该库存已经被使用过了
        		flag=true;
        	}
            if(flag){
            	rd.setCode(500);
            	rd.setMsg("该库存已经被使用过，不可删除。");
            }else{
            	goods.setStoreNum(new_inNum);
            	goodsService.saveInfo(goods);
            	//删除库存信息
            	storeService.delInfo(storeId);
            	rd.setCode(200);
                rd.setMsg("删除库存成功");
            }
            
        }catch (JwtException e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("token转码失败，token过期");
        }catch (Exception e){
            e.printStackTrace();
            rd.setCode(500);
            rd.setMsg("删除库存失败");
        }
        this.jsonWrite2(rd);
    }
    
    @RequestMapping("/findStoreInfoById")
    @ResponseBody
    @ApiOperation(value = "根据id查询库存信息", notes = "根据id查询库存信息", httpMethod = "POST")
    public void findStoreInfoById(@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
    							@ApiParam(required = true, name = "id", value = "id")@RequestParam("id")String id){
    	Store store=storeService.findById(id);
    	ResultData rd=new ResultData();
    	rd.setData(store);
    	rd.setCode(200);
    	rd.setMsg("查询成功");
    	this.jsonWrite2(rd);
    }
    
}
