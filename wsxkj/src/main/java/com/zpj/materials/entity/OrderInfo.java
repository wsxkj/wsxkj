package com.zpj.materials.entity;

import com.zpj.common.UUIDGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/*
 * @ClassName: OrderInfo
 * @Description: TODO(订单表)
 * @author zpj
 * @date 2019/4/1 15:20
*/
@Entity
@Table(name = "jl_material_order_info")
@ApiModel(value = "订单表", description = "订单信息表")
public class OrderInfo implements java.io.Serializable{
    @ApiModelProperty(value = "主键",name="id", required = true)
    private String id= UUIDGenerator.generatePk("order");
    @ApiModelProperty(value = "用户表id",name="userId", required = false)
	private String userId;
    @ApiModelProperty(value = "客户id",name="customerId", required = false)
    private String customerId;
    @ApiModelProperty(value = "邮费",name="postage", required = false)
    private double postage;
    @ApiModelProperty(value = "订单状态0,1,2,3。0表示已发货，1表示未发货现货，2表示未发货缺货",name="state", required = false)
    private String state;
    @ApiModelProperty(value = "更新时间",name="updateTime", required = false)
    private Date updateTime;
    @ApiModelProperty(value = "物流单号",name="trackingNo", required = false)
    private String trackingNo;
    
    private List<OrderGoodsInfo>  goodsList;//订单下面挂的商品list
    
    @Id
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


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    @Column(name = "postage", precision=12 ,scale=2)
    public double getPostage() {
        return postage;
    }

    public void setPostage(double postage) {
        this.postage = postage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updateTime", length=7)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	@Transient
	public List<OrderGoodsInfo> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<OrderGoodsInfo> goodsList) {
		this.goodsList = goodsList;
	}
    
	
	@Override
    public String toString() {
    	StringBuffer sb=new StringBuffer(100);
    	sb.append("id:"+this.id).append(",userId:"+this.userId+",customerId:"+this.getCustomerId()+
    			",postage:"+this.getPostage()+",state:"+this.getState()+",trackNo:"+this.getTrackingNo());
    	return sb.toString();
    }
}
