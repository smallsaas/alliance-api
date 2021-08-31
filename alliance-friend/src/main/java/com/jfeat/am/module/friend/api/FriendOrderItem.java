package com.jfeat.am.module.friend.api;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Code Generator
 * @since 2019-07-31
 */
@TableName("t_order_item")
public class FriendOrderItem extends Model<FriendOrderItem> {

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
	@TableField("order_id")
	private Integer orderId;
	@TableField("product_id")
	private Integer productId;
	@TableField("product_name")
	private String productName;
	private Integer quantity;
	private BigDecimal price;
	@TableField("final_price")
	private BigDecimal finalPrice;
	private String status;
	@TableField("cost_price")
	private BigDecimal costPrice;
	private String cover;
	@TableField("partner_level_zone")
	private Integer partnerLevelZone;
	@TableField("product_specification_name")
	private String productSpecificationName;
	@TableField("product_specification_id")
	private Integer productSpecificationId;
	private Integer weight;
	private Integer bulk;
	private String barcode;
	@TableField("store_location")
	private String storeLocation;
	private String marketing;
	@TableField("marketing_id")
	private Integer marketingId;
	@TableField("marketing_description")
	private String marketingDescription;
    /**
     * SKU ID
     */
	@TableField("sku_id")
	private String skuId;
    /**
     * WAERHOUSE ID
     */
	@TableField("warehouse_id")
	private String warehouseId;


	public Long getId() {
		return id;
	}

	public FriendOrderItem setId(Long id) {
		this.id = id;
		return this;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public FriendOrderItem setOrderId(Integer orderId) {
		this.orderId = orderId;
		return this;
	}

	public Integer getProductId() {
		return productId;
	}

	public FriendOrderItem setProductId(Integer productId) {
		this.productId = productId;
		return this;
	}

	public String getProductName() {
		return productName;
	}

	public FriendOrderItem setProductName(String productName) {
		this.productName = productName;
		return this;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public FriendOrderItem setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public FriendOrderItem setPrice(BigDecimal price) {
		this.price = price;
		return this;
	}

	public BigDecimal getFinalPrice() {
		return finalPrice;
	}

	public FriendOrderItem setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public FriendOrderItem setStatus(String status) {
		this.status = status;
		return this;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public FriendOrderItem setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
		return this;
	}

	public String getCover() {
		return cover;
	}

	public FriendOrderItem setCover(String cover) {
		this.cover = cover;
		return this;
	}

	public Integer getPartnerLevelZone() {
		return partnerLevelZone;
	}

	public FriendOrderItem setPartnerLevelZone(Integer partnerLevelZone) {
		this.partnerLevelZone = partnerLevelZone;
		return this;
	}

	public String getProductSpecificationName() {
		return productSpecificationName;
	}

	public FriendOrderItem setProductSpecificationName(String productSpecificationName) {
		this.productSpecificationName = productSpecificationName;
		return this;
	}

	public Integer getProductSpecificationId() {
		return productSpecificationId;
	}

	public FriendOrderItem setProductSpecificationId(Integer productSpecificationId) {
		this.productSpecificationId = productSpecificationId;
		return this;
	}

	public Integer getWeight() {
		return weight;
	}

	public FriendOrderItem setWeight(Integer weight) {
		this.weight = weight;
		return this;
	}

	public Integer getBulk() {
		return bulk;
	}

	public FriendOrderItem setBulk(Integer bulk) {
		this.bulk = bulk;
		return this;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getStoreLocation() {
		return storeLocation;
	}

	public FriendOrderItem setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
		return this;
	}

	public String getMarketing() {
		return marketing;
	}

	public FriendOrderItem setMarketing(String marketing) {
		this.marketing = marketing;
		return this;
	}

	public Integer getMarketingId() {
		return marketingId;
	}

	public FriendOrderItem setMarketingId(Integer marketingId) {
		this.marketingId = marketingId;
		return this;
	}

	public String getMarketingDescription() {
		return marketingDescription;
	}

	public FriendOrderItem setMarketingDescription(String marketingDescription) {
		this.marketingDescription = marketingDescription;
		return this;
	}

	public String getSkuId() {
		return skuId;
	}

	public FriendOrderItem setSkuId(String skuId) {
		this.skuId = skuId;
		return this;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public FriendOrderItem setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
		return this;
	}

	public static final String ID = "id";

	public static final String ORDER_ID = "order_id";

	public static final String PRODUCT_ID = "product_id";

	public static final String PRODUCT_NAME = "product_name";

	public static final String QUANTITY = "quantity";

	public static final String PRICE = "price";

	public static final String FINAL_PRICE = "final_price";

	public static final String STATUS = "status";

	public static final String COST_PRICE = "cost_price";

	public static final String COVER = "cover";

	public static final String PARTNER_LEVEL_ZONE = "partner_level_zone";

	public static final String PRODUCT_SPECIFICATION_NAME = "product_specification_name";

	public static final String PRODUCT_SPECIFICATION_ID = "product_specification_id";

	public static final String WEIGHT = "weight";

	public static final String BULK = "bulk";

	public static final String BARCODE = "barCode";

	public static final String STORE_LOCATION = "store_location";

	public static final String MARKETING = "marketing";

	public static final String MARKETING_ID = "marketing_id";

	public static final String MARKETING_DESCRIPTION = "marketing_description";

	public static final String SKU_ID = "sku_id";

	public static final String WAREHOUSE_ID = "warehouse_id";

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "FriendOrderItem{" +
			"id=" + id +
			", orderId=" + orderId +
			", productId=" + productId +
			", productName=" + productName +
			", quantity=" + quantity +
			", price=" + price +
			", finalPrice=" + finalPrice +
			", status=" + status +
			", costPrice=" + costPrice +
			", cover=" + cover +
			", partnerLevelZone=" + partnerLevelZone +
			", productSpecificationName=" + productSpecificationName +
			", productSpecificationId=" + productSpecificationId +
			", weight=" + weight +
			", bulk=" + bulk +
			", barcode=" + barcode +
			", storeLocation=" + storeLocation +
			", marketing=" + marketing +
			", marketingId=" + marketingId +
			", marketingDescription=" + marketingDescription +
			", skuId=" + skuId +
			", warehouseId=" + warehouseId +
			"}";
	}
}
