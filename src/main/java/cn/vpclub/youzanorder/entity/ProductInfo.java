package cn.vpclub.youzanorder.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品信息表
 * Created by chentao on 2018/4/27.
 */
@TableName("product_info")
@Data
public class ProductInfo extends Model<SubOrder> {
    /**
     * 主键id
     */
    private Long id;

    @TableField("sub_order_id")
    private Long subOrderId;


    @TableField("is_virtual")
    private Integer isVirtual;

    /**
     * 商品编码, 用于标记商户id
     */
    @TableField("outer_item_id")
    private String outerItemId;

    @TableField("pic_thumb_path")
    private String picThumbPath;

    @TableField("item_type")
    private Integer itemType;

    @TableField("num")
    private String num;

    @TableField("refunded_fee")
    private BigDecimal refundedFee;

    @TableField("num_iid")
    private String numIid;

    private String oid;

    private String title;

    @TableField("fenxiao_payment")
    private String fenxiaoPayment;

    @TableField("discount_fee")
    private String discountFee;

    @TableField("buyer_messages")
    private String buyerMessages;

    @TableField("is_present")
    private Integer isPresent;


    private String price;

    @TableField("fenxiao_price")
    private String fenxiaoPrice;

    @TableField("total_fee")
    private String totalFee;

    private String alias;

    private String payment;

    @TableField("outer_sku_id")
    private String outerSkuId;

    @TableField("sku_unique_code")
    private String skuUniqueCode;

    @TableField("is_send")
    private Integer isSend;

    @TableField("item_id")
    private String itemId;

    @TableField("sku_id")
    private String skuId;

    @TableField("sku_properties_name")
    private String skuPropertiesName;

    @TableField("pic_path")
    private String picPath;

    @TableField("item_refund_state")
    private String itemRefundState;

    private String unit;

    @TableField("order_promotion_details")
    private String orderPromotionDetails;

    @TableField("allow_send")
    private Integer allowSend;

    @TableField("seller_nick")
    private String sellerNick;


    /**
     * 订单状态
     */
    @TableField("state_str")
    private String stateStr;

    /**
     * 物流状态
     */
    @TableField("deliver_status")
    private String deliverStatus;

    /**
     * 物流单号
     */
    @TableField("logistics_number")
    private String logisticsNumber;

    /**
     * 物流公司名称
     */
    @TableField("logistics_company_name")
    private String logisticsCompanyName;
    /**
     * 物流公司id
     */
    @TableField("logistics_company_id")
    private String logisticsCompanyId;


    /**
     * 备注可以是json字符串
     */
    private String remarks;

    /**
     * 创建人
     */
    @TableField("created_by")
    private Long createdBy;
    /**
     * 创建时间
     */
    @TableField("created_time")
    private Long createdTime;
    /**
     * 更新人
     */
    @TableField("updated_by")
    private Long updatedBy;
    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Long updatedTime;
    /**
     * 逻辑删除
     */
    private Integer deleted;

    @TableField("refund_status")
    private Byte refundStatus;//退款状态

    @TableField(exist = false)
    private RefundInfo refunDetail;//退款详情


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
