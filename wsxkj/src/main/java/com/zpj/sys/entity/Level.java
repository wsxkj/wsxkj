package com.zpj.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zpj.common.UUIDGenerator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "sys_level")
@ApiModel(value = "用户表", description = "用户信息表")
public class Level implements java.io.Serializable{
	@ApiModelProperty(value = "主键",name="id", required = true)
	private String id= UUIDGenerator.generatePk("level");
	@ApiModelProperty(value = "等级0,1,2,3,4",name="level", required = true)
	private int level;
	@ApiModelProperty(value = "使用时间，可使用天数",name="days", required = true)
	private int days;
	@ApiModelProperty(value = "可调用接口次数",name="maxtime", required = true)
	private int maxtime;
	@ApiModelProperty(value = "价格",name="money", required = true)
	private int money;
	
	@Id
	@Column(name = "id", unique = true, nullable = false ,length=36)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getMaxtime() {
		return maxtime;
	}
	public void setMaxtime(int maxtime) {
		this.maxtime = maxtime;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	
	
	
	
}
