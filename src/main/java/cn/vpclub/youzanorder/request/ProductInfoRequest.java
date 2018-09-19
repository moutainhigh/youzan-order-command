package cn.vpclub.youzanorder.request;

import lombok.Data;

import java.util.List;

/**
 * Created by chentao on 2018/4/28.
 */
@Data
public class ProductInfoRequest {
    private Long id;
    private Long subOrderId;
    private Integer oid;
    /**
     *  订单状态
     */
    private String stateStr;

    /**
     * 物流状态
     */
    private String deliverStatus;

    /**
     *  物流单号
     */
    private String logisticsNumber;

    /**
     * 物流公司名称
     */
    private String logisticsCompanyName;
    /**
     *  物流公司id
     */
    private String logisticsCompanyId;

    private List<Integer> oidList;
}
