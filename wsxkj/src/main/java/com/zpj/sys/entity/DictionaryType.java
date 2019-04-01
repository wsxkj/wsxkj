package com.zpj.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.annotations.ApiModel;

@Entity
@Table(name = "sys_dictionary_type")
@ApiModel(value = "字段类型", description = "字段类型表")
public class DictionaryType implements java.io.Serializable{
	private Integer id;
	private String typeCode;//类型编码
	private String typeName;//类型名称
	private Integer parentTypeid=0;
	private int orderNum=1;
	
	private String parentTypeName;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name = "id",unique=true, nullable = false, precision = 22, scale = 0)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getParentTypeid() {
		return parentTypeid;
	}
	public void setParentTypeid(Integer parentTypeid) {
		this.parentTypeid = parentTypeid;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	
	@Transient
	public String getParentTypeName() {
		return parentTypeName;
	}
	public void setParentTypeName(String parentTypeName) {
		this.parentTypeName = parentTypeName;
	}
	
	
	
}
