package com.jfeat.am.module.friend.api;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Code Generator
 * @since 2019-07-25
 */
@TableName("t_product")
public class AllianceProduct extends Model<AllianceProduct> {

    @TableField(exist = false)
    private com.alibaba.fastjson.JSONObject extra;

    public com.alibaba.fastjson.JSONObject getExtra() {
        return extra;
    }
    public void setExtra(com.alibaba.fastjson.JSONObject extra) {
        this.extra = extra;
    }


    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	@TableField("category_id")
	private Integer categoryId;
	@TableField("brand_id")
	private Integer brandId;
	private String name;
	@TableField("short_name")
	private String shortName;
	private String cover;
	@TableField("stock_balance")
	private Integer stockBalance;
	private Integer sales;
	private String status;
	@TableField("created_date")
	private Date createdDate;
	@TableField("last_modified_date")
	private Date lastModifiedDate;
	private String unit;
	private BigDecimal price;
	@TableField("cost_price")
	private BigDecimal costPrice;
	@TableField("suggested_price")
	private BigDecimal suggestedPrice;
	private Integer promoted;
	private BigDecimal freight;
	@TableField("free_shipping")
	private Integer freeShipping;
	@TableField("sort_order")
	private Integer sortOrder;
	@TableField("partner_level_zone")
	private Integer partnerLevelZone;
	@TableField("view_count")
	private Long viewCount;
	@TableField("fare_id")
	private Integer fareId;

	@TableField("store_location")
	private String storeLocation;
	private Integer weight;
	private Integer bulk;
	@TableField("sku_id")
	private String skuId;
	@TableField("sku_name")
	private String skuName;
	@TableField("sku_code")
	private String skuCode;

	@TableField("banner")
	private String banner;


	@TableField("barcode")
	private String barcode;

	/*private String barCode;*/
    /**
     * 商家id
     */
	private Integer mid;
    /**
     * 优惠活动-优惠券
     */
	@TableField("allow_coupon")
	private Integer allowCoupon;
    /**
     * 优惠活动-可用积分
     */
	private Integer credit;
    /**
     * 是否虚拟产品
     */
	@TableField("is_virtual")
	private Integer isVirtual;
    /**
     * 是否需要做了检测才可以购买
     */
	@TableField("required_participate_exam")
	private Integer requiredParticipateExam;

	@TableField("distribution_price")
	private BigDecimal distributionPrice;


	@TableField("org_id")
	private Long orgId;


	private Long presale;

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Long getId() {
		return id;
	}

	public AllianceProduct setId(Long id) {
		this.id = id;
		return this;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public AllianceProduct setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
		return this;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public AllianceProduct setBrandId(Integer brandId) {
		this.brandId = brandId;
		return this;
	}

	public String getName() {
		return name;
	}

	public AllianceProduct setName(String name) {
		this.name = name;
		return this;
	}

	public String getShortName() {
		return shortName;
	}

	public AllianceProduct setShortName(String shortName) {
		this.shortName = shortName;
		return this;
	}

	public String getCover() {
		return cover;
	}

	public AllianceProduct setCover(String cover) {
		this.cover = cover;
		return this;
	}

	public Integer getStockBalance() {
		return stockBalance;
	}

	public AllianceProduct setStockBalance(Integer stockBalance) {
		this.stockBalance = stockBalance;
		return this;
	}

	public Integer getSales() {
		return sales;
	}

	public AllianceProduct setSales(Integer sales) {
		this.sales = sales;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public AllianceProduct setStatus(String status) {
		this.status = status;
		return this;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public AllianceProduct setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public AllianceProduct setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
		return this;
	}

	public String getUnit() {
		return unit;
	}

	public AllianceProduct setUnit(String unit) {
		this.unit = unit;
		return this;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public AllianceProduct setPrice(BigDecimal price) {
		this.price = price;
		return this;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public AllianceProduct setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
		return this;
	}

	public BigDecimal getSuggestedPrice() {
		return suggestedPrice;
	}

	public AllianceProduct setSuggestedPrice(BigDecimal suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
		return this;
	}

	public Integer getPromoted() {
		return promoted;
	}

	public AllianceProduct setPromoted(Integer promoted) {
		this.promoted = promoted;
		return this;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public AllianceProduct setFreight(BigDecimal freight) {
		this.freight = freight;
		return this;
	}

	public Integer getFreeShipping() {
		return freeShipping;
	}

	public AllianceProduct setFreeShipping(Integer freeShipping) {
		this.freeShipping = freeShipping;
		return this;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getPartnerLevelZone() {
		return partnerLevelZone;
	}

	public AllianceProduct setPartnerLevelZone(Integer partnerLevelZone) {
		this.partnerLevelZone = partnerLevelZone;
		return this;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public AllianceProduct setViewCount(Long viewCount) {
		this.viewCount = viewCount;
		return this;
	}

	public Integer getFareId() {
		return fareId;
	}

	public AllianceProduct setFareId(Integer fareId) {
		this.fareId = fareId;
		return this;
	}



	public String getStoreLocation() {
		return storeLocation;
	}

	public AllianceProduct setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
		return this;
	}

	public Integer getWeight() {
		return weight;
	}

	public AllianceProduct setWeight(Integer weight) {
		this.weight = weight;
		return this;
	}

	public Integer getBulk() {
		return bulk;
	}

	public AllianceProduct setBulk(Integer bulk) {
		this.bulk = bulk;
		return this;
	}

	public String getSkuId() {
		return skuId;
	}

	public AllianceProduct setSkuId(String skuId) {
		this.skuId = skuId;
		return this;
	}

	public String getSkuName() {
		return skuName;
	}

	public AllianceProduct setSkuName(String skuName) {
		this.skuName = skuName;
		return this;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public AllianceProduct setSkuCode(String skuCode) {
		this.skuCode = skuCode;
		return this;
	}

/*	public String getBarCode() {
		return barCode;
	}

	public AllianceProduct setBarCode(String barCode) {
		this.barCode = barCode;
		return this;
	}*/

	public Integer getMid() {
		return mid;
	}

	public AllianceProduct setMid(Integer mid) {
		this.mid = mid;
		return this;
	}

	public Integer getAllowCoupon() {
		return allowCoupon;
	}

	public AllianceProduct setAllowCoupon(Integer allowCoupon) {
		this.allowCoupon = allowCoupon;
		return this;
	}

	public Integer getCredit() {
		return credit;
	}

	public AllianceProduct setCredit(Integer credit) {
		this.credit = credit;
		return this;
	}

	public Integer getIsVirtual() {
		return isVirtual;
	}

	public AllianceProduct setIsVirtual(Integer isVirtual) {
		this.isVirtual = isVirtual;
		return this;
	}

	public Integer getRequiredParticipateExam() {
		return requiredParticipateExam;
	}

	public AllianceProduct setRequiredParticipateExam(Integer requiredParticipateExam) {
		this.requiredParticipateExam = requiredParticipateExam;
		return this;
	}

	public static final String ID = "id";

	public static final String CATEGORY_ID = "category_id";

	public static final String BRAND_ID = "brand_id";

	public static final String NAME = "name";

	public static final String SHORT_NAME = "short_name";

	public static final String COVER = "cover";

	public static final String STOCK_BALANCE = "stock_balance";

	public static final String SALES = "sales";

	public static final String STATUS = "status";

	public static final String CREATED_DATE = "created_date";

	public static final String LAST_MODIFIED_DATE = "last_modified_date";

	public static final String UNIT = "unit";

	public static final String PRICE = "price";

	public static final String COST_PRICE = "cost_price";

	public static final String SUGGESTED_PRICE = "suggested_price";

	public static final String PROMOTED = "promoted";

	public static final String FREIGHT = "freight";

	public static final String FREE_SHIPPING = "free_shipping";

	public static final String SORT_ORDER = "sort_order";

	public static final String PARTNER_LEVEL_ZONE = "partner_level_zone";

	public static final String VIEW_COUNT = "view_count";

	public static final String FARE_ID = "fare_id";

	public static final String BARCODE = "barcode";

	public static final String STORE_LOCATION = "store_location";

	public static final String WEIGHT = "weight";

	public static final String BULK = "bulk";

	public static final String SKU_ID = "sku_id";

	public static final String SKU_NAME = "sku_name";

	public static final String SKU_CODE = "sku_code";

	public static final String BAR_CODE = "bar_code";

	public static final String MID = "mid";

	public static final String ALLOW_COUPON = "allow_coupon";

	public static final String CREDIT = "credit";

	public static final String IS_VIRTUAL = "is_virtual";

	public static final String REQUIRED_PARTICIPATE_EXAM = "required_participate_exam";

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "AllianceProduct{" +
			"id=" + id +
			", categoryId=" + categoryId +
			", brandId=" + brandId +
			", name=" + name +
			", shortName=" + shortName +
			", cover=" + cover +
			", stockBalance=" + stockBalance +
			", sales=" + sales +
			", status=" + status +
			", createdDate=" + createdDate +
			", lastModifiedDate=" + lastModifiedDate +
			", unit=" + unit +
			", price=" + price +
			", costPrice=" + costPrice +
			", suggestedPrice=" + suggestedPrice +
			", promoted=" + promoted +
			", freight=" + freight +
			", freeShipping=" + freeShipping +
			", sortOrder=" + sortOrder +
			", partnerLevelZone=" + partnerLevelZone +
			", viewCount=" + viewCount +
			", fareId=" + fareId +
				", barcode=" + barcode +
			", storeLocation=" + storeLocation +
			", weight=" + weight +
			", bulk=" + bulk +
			", skuId=" + skuId +
			", skuName=" + skuName +
			", skuCode=" + skuCode +
/*			", barCode=" + barCode +*/
			", mid=" + mid +
			", allowCoupon=" + allowCoupon +
			", credit=" + credit +
			", isVirtual=" + isVirtual +
			", requiredParticipateExam=" + requiredParticipateExam +
			"}";
	}

	public BigDecimal getDistributionPrice() {
		return distributionPrice;
	}

	public void setDistributionPrice(BigDecimal distributionPrice) {
		this.distributionPrice = distributionPrice;
	}

	public Long getPresale() {
		return presale;
	}

	public void setPresale(Long presale) {
		this.presale = presale;
	}
}
