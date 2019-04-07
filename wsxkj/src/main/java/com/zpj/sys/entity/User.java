package com.zpj.sys.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.zpj.common.UUIDGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * User generated by hbm2java
 */
@Entity
@Table(name = "sys_userinfo")
@ApiModel(value = "用户表", description = "用户信息表")
public class User implements java.io.Serializable {
	
	@ApiModelProperty(value = "主键",name="id", required = true)
	private String id= UUIDGenerator.generatePk("user");
	@ApiModelProperty(value = "手机号码",name="phone", required =  false)
	private String phone;
	@ApiModelProperty(value = "微信openid",name="name", required =  false)
	private String wxid;
	@ApiModelProperty(value = "微信昵称",name="wxnickname", required =  false)
	private String wxnickname;
	@ApiModelProperty(value = "最后一次登陆时间",name="lastLoginTime", required =  false)
	private Date lastLoginTime;
	@ApiModelProperty(value = "更新时间",name="updateTime", required =  false)
	private Date updateTime;
	
	

	@Id
	@Column(name = "id", unique = true, nullable = false ,length=36)
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
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}
	public String getWxnickname() {
		return wxnickname;
	}
	public void setWxnickname(String wxnickname) {
		this.wxnickname = wxnickname;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	
	

}
