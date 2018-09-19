package cn.vpclub.youzanorder.controllers;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.web.controller.AbstractController;
import cn.vpclub.youzanorder.common.LogisticsData;
import cn.vpclub.youzanorder.common.LogisticsOrg;
import cn.vpclub.youzanorder.common.YouZanOrder;
import cn.vpclub.youzanorder.service.ILogisticsDataService;
import cn.vpclub.youzanorder.utils.BaseUtil;
import cn.vpclub.youzanorder.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/logInfo")
public class LogsticsController extends AbstractController {

    private ILogisticsDataService logisticsDataService;//物流服务

    /**
     * 查询所有物流公司信息
     * @return
     */
    @RequestMapping(value = "/logistics", method = RequestMethod.GET)
    public BaseResponse getLogistics() {
        BaseResponse<List<LogisticsOrg>> result = logisticsDataService.getYouZanLogOrgs();
        return result;
    }

    /**
     * 提交物流信息
     * @param order orderId      三合一订单id
     * @param order tradeOrderId 有赞订单id
     * @param order productId    有赞商品id
     * @param order logId        物流单号
     * @param order logOrgId     物流公司id
     * @return
     */
    @RequestMapping(value = "/logistics", method = RequestMethod.POST)
    public BaseResponse updateLogistics(@RequestBody YouZanOrder order) {
        if (order == null) {
            return BaseUtil.response(false, null, "参数为空");
        }
        BaseResponse<Boolean> result = logisticsDataService.updateOrderLogs(order.getOrderId(), order.getTradeOrderId(),
                order.getProductId(), order.getLogId(), order.getLogOrgId());
        return result;
    }

}
