package com.zpj.materials.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "jl_material_goods_info")
@ApiModel(value = "商品表", description = "商品信息表")
public class Goods implements java.io.Serializable{
	
	@ApiModelProperty(value = "主键",name="id", required = true)
	private String id;
	@ApiModelProperty(value = "商品名称",name="name", required = false)
	private String name;
	@ApiModelProperty(value = "规格型号",name="type", required = false)
	private String type;//规格型号
	@ApiModelProperty(value = "单位",name="unit", required = false)
	private String unit;//
	@ApiModelProperty(value = "进价",name="purchaseprice", required = false)
	private double purchasePrice=0;
	@ApiModelProperty(value = "卖价",name="sellingPrice", required = false)
	private double sellingPrice=0;
	@ApiModelProperty(value = "数量",name="num", required = false)
	private double num=0;//数量
	@ApiModelProperty(value = "供应商id",name="supplierId", required = false)
	private String supplierId;//供应商id
	@ApiModelProperty(value = "供应商名称",name="supplierName", required = false)
	private String supplierName;//供应商名称
	@ApiModelProperty(value = "商品类别",name="goodsType", required = false)
	private String goodsType;
	private String goodsTypeName;
	private Date createtime=new Date();//创建时间
	@ApiModelProperty(value = "备注",name="remark", required = false)
	private String remark;
	
	@ApiModelProperty(value = "附件路径已逗号分割",name="fujian", required = false)
	private String fujian;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public double getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public double getNum() {
		return num;
	}
	public void setNum(double num) {
		this.num = num;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getFujian() {
		return fujian;
	}
	public void setFujian(String fujian) {
		this.fujian = fujian;
	}
	@Transient
	public String getGoodsTypeName() {
		return goodsTypeName;
	}
	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}

	@Override
	public String toString() {
		return "id:"+this.getId()+
				",name:"+this.getName()
				+",type:"+this.getType()
				+",unit:"+this.getUnit()
				+",purchasePrice:"+this.getPurchasePrice()+
				",sellingPrice:"+this.getSellingPrice()+
				",supplierName:"+this.getSupplierName()+
				",goodsType:"+this.getGoodsType()+
				",remark:"+this.getRemark();
	}
}
