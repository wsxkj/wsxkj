package com.zpj.materials.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;


/**
 * @Description: 维修日常表
 * @ClassName: Maintain
 * @author zpj 
 * @date 2016-4-11 上午9:39:22
 *
 */
@Entity
@Table(name = "jl_material_maintain_info")
public class Maintain implements java.io.Serializable{
	@ApiModelProperty(value = "主键",name="id", required = true)
	private String id;//主键
	@ApiModelProperty(value = "登记时间",name="registerTime", required = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registertime;
	@ApiModelProperty(value = "备注",name="remark", required = false)
	private String remark;//备注
	@ApiModelProperty(value = "金额",name="jine", required = false)
	private Double jine;
	@ApiModelProperty(value = "是否付款，1已付款，0未付款",name="isPay", required = false)
	private String isPay;
	@ApiModelProperty(value = "修理的人的名称",name="username", required = false)
	private String username;
	
	private Date createtime;//创建时间
	
	@Id
	@Column(name = "id",  nullable = false, length=36)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createtime", length=7)
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	@Column(name = "registertime")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date getRegistertime() {
		return registertime;
	}
	public void setRegistertime(Date registertime) {
		this.registertime = registertime;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getJine() {
		return jine;
	}
	public void setJine(Double jine) {
		this.jine = jine;
	}
	public String getIsPay() {
		return isPay;
	}
	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "id:"+this.getId()+",username:"+this.getUsername()+",registertime:"+this.getRegistertime()+",remark:"+this.getRemark()+",jine:"+this.getJine()+",isPay:"+this.getIsPay()+",createtime:"+this.getCreatetime();
	}
}
