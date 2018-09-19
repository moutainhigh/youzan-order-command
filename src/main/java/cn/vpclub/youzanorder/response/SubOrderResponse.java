package cn.vpclub.youzanorder.response;

import cn.vpclub.youzanorder.entity.ProductInfo;
import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.util.List;

/**
 * Created by chentao on 2018/4/27.
 */
@Data
public class SubOrderResponse {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 商品编码, 用于标记商户id
     */
    private String outerItemId;

    private String totalFee;

    /**
     *  订单状态
     */
    private String stateStr;


    /**
     *  用户登录手机号
     */
    private String userPhone;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 订单id
     */
    private String baseOrderId;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 支付状态
     */
    private String payStatus;

    /**
     * 支付时间
     */
    private Long payTime;

    /**
     * 物流状态
     */
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
     * 创建时间
     */
    private Long createdTime;

    /**
     * 更新时间
     */
    private Long updatedTime;
    /**
     *  逻辑删除
     */
    private Integer deleted;

    private String buyerMessage;//买家信息
    private String buyerMessages;//买家信息
    /**
     * 订单商品集合
     */
    private List<ProductInfo> productInfoList;
}
