package cn.vpclub.youzanorder.request;

import cn.vpclub.moses.utils.validator.AbstractGenericParameter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chentao on 2018/4/23.
 */
@Data
public class CreateOrderRequest extends AbstractGenericParameter {

    /**
     * 收货地址
     */
    private String address;

    /**
     * 收货城市
     */
    private String city;

    /**
     * 创建时间
     */
    private Long createTime;

    private String name;
    /**
     * 微信openId
     */
    private String openId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 支付时间
     */
    private Long payTime;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 省份
     */
    private String province;
    /**
     * 状态
     */
    private String status;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     *  商品数据
     */
    private String productList;

    /**
     * 物流方式
     */
    private Integer logisticsWay;

    private String buyerMessage;//卖家留言

    private String payType;

}
