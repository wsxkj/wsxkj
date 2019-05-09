package com.zpj.materials.entity;

import com.zpj.common.UUIDGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/*
 * @ClassName: GoodsType
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zpj
 * @date 2019/4/1 16:18
*/
@Entity
@Table(name = "jl_material_goods_type_info")
@ApiModel(value = "商品分类表", description = "商品分类信息表")
public class GoodsType implements java.io.Serializable{
    @ApiModelProperty(value = "主键",name="id", required = true)
    private String id= UUIDGenerator.generatePk("type");
    @ApiModelProperty(value = "用户表id",name="userId", required = false)
	private String userId;
    @ApiModelProperty(value = "分类名称",name="name", required = false)
    private String name;
    
    @ApiModelProperty(value = "修改时间",name="updateTime", required = false)
    private Date updateTime;
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    
    @Override
    public String toString() {
    	StringBuffer sb=new StringBuffer(100);
    	sb.append("id:"+this.id).append(",userId:"+this.userId+",name:"+this.name);
    	return sb.toString();
    }
}
