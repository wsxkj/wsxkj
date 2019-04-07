package com.zpj.materials.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "jl_material_idcode_info")
@ApiModel(value = "验证码表", description = "验证码信息表")
public class IdCodeInfo implements java.io.Serializable{
	@ApiModelProperty(value = "主键",name="id", required = true)
	private int id;
	
	@ApiModelProperty(value = "手机号码",name="phone", required = false)
	private String phone;
	@ApiModelProperty(value = "验证码",name="yzm", required = false)
	private String yzm;
	@ApiModelProperty(value = "验证码时间",name="yzmTime", required = false)
	private Date yzmTime;
	@ApiModelProperty(value = "验证码类型",name="type", required = false)
	private String type;
	@ApiModelProperty(value = "更新时间",name="updateTime", required = false)
	private Date updateTime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	@Column(name = "id",  nullable = false, precision = 22, scale = 0)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getYzm() {
		return yzm;
	}
	public void setYzm(String yzm) {
		this.yzm = yzm;
	}
	public Date getYzmTime() {
		return yzmTime;
	}
	public void setYzmTime(Date yzmTime) {
		this.yzmTime = yzmTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
