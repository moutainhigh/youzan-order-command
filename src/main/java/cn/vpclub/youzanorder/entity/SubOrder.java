package cn.vpclub.youzanorder.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *  商品订单明细表
 * Created by chentao on 2018/4/24.
 */
@TableName("sub_order")
@Data
public class SubOrder extends Model<SubOrder> {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 商品编码, 用于标记商户id
     */
    @TableField("outer_item_id")
    private String outerItemId;

    @TableField("total_fee")
    private String totalFee;

    /**
     *  订单状态
     */
    @TableField("state_str")
    private String stateStr;


    /**
     *  用户登录手机号
     */
    @TableField("user_phone")
    private String userPhone;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 订单id
     */
    @TableField("base_order_id")
    private String baseOrderId;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 支付状态
     */
    @TableField("pay_status")
    private String payStatus;

    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Long payTime;

    /**
     * 物流状态
     */
    @TableField("deliver_status")
    private String deliverStatus;

    /**
     * 省份
     */
    private String province;

    /**
     * 收货城市
     */
    private String city;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 收货地址
     */
    private String address;

    /**
     *  备注可以是json字符串
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
     *  逻辑删除
     */
    private Integer deleted;

    /**
     * 订单渠道 1,有赞 2，boss
     */
    @TableField("order_channel")
    private Integer orderChannel;

    /**
     * 批次号
     */
    @TableField("batch_no")
    private String batchNo;
    /**
     * 买家留言
     */
    @TableField("buyer_message")
    private String buyerMessage;

    @TableField("refund_status")
    private Byte refundStatus;//订单退款状态
    @TableField(exist = false)//订单是否被标记红色
    Boolean redError;
    @TableField("pay_type")
    private String payType;//支付方式

    @TableField("order_time")
    private Long orderTime;//有赞订单创建时间


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
