package com.zpj.materials.entity;

import com.zpj.common.UUIDGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

/*
 * @ClassName: OrderGoodsInfo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zpj
 * @date 2019/4/1 15:29
*/
@Entity
@Table(name = "jl_material_order_goods_info")
@ApiModel(value = "订单商品表", description = "订单商品信息表")
public class OrderGoodsInfo implements java.io.Serializable{
    @ApiModelProperty(value = "主键",name="id", required = true)
    private String id= UUIDGenerator.generatePk("OG");
    @ApiModelProperty(value = "order表id",name="orderId", required = false)
    private String orderId;
    @ApiModelProperty(value = "库存表id,若库存表id为空则说明没有库存",name="storeId", required = false)
    private String storeId;
    @ApiModelProperty(value = "商品id",name="goodsId", required = false)
    private String goodsId;
    @ApiModelProperty(value = "出售数量",name="soldNum", required = false)
    private double soldNum;
    @ApiModelProperty(value = "出售单价",name="soldPrice", required = false)
    private double soldPrice;
    @ApiModelProperty(value = "出售总价",name="soldTotalPrice", required = false)
    private double soldTotalPrice;
    @ApiModelProperty(value = "更新时间",name="updateTime", required = false)
    private Date updateTime;
    @ApiModelProperty(value = "已付多少",name="paidMoney", required = false)
    private double paidMoney;
    @ApiModelProperty(value = "未付多少",name="unpaidMoney", required = false)
    private double unpaidMoney;
    
    
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    @Column(name = "soldNum", precision=12 ,scale=2)
    public double getSoldNum() {
        return soldNum;
    }

    public void setSoldNum(double soldNum) {
        this.soldNum = soldNum;
    }
    @Column(name = "soldPrice", precision=12 ,scale=2)
    public double getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(double soldPrice) {
        this.soldPrice = soldPrice;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updateTime", length=7)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public double getPaidMoney() {
		return paidMoney;
	}

	public void setPaidMoney(double paidMoney) {
		this.paidMoney = paidMoney;
	}

	public double getUnpaidMoney() {
		return unpaidMoney;
	}

	public void setUnpaidMoney(double unpaidMoney) {
		this.unpaidMoney = unpaidMoney;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public double getSoldTotalPrice() {
		return soldTotalPrice;
	}

	public void setSoldTotalPrice(double soldTotalPrice) {
		this.soldTotalPrice = soldTotalPrice;
	}
    
	@Override
    public String toString() {
    	StringBuffer sb=new StringBuffer(100);
    	sb.append("id:"+this.id).append(",orderId:"+this.orderId+",goodsId:"+this.goodsId+",storeId:"+this.getStoreId()+
    	",soldNum:"+this.getSoldNum()+",soldPrice:"+this.getSoldPrice()+",soldTotalPrice:"+this.getSoldTotalPrice()+
    	",paidMoney:"+this.getPaidMoney()+",unpaidMoney:"+this.getUnpaidMoney());
    	return sb.toString();
    }
}
