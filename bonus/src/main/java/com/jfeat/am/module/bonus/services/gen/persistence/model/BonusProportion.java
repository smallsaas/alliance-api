package com.jfeat.am.module.bonus.services.gen.persistence.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("t_bonus_proportion")
public class BonusProportion
        extends Model<BonusProportion> {

    @TableField(exist = false)
    private com.alibaba.fastjson.JSONObject extra;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("product_id")
    private Long productId;
    @TableField("bonus_proportion")
    private BigDecimal bonusProportion;
    private String type;
    private String name;

    public JSONObject getExtra() {
        return this.extra;
    }

    private Integer level;
    public static final String ID = "id";
    public static final String PRODUCT_ID = "product_id";
    public static final String BONUS_PROPORTION = "bonus_proportion";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String LEVEL = "level";

    public void setExtra(JSONObject extra) {
        this.extra = extra;
    }


    /*  66 */
    public Long getId() {
        return this.id;
    }


    public BonusProportion setId(Long id) {
        /*  70 */
        this.id = id;
        /*  71 */
        return this;
    }


    /*  75 */
    public Long getProductId() {
        return this.productId;
    }


    public BonusProportion setProductId(Long productId) {
        /*  79 */
        this.productId = productId;
        /*  80 */
        return this;
    }


    /*  84 */
    public BigDecimal getBonusProportion() {
        return this.bonusProportion;
    }


    public BonusProportion setBonusProportion(BigDecimal bonusProportion) {
        /*  88 */
        this.bonusProportion = bonusProportion;
        /*  89 */
        return this;
    }


    /*  93 */
    public String getType() {
        return this.type;
    }


    public BonusProportion setType(String type) {
        /*  97 */
        this.type = type;
        /*  98 */
        return this;
    }


    /* 102 */
    public String getName() {
        return this.name;
    }


    public BonusProportion setName(String name) {
        /* 106 */
        this.name = name;
        /* 107 */
        return this;
    }


    /* 111 */
    public Integer getLevel() {
        return this.level;
    }


    public BonusProportion setLevel(Integer level) {
        /* 115 */
        this.level = level;
        /* 116 */
        return this;
    }


    /* 133 */
    protected Serializable pkVal() {
        return this.id;
    }


    /* 138 */
    public String toString() {
        return "BonusProportion{id=" + this.id + ", productId=" + this.productId + ", bonusProportion=" + this.bonusProportion + ", type=" + this.type + ", name=" + this.name + ", level=" + this.level + "}";
    }
}


/* Location:              C:\Users\39250\Desktop\新建文件夹\alliance-api-1.0.0-standalone.jar!\BOOT-INF\lib\bonus-1.0.0.jar!\com\jfeat\am\module\bonus\services\gen\persistence\model\BonusProportion.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.0.7
 */
