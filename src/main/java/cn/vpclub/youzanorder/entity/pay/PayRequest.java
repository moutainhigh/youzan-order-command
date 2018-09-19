package cn.vpclub.youzanorder.entity.pay;


import cn.vpclub.moses.utils.validator.BaseAbstractParameter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PayRequest extends BaseAbstractParameter {

    @ApiModelProperty(value = "订单id")
    private String orderNo;

    @ApiModelProperty(value = "订单描述信息")
    private String orderDesc;

    @ApiModelProperty(value = "订单金额")
    private Integer totalAmount;

    @ApiModelProperty(value = "子订单")
    private List<SubOrderDto> subList;

    @ApiModelProperty(value = "用户号码")
    private String telphone;

    @ApiModelProperty(value = "用户类型,1:手机号;2:openId")
    private String userType;

    @ApiModelProperty(value = "用户Id")
    private String userId;

    //机构id
    private String orgId;
//    @ApiModelProperty(value = "订单状态修改回调URL")
//    private String backNotifyUrl;
//
//    @ApiModelProperty(value = "支付成页面URL，透传至清结算")
//    private String frontUrl;

    @ApiModelProperty(value = "appId")
    private String appId;

//    //默认值 1.0
//    @ApiModelProperty(value = "支付版本")
//    private String version;
//
//    @ApiModelProperty(value = "签名")
//    private String sign;

    private String devMode;

    private String attach1;

    private String attach2;

    @ApiModelProperty(value = "和包电子券商户编号,若该字段不为空，那么支付报文中的ticketpayflag为2，paycnl为CMPAY，paydirect为0")
    private String ticketAccount;

    /**
     * 商户类型
     */
    private String reserve1;

    private String reserve2;


}
