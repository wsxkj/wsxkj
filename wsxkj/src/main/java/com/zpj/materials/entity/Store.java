package com.zpj.materials.entity;

import com.zpj.common.UUIDGenerator;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/*
 * @ClassName: Store
 * @Description: 库存信息表
 * @author zpj
 * @date 2019/4/1 14:25
*/
@Entity
@Table(name = "jl_material_store_info")
public class Store implements java.io.Serializable {
	@ApiModelProperty(value = "主键",name="id", required = true)
	private String id=UUIDGenerator.generatePk("store");
	@ApiModelProperty(value = "用户表id",name="userId", required = false)
	private String userId;
	@ApiModelProperty(value = "商品id",name="goodsId", required = false)
	private String goodsId;
	@ApiModelProperty(value = "进货日期",name="inDate", required = false)
	private Date inDate;
	@ApiModelProperty(value = "进货数量",name="inNum", required = false)
	private double inNum;
	@ApiModelProperty(value = "库存数量",name="storeNum", required = false)
	private double storeNum;
	@ApiModelProperty(value = "进货价格",name="inPrice", required = false)
	private double inPrice;
	@ApiModelProperty(value = "出货价格",name="outPrice", required = false)
	private double outPrice;
	@ApiModelProperty(value = "保质日期",name="sureDate", required = false)
	private Date sureDate;

	@ApiModelProperty(value = "更新时间",name="updateTime", required = false)
	private Date updateTime;


	@Id
	@Column(name = "id",  nullable = false, length = 50)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}


	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	@Column(name = "inNum", precision=12 ,scale=2)
	public double getInNum() {
		return inNum;
	}

	public void setInNum(double inNum) {
		this.inNum = inNum;
	}
	@Column(name = "storeNum", precision=12 ,scale=2)
	public double getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(double storeNum) {
		this.storeNum = storeNum;
	}
	@Column(name = "inPrice", precision=12 ,scale=2)
	public double getInPrice() {
		return inPrice;
	}

	public void setInPrice(double inPrice) {
		this.inPrice = inPrice;
	}
	@Column(name = "outPrice", precision=12 ,scale=2)
	public double getOutPrice() {
		return outPrice;
	}

	public void setOutPrice(double outPrice) {
		this.outPrice = outPrice;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sureDate", length=7)
	public Date getSureDate() {
		return sureDate;
	}

	public void setSureDate(Date sureDate) {
		this.sureDate = sureDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime", length=7)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
}
