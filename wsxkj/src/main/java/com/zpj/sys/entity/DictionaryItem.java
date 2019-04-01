package com.zpj.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@Entity
@Table(name = "sys_dictionary_item")
@ApiModel(value = "字段项目", description = "字段项目表")
public class DictionaryItem implements java.io.Serializable{
	
	
	private Integer id;//item主键
	private String itemCode;//编码
	private String itemName;//名称
	private String typeCode;//所属类型编码
	private Integer parentItemid=0;//父项id
	private Integer itemOrder;//排序
	private String remark;
	private Integer isEnable=1;//1可用，0禁用
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name = "id",unique=true, nullable = false, precision = 22, scale = 0)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public Integer getParentItemid() {
		return parentItemid;
	}
	public void setParentItemid(Integer parentItemid) {
		this.parentItemid = parentItemid;
	}
	public Integer getItemOrder() {
		return itemOrder;
	}
	public void setItemOrder(Integer itemOrder) {
		this.itemOrder = itemOrder;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}
	
	
	
}
