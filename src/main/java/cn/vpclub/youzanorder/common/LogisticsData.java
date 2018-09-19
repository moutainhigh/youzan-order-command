package cn.vpclub.youzanorder.common;

import cn.vpclub.youzanorder.utils.vaild.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量导入物流数据封装对象
 */
@Data
public class LogisticsData implements Serializable {

    @NotNull(message = "orderId为空")
    private String orderId; //三合一id
    @NotNull(message = "baseOrderId为空")
    private String baseOrderId;//有赞订单号
    @NotNull(message = "oidList为空")
    private List<String> oidList;//有赞订单内商品id集合
    @NotNull(message = "logId为空")
    private String logId;//物流单号
    @NotNull(message = "logOrgId为空")
    private String logOrgId;//物流公司id
}
