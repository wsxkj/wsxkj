package com.zpj.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "sys_log_info")
@ApiModel(value = "日志信息表", description = "日志信息表")
public class LogInfo implements java.io.Serializable{
	@ApiModelProperty(value = "主键",name="id", required = true)
	private String id;
	@ApiModelProperty(value = "时间",name="createtime", required =  false)
	private Date createtime; 
	@ApiModelProperty(value = "描述",name="description", required =  false)
	private String description;
	@ApiModelProperty(value = "类型",name="type", required =  false)
	private String type;
//	@ApiModelProperty(value = "用户id",name="userid", required =  false)
//	private String userid;
	@ApiModelProperty(value = "用户",name="username", required =  false)
	private String username;
	
	@Id
	@Column(name = "id", unique = true, nullable = false ,length=36)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Temporal(value=TemporalType.TIMESTAMP)
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
	
}
