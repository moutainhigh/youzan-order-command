package cn.vpclub.youzanorder.entity.pay;

import cn.vpclub.moses.utils.constant.ValidatorConditionType;
import cn.vpclub.moses.utils.validator.BaseAbstractParameter;
import cn.vpclub.moses.utils.validator.annotations.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SubOrderDto extends BaseAbstractParameter {


    @ApiModelProperty(value = "子订单id")
    @NotNull(when = {ValidatorConditionType.CREATE, ValidatorConditionType.UPDATE, ValidatorConditionType.DELETE})
    private String subMerchOrderNo;

    @ApiModelProperty(value = "子订单金额")
    @NotNull(when = {ValidatorConditionType.CREATE, ValidatorConditionType.UPDATE, ValidatorConditionType.DELETE})
    private Integer payAmount;

    @ApiModelProperty(value = "清结算商户编码")
    private String payStoreId;

    @ApiModelProperty(value = "商户编码")
    private String storeId;

    //预留，可以为空值
    @ApiModelProperty(value = "邮费")
    private Integer logisticsFee;

    private String reserve1;

    private String reserve2;

    private String reserve3;

    private String reserve4;

    private String reserve5;

    private String reserve6;

    private String reserve7;

    private String reserve8;

    private String reserve9;

    private String reserve10;
}
