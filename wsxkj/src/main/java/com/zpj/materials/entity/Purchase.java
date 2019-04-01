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
 * @Description: 采购登记表
 * @ClassName: Purchase
 * @author zpj 
 * @date 2016-4-9 下午3:27:59
 *
 */
@Entity
@Table(name = "jl_material_purchase_info")
public class Purchase implements java.io.Serializable {
	private String id;//主键  生成规则CGD+时间戳
	private int departmentid;//采购部门id
	private float totalprice;//合计采购金额
	private String chargename;//采购人
	private int loginid;//登记人id
	private String state;//采购状态（1采购中，2已审核入库，入库的话会存一条记录到入库表中,3未通过审核）
	private Date createtime;//创建时间采购时间
	
	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "departmentid",   precision = 22, scale = 0)
	public int getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(int departmentid) {
		this.departmentid = departmentid;
	}
	
	
	@Column(name = "totalprice", precision=12 ,scale=2)
	public float getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(float totalprice) {
		this.totalprice = totalprice;
	}
	@Column(name = "chargename", length = 100)
	public String getChargename() {
		return chargename;
	}
	public void setChargename(String chargename) {
		this.chargename = chargename;
	}
	@Column(name = "loginid",   precision = 22, scale = 0)
	public int getLoginid() {
		return loginid;
	}
	public void setLoginid(int loginid) {
		this.loginid = loginid;
	}

	@Column(name = "state", length = 100)
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
