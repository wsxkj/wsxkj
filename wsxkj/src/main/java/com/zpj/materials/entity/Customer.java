package com.zpj.materials.entity;

import com.zpj.common.UUIDGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

/*
 * @ClassName: Customer
 * @Description: TODO(客户表)
 * @author zpj
 * @date 2019/4/1 16:10
*/
@Entity
@Table(name = "jl_material_customer_info")
@ApiModel(value = "客户表", description = "客户信息表")
public class Customer implements java.io.Serializable{
    @ApiModelProperty(value = "主键",name="id", required = true)
    private String id= UUIDGenerator.generatePk("customer");
    @ApiModelProperty(value = "用户表id",name="userId", required = false)
	private String userId;
    @ApiModelProperty(value = "微信昵称",name="nickname", required = false)
    private String nickname;
    @ApiModelProperty(value = "微信号",name="wxh", required = false)
    private String wxh;
    @ApiModelProperty(value = "收货姓名",name="receiver", required = false)
    private String receiver;
    @ApiModelProperty(value = "收货地址",name="address", required = false)
    private String address;
    @ApiModelProperty(value = "手机号码",name="phone", required = false)
    private String phone;
    @ApiModelProperty(value = "更新时间",name="updateTime", required = false)
    private Date updateTime;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWxh() {
        return wxh;
    }

    public void setWxh(String wxh) {
        this.wxh = wxh;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updateTime", length=7)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    
	@Override
	public String toString() {
		return "id:"+this.getId()+
				",userId:"+this.getUserId()+
				",nickname:"+this.nickname+
				",wxh:"+this.wxh+
				",receiver:"+this.receiver+
				",address:"+this.address+
				",phone:"+this.phone;
	}
    
}
