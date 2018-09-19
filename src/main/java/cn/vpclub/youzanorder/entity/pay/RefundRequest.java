package cn.vpclub.youzanorder.entity.pay;

import cn.vpclub.moses.utils.validator.BaseAbstractParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退款记录表
 * </p>
 *
 * @author chensheng
 * @since 2018-01-06
 */

@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@ApiModel("退款请求对象")
public class RefundRequest extends BaseAbstractParameter {
    private static final long serialVersionUID = 1L;

    /**
     * 子订单编号
     */
    @ApiModelProperty(value = "子订单编号")
    private String subOrderNo;
    /**
     * 退款结果通知url
     */
    @ApiModelProperty(value = "退款结果通知url")
    private String notifyUrl;
    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private Integer refundAmount;
    /**
     * 用户号码
     */
    @ApiModelProperty(value = "用户号码")
    private String telphone;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    /**
     * 清结算商户编码
     */
    @ApiModelProperty(value = "清结算商户编码")
    private String payStoreId;

    /**
     * 商户编码
     */
    @ApiModelProperty(value = "商户编码")
    private String storeId;
    /**
     * app_id
     */
    @ApiModelProperty(value = "app_id")
    private String appId;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;

    /**
     * 退款主键
     */
    @ApiModelProperty(value = "退款主键")
    private String refundId;

    private Long paymentId;

    private String nonceStr;

    private String devMode;

    private String sign;
}
