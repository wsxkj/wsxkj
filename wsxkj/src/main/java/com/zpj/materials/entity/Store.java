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
 * @Description: 库存表
 * @ClassName: Store
 * @author zpj 
 * @date 2016-4-11 上午9:25:30
 *
 */
@Entity
@Table(name = "jl_material_store_info")
public class Store implements java.io.Serializable {
	private int id;//主键
	private int goodsid;//物资id
	private int supplierid;//供应商id
	private double num=0;//库存总数量
	private double price=0;//库存总金额
	private Date updatetime;//更新时间
	
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
	@Column(name = "updatetime", length=7)
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	
}
