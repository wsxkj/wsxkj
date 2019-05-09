package com.zpj.materials.entity;

import com.zpj.common.UUIDGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/*
 * @ClassName: GoodsBrand
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zpj
 * @date 2019/4/1 16:21
*/
@Entity
@Table(name = "jl_material_goods_brand_info")
@ApiModel(value = "商品品牌表", description = "商品品牌信息表")
public class GoodsBrand implements java.io.Serializable{
    @ApiModelProperty(value = "主键",name="id", required = true)
    private String id= UUIDGenerator.generatePk("brand");
    @ApiModelProperty(value = "用户表id",name="userId", required = false)
	private String userId;
    @ApiModelProperty(value = "品牌名称",name="name", required = false)
    private String name;
    @ApiModelProperty(value = "所属分类id",name="typeId", required = false)
    private String typeId;
    @ApiModelProperty(value = "图片表id",name="pictureId", required = false)
    private String pictureId;
    @ApiModelProperty(value = "修改时间",name="updateTime", required = false)
    private Date updateTime;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   

    public String getPictureId() {
		return pictureId;
	}

	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}

	public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
    
    
}
