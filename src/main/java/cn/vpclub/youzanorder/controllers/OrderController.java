package cn.vpclub.youzanorder.controllers;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.core.model.response.PageDataResponse;
import cn.vpclub.moses.web.controller.AbstractController;
import cn.vpclub.youzanorder.constant.ConfigConstants;
import cn.vpclub.youzanorder.entity.ProductInfo;
import cn.vpclub.youzanorder.entity.SubOrder;
import cn.vpclub.youzanorder.request.CreateOrderRequest;
import cn.vpclub.youzanorder.request.SubOrderParam;
import cn.vpclub.youzanorder.response.SubOrderResponse;
import cn.vpclub.youzanorder.service.ProductInfoService;
import cn.vpclub.youzanorder.service.SubOrderService;
import cn.vpclub.youzanorder.utils.BaseUtil;
import cn.vpclub.youzanorder.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class OrderController extends AbstractController {

    private SubOrderService subOrderService;//拆单服务
    private ProductInfoService productInfoService;//拆单商品服务

    /**
     * 有赞订单拆单,确认收货都会调用,有可能重复调用!
     * @return
     */
    @PostMapping(value = "/add")
    public Object create(@RequestBody CreateOrderRequest request) {
        BaseResponse<Boolean> result = new BaseResponse<>();
        if (request != null) {
            String youanStatus = request.getStatus();//有赞的订单状态
            log.info("订单推送:" + request.getOrderId() + "状态" + youanStatus);
            if (ConfigConstants.WAIT_SELLER_SEND_GOODS.equals(youanStatus)) {
                result = subOrderService.insertOrder(request);
            } else if (ConfigConstants.TRADE_BUYER_SIGNED.equals(youanStatus)) {
                //确认收货订单
                result = subOrderService.confirmReceipt(request);
            } else {
                result=subOrderService.updateOrder(request);
            }
        }
        return result;
    }


    /**
     * 订单列表查询
     */
    @PostMapping(value = "/page")
    public PageDataResponse querySubOrder(@RequestBody(required = false) SubOrderParam param) {
        //运营的指定账号查询
        String outerItemId = param.getOuterItemId();
        if(param!=null&&"999224252890615810".equals(outerItemId)){
            PageDataResponse<SubOrder> result = subOrderService.findListSpecial(param);
            return result;
        }
        PageDataResponse<SubOrder> pageDataResponse = subOrderService.findList(param);
        return pageDataResponse;
    }

    /**
     * 订单详细查询，包含自商品信息
     */
    @PostMapping(value = "query")
    public BaseResponse query(@RequestBody SubOrder request) {
        BaseResponse baseResponse = subOrderService.query(request);
        SubOrder subOrder = (SubOrder) baseResponse.getDataInfo();
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("subOrderId", subOrder.getId());
        List<ProductInfo> productInfoList = productInfoService.queryList(queryParam);
        SubOrderResponse subOrderResponse = new SubOrderResponse();
        subOrderResponse.setBuyerMessages(subOrder.getBuyerMessage());
        BeanUtils.copyProperties(subOrder, subOrderResponse);
        subOrderResponse.setProductInfoList(productInfoList);
        baseResponse.setDataInfo(subOrderResponse);
        return baseResponse;
    }


}
