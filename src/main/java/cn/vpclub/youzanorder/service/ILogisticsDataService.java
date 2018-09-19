package cn.vpclub.youzanorder.service;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.youzanorder.common.LogisticsData;
import cn.vpclub.youzanorder.common.LogisticsOrg;
import cn.vpclub.youzanorder.request.LogisticsReq;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

public interface ILogisticsDataService {

    BaseResponse<List<LogisticsOrg>> getYouZanLogOrgs();


    /**
     * 更新物流信息
     *
     * @param orderId 三合一订单id
     * @param tradeOrderId 有赞订单id
     * @param productId 有赞商品id
     * @param logId 物流单号
     * @param logOrgId 物流公司id
     * @return
     */
    BaseResponse<Boolean> updateOrderLogs(String orderId, String tradeOrderId, List<String> productId,
                                          String logId, String logOrgId);

    /**
     * 批量更新物流信息
     * @param batch
     * @return
     */
    BaseResponse<Boolean> updateLogisticsBatch(List<LogisticsData> batch);
}
