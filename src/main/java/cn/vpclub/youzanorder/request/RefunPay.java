package cn.vpclub.youzanorder.request;


import cn.vpclub.youzanorder.utils.vaild.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class RefunPay implements Serializable {

    @NotNull(message = "tid=null")
    private String tid;//有赞订单编号
    @NotNull(message = "oid=null")
    private Long oid;//订单内商品id
    private Long new_oid;//订单内商品id
    @NotNull(message = "refund_id=null")
    private String refund_id; //退款id
    @NotNull(message = "refund_state=null")
    private String refund_state;//退款状态
    @NotNull(message = "update_time=null")
    private String update_time;//更新时间
    @NotNull(message = "refunded_fee=null")
    private String refunded_fee;//退款金额
    @NotNull(message = "refund_type=null")
    private String refund_type;//退款类型
    @NotNull(message = "refund_reason=null")
    private String refund_reason; //退款原因


}
