package cn.vpclub.youzanorder.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *  订单主表
 * Created by chentao on 2018/4/24.
 */
@Data
@TableName("order_basic")
public class OrderBasic extends Model<OrderBasic> {

    /**
     * 主键id
     */
    private Long id;

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
    @TableField("create_time")
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
    @TableField("pay_time")
    private Long payTime;

    /**
     * 联系电话
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
    @TableField("user_phone")
    private String userPhone;

    /**
     * 订单渠道 1,有赞 2，boss
     */
    @TableField("order_channel")
    private String orderChannel;

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

    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
