package com.zpj.materials.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.swagger.annotations.ApiModel;

/**
 * @Description: 入库表
 * @ClassName: InStore
 * @author zpj 
 * @date 2016-4-11 上午9:24:49
 *
 */
@Entity
@Table(name = "jl_material_instore_info")
public class InStore implements java.io.Serializable{
	private int id;//主键
	private int goodsid;//物资id
	private int supplierid;//供货商id
	private int departmentid;//入库部门id
	private double num=0;//入库数量
	private double price;//入库金额
	private Date createtime;//入库时间
	private String purchaseid;//采购单id
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	@Column(name = "id",  nullable = false, precision = 22, scale = 0)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "goodsid", precision = 22, scale = 0)
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	@Column(name = "supplierid", precision = 22, scale = 0)
	public int getSupplierid() {
		return supplierid;
	}
	public void setSupplierid(int supplierid) {
		this.supplierid = supplierid;
	}
	@Column(name = "departmentid", precision = 22, scale = 0)
	public int getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(int departmentid) {
		this.departmentid = departmentid;
	}
	
	@Column(name = "num", precision=12 ,scale=2)
	public double getNum() {
		return num;
	}
	public void setNum(double num) {
		this.num = num;
	}
	
	@Column(name = "price", precision=12 ,scale=2)
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createtime", length=7)
	public Date getCreatetime() {
		return createtime;
	}
	
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Column(name = "purchaseid", length = 50)
	public String getPurchaseid() {
		return purchaseid;
	}
	public void setPurchaseid(String purchaseid) {
		this.purchaseid = purchaseid;
	}
	
}
