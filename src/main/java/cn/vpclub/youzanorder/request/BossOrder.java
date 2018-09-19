package cn.vpclub.youzanorder.request;

import lombok.Data;

/**
 * Created by chentao on 2018/5/3.
 */
@Data
public class BossOrder {
    private String trade_no;
    private String telephone;
    private String deal_time;
    private Integer sign_type;
    private Integer signt_status;
    private String serial_number;
    private String merchant_no;
    private String batch_no;
    private Long price;
    private String boss_goods_no;
    private String coupon_name;
    private String remark;
    private String model_no;
    private String model_name;
    private String deal_no;
    private String deal_channel;
    private String deal_city;
    private String broadband_no;
    private String consumer_type;
}
