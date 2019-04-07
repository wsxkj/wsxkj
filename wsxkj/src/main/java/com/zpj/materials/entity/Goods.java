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
	@ApiModelProperty(value = "创建时间",name="createTime", required = false)
	private Date createTime=new Date();
	@ApiModelProperty(value = "修改时间",name="updateTime", required = false)
	private Date updateTime;
	@ApiModelProperty(value = "总库存数量",name="storeNum", required = false)
	private double storeNum;

	
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
	@Column(name = "createTime", length=7)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
}
