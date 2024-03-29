package com.zpj.materials.entity;

import java.util.Date;

import javax.persistence.*;

import com.zpj.common.UUIDGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/*
 * @ClassName: Goods
 * @Description: 商品信息表
 * @author zpj
 * @date 2019/4/1 14:25
*/
@Entity
@Table(name = "jl_material_goods_info")
@ApiModel(value = "商品表", description = "商品信息表")
public class Goods implements java.io.Serializable{
	
	@ApiModelProperty(value = "主键",name="id", required = true)
	private String id= UUIDGenerator.generatePk("goods");
	@ApiModelProperty(value = "商品名称",name="name", required = false)
	private String name;
	@ApiModelProperty(value = "用户表id",name="userId", required = false)
	private String userId;
	@ApiModelProperty(value = "条形码，二维码",name="qrCode", required = false)
	private String qrCode;
	@ApiModelProperty(value = "商品图片,多个用英文，分割",name="picture", required = false)
	private String picture;
	@ApiModelProperty(value = "商品分类",name="goodsType", required = false)
	private String goodsType;
	@ApiModelProperty(value = "商品品牌",name="goodsBrand", required = false)
	private String goodsBrand;
	@ApiModelProperty(value = "修改时间",name="updateTime", required = false)
	private Date updateTime;
	@ApiModelProperty(value = "总库存数量",name="storeNum", required = false)
	private double storeNum;
	/**********v1_1_0新增*****开始********/
	@ApiModelProperty(value = "功效",name="effect", required = false)
	private String effect;
	@ApiModelProperty(value = "适用肤质",name="suitableForSkin", required = false)
	private String suitableForSkin;
	@ApiModelProperty(value = "详情",name="detail", required = false)
	private String detail;
	@ApiModelProperty(value = "是否发布，1发布，0不发布",name="isPublish", required = false)
	private String isPublish;
	@ApiModelProperty(value = "浏览次数",name="glanceTimes", required = false)
	private int glanceTimes=0;
	@ApiModelProperty(value = "售价",name="soldPrice", required = false)
	private double soldPrice=0;
	
	/**********v1_1_0新增****结束*********/
	
	/***********稍后设置填充临时值用**开始************/
	@ApiModelProperty(value = "总进货数量",name="totalInNum", required = false)
	private double totalInNum;
	@ApiModelProperty(value = "总售出数量",name="totalSoldNum", required = false)
	private double totalSoldNum;
	@ApiModelProperty(value = "总售出额",name="totoalSoldPrice", required = false)
	private double totoalSoldPrice;
	@ApiModelProperty(value = "售价最高价",name="maxOutPrice", required = false)
	private double maxOutPrice;
	
	@ApiModelProperty(value = "售价最低价",name="minOutPrice", required = false)
	private double minOutPrice;
	
	@ApiModelProperty(value = "进价最高价",name="maxInPrice", required = false)
	private double maxInPrice;
	
	@ApiModelProperty(value = "进价最低价",name="minInPrice", required = false)
	private double minInPrice;
	
	@ApiModelProperty(value = "商品分类中文",name="minInPrice", required = false)
	private String goodsTypeName;
	
	@ApiModelProperty(value = "商品品牌中文",name="minInPrice", required = false)
	private String goodsBrandName;
	
	
	/***********稍后设置填充临时值用**结束************/
	
	
	
	@Id
	@Column(name = "id",  nullable = false, length=50)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getGoodsBrand() {
		return goodsBrand;
	}

	public void setGoodsBrand(String goodsBrand) {
		this.goodsBrand = goodsBrand;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime", length=7)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public double getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(double storeNum) {
		this.storeNum = storeNum;
	}

	
	
	
	
	
	@Override
	public String toString() {
		return "id:"+this.getId()+
				",name:"+this.getName();

	}
	@Transient
	public double getMaxOutPrice() {
		return maxOutPrice;
	}
	public void setMaxOutPrice(double maxOutPrice) {
		this.maxOutPrice = maxOutPrice;
	}
	@Transient
	public double getMinOutPrice() {
		return minOutPrice;
	}
	public void setMinOutPrice(double minOutPrice) {
		this.minOutPrice = minOutPrice;
	}
	@Transient
	public double getMaxInPrice() {
		return maxInPrice;
	}
	public void setMaxInPrice(double maxInPrice) {
		this.maxInPrice = maxInPrice;
	}
	@Transient
	public double getMinInPrice() {
		return minInPrice;
	}
	public void setMinInPrice(double minInPrice) {
		this.minInPrice = minInPrice;
	}
	@Transient
	public double getTotalInNum() {
		return totalInNum;
	}
	public void setTotalInNum(double totalInNum) {
		this.totalInNum = totalInNum;
	}
	@Transient
	public double getTotalSoldNum() {
		return totalSoldNum;
	}
	public void setTotalSoldNum(double totalSoldNum) {
		this.totalSoldNum = totalSoldNum;
	}
	@Transient
	public double getTotoalSoldPrice() {
		return totoalSoldPrice;
	}
	public void setTotoalSoldPrice(double totoalSoldPrice) {
		this.totoalSoldPrice = totoalSoldPrice;
	}
	@Transient
	public String getGoodsTypeName() {
		return goodsTypeName;
	}
	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}
	@Transient
	public String getGoodsBrandName() {
		return goodsBrandName;
	}
	public void setGoodsBrandName(String goodsBrandName) {
		this.goodsBrandName = goodsBrandName;
	}
	public String getEffect() {
		return effect;
	}
	public void setEffect(String effect) {
		this.effect = effect;
	}
	public String getSuitableForSkin() {
		return suitableForSkin;
	}
	public void setSuitableForSkin(String suitableForSkin) {
		this.suitableForSkin = suitableForSkin;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}
	public int getGlanceTimes() {
		return glanceTimes;
	}
	public void setGlanceTimes(int glanceTimes) {
		this.glanceTimes = glanceTimes;
	}
	public double getSoldPrice() {
		return soldPrice;
	}
	public void setSoldPrice(double soldPrice) {
		this.soldPrice = soldPrice;
	}
	
	
	
	
}
