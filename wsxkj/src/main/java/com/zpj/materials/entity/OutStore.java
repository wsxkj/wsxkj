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

/**
 * @Description: 出库（领用）记录表
 * @ClassName: OutStore
 * @author zpj 
 * @date 2016-4-11 上午9:37:33
 *
 */
@Entity
@Table(name = "jl_material_outstore_info")
public class OutStore implements java.io.Serializable {
	private int id;//主键
	private int goodsid;//物资id
	private int supplierid;//供应商id
	private int departmentid;//部门id
	private String chargename;//领用人姓名
	private double num=0;//出库数量
	private double price=0;//出库（领用）金额
	private Date createtime;//出库时间
	
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
	@Column(name = "chargename", length = 100)
	public String getChargename() {
		return chargename;
	}
	public void setChargename(String chargename) {
		this.chargename = chargename;
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
	
	
}
