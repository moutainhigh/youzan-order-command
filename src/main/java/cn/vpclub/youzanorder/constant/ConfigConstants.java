package cn.vpclub.youzanorder.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by asus on 2018/4/24.
 */
@Component
public class ConfigConstants {

    /**
     * 支付状态
     */
    public static final String UnPAID = "待支付";

    public static final String HAVING_PAIED = "已支付";

    /**
     *  发货状态
     */

    public static final String UNSHIPPED_DELIVERY = "未发货";
    public static final String DELIVERED = "已发货";

    /**
     * 三合一订单状态
     */
    public static final String PENDING_DELIVERY = "待发货";
    public static final String PENDING_RECEIVED = "待收货";
    public static final String CONFRIM_DELIVERY = "买家签收";
    public static final String ORDER_CLOSE = "交易关闭";
    public static final String ORDER_COMPLETED = "交易成功";


    //有赞订单状态
    public static final String WAIT_SELLER_SEND_GOODS = "WAIT_SELLER_SEND_GOODS";//待发货
    public static final String TRADE_BUYER_SIGNED = "TRADE_BUYER_SIGNED";//买家已签收
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";//交易成功
    public static final String TRADE_CLOSED = "TRADE_CLOSED";//交易关闭

    /**
     * 有赞渠道编号
     */
    public static final int ORDER_CHANNEL_YOUZAN = 1;

    /**
     * boss渠道编号
     */
    public static final int ORDER_CHANNEL_BOSS = 2;

    public static final int BOSS_ORDERTYPE_COUPON = 1;

    public static final int BOSS_ORDERTYPE_GUEST = 2;


}
