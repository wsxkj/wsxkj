package com.zpj.materials.entity;

import com.zpj.common.UUIDGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/*
 * @ClassName: GoodsBrandPicture
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zpj
 * @date 2019/4/2 9:07
*/
@Entity
@Table(name = "jl_material_goods_brand_picture_info")
@ApiModel(value = "商品品牌图片表", description = "商品品牌图片信息表")
public class GoodsBrandPicture implements java.io.Serializable{
    @ApiModelProperty(value = "主键",name="id", required = true)
    private String id= UUIDGenerator.generatePk("gbp");
   
    @ApiModelProperty(value = "图片路径",name="pictureURL", required = false)
    private String pictureURL;

    @ApiModelProperty(value = "修改时间",name="updateTime", required = false)
    private Date updateTime;
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
