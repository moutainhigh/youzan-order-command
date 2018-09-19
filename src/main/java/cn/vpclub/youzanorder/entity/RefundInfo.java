package cn.vpclub.youzanorder.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 记录每个商品的退款申请
 * </p>
 *
 * @author chenwei
 * @since 2018-05-23
 */
@TableName("refund_info")
public class RefundInfo extends Model<RefundInfo> {

    private static final long serialVersionUID = 1L;

	private Long id;
    /**
     * 对应拆单的订单id
     */
	@TableField("sub_order_id")
	private Long subOrderId;
	@TableField("product_id")
	private Long productId;
    /**
     * 有赞订单id
     */
	private String tid;
    /**
     * 有赞订单下的商品id
     */
	private Long oid;
    /**
     * 退款id
     */
	@TableField("refund_id")
	private String refundId;
    /**
     * SAFE_NEW（买家发起）；SAFE_HANDLED（被拒绝后再次发起）；SAFE_REJECT（卖家拒绝）；SAFE_INVOLVED（客服介入）；SAFE_ACCEPT（卖家接受）；SAFE_SEND（买家发货）；SAFE_NO_RECEIVE（卖家没有收到货）；SAFE_CLOSE（维权关闭）；SAFE_FINISHED（维权结束）
     */
	@TableField("refund_state")
	private String refundState;
    /**
     * 上一次的状态
     */
	@TableField("last_refund_state")
	private String lastRefundState;
	@TableField("create_time")
	private Long createTime;
	@TableField("update_time")
	private Long updateTime;
    /**
     * 退款金额
     */
	@TableField("refunded_fee")
	private BigDecimal refundedFee;
    /**
     * 1-仅退款 2-退货退款
     */
	@TableField("refund_type")
	private Integer refundType;
    /**
     * 退货原因
     */
	@TableField("refund_reason")
	private String refundReason;
    /**
     * 订单支付类型
     */
	@TableField("pay_type")
	private String payType;
    /**
     * 退款消息记录
     */
	@TableField("refund_msg")
	private String refundMsg;
    /**
     * 退货物流单号
     */
	@TableField("refun_log_num")
	private String refunLogNum;
    /**
     * 退货物流公司
     */
	@TableField("refun_log_org")
	private String refunLogOrg;
    /**
     * 退货的收货人电话
     */
	@TableField("refun_phone")
	private String refunPhone;
    /**
     * 退货的收货人地址
     */
	@TableField("refun_name")
	private String refunName;
    /**
     * 退货地址
     */
	@TableField("refun_address")
	private String refunAddress;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSubOrderId() {
		return subOrderId;
	}

	public void setSubOrderId(Long subOrderId) {
		this.subOrderId = subOrderId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getRefundState() {
		return refundState;
	}

	public void setRefundState(String refundState) {
		this.refundState = refundState;
	}

	public String getLastRefundState() {
		return lastRefundState;
	}

	public void setLastRefundState(String lastRefundState) {
		this.lastRefundState = lastRefundState;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public BigDecimal getRefundedFee() {
		return refundedFee;
	}

	public void setRefundedFee(BigDecimal refundedFee) {
		this.refundedFee = refundedFee;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getRefundMsg() {
		return refundMsg;
	}

	public void setRefundMsg(String refundMsg) {
		this.refundMsg = refundMsg;
	}

	public String getRefunLogNum() {
		return refunLogNum;
	}

	public void setRefunLogNum(String refunLogNum) {
		this.refunLogNum = refunLogNum;
	}

	public String getRefunLogOrg() {
		return refunLogOrg;
	}

	public void setRefunLogOrg(String refunLogOrg) {
		this.refunLogOrg = refunLogOrg;
	}

	public String getRefunPhone() {
		return refunPhone;
	}

	public void setRefunPhone(String refunPhone) {
		this.refunPhone = refunPhone;
	}

	public String getRefunName() {
		return refunName;
	}

	public void setRefunName(String refunName) {
		this.refunName = refunName;
	}

	public String getRefunAddress() {
		return refunAddress;
	}

	public void setRefunAddress(String refunAddress) {
		this.refunAddress = refunAddress;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
