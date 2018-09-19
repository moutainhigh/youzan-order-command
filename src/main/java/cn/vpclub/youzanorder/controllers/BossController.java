package cn.vpclub.youzanorder.controllers;

import cn.vpclub.moses.core.enums.ReturnCodeEnum;
import cn.vpclub.moses.core.model.response.BackResponseUtil;
import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.utils.common.DateUtil;
import cn.vpclub.moses.utils.common.IdWorker;
import cn.vpclub.moses.utils.common.JsonUtil;
import cn.vpclub.moses.utils.web.HttpRequestUtil;
import cn.vpclub.moses.web.controller.AbstractController;
import cn.vpclub.youzanorder.constant.ConfigConstants;
import cn.vpclub.youzanorder.entity.ProductInfo;
import cn.vpclub.youzanorder.entity.SubOrder;
import cn.vpclub.youzanorder.request.BossOrder;
import cn.vpclub.youzanorder.request.SubOrderParam;
import cn.vpclub.youzanorder.response.SubOrderResponse;
import cn.vpclub.youzanorder.service.ProductInfoService;
import cn.vpclub.youzanorder.service.SubOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value ="/boss")
public class BossController extends AbstractController {

    @Value("${system.url.productDetailUrl}")
    private String productDetailUrl;
    @Autowired
    private SubOrderService subOrderService;//拆单服务
    @Autowired
    private ProductInfoService productInfoService;

    /**
     * boss订单插入
     * @param bossOrder
     * @return
     */
    @PostMapping("/insert")
    public BaseResponse orderAdd(@RequestBody(required = false) BossOrder bossOrder) {
        log.info("boss订单数据:{}", JsonUtil.objectToJson(bossOrder));
        BaseResponse baseResponse = new BaseResponse();
        try {
            SubOrderParam subOrderParam = new SubOrderParam();
            subOrderParam.setBaseOrderId(bossOrder.getTrade_no());
            List<SubOrder> subOrderList = subOrderService.queryList(subOrderParam);
            //如果根据主订单号查询数据库没有存在数据，那么就进行下单操作，此判断防止重复订单插入
            if (CollectionUtils.isEmpty(subOrderList)) {

                Long subOrderId = IdWorker.getId();
                //TODO 根据商品编码，查询供货商id

                Map<String, Object> param = new HashMap<>();
                param.put("productCode", bossOrder.getBoss_goods_no());

                String result = HttpRequestUtil.sendJsonPost(productDetailUrl, JsonUtil.objectToJson(param));
                log.info("远程调用结果，{}", result);
                Map<String, Object> resultMap = (Map<String, Object>) JsonUtil.jsonToMap(result).get("dataInfo");
                String outerItemId = (String) resultMap.get("merchantId");
                //商品价格
                BigDecimal price = new BigDecimal((String) resultMap.get("price"));
                //抵扣金额
                BigDecimal discountAmount = new BigDecimal(bossOrder.getPrice()).divide(new BigDecimal(100)).setScale(2);

                Integer signType = (Integer) resultMap.get("sign_type");

                BigDecimal totelPrice = new BigDecimal("0.00");

                SubOrder subOrder = new SubOrder();
                if (signType.intValue() == 100) {
                    totelPrice = totelPrice.add(discountAmount);
                    subOrder.setStateStr(ConfigConstants.ORDER_COMPLETED);
                    subOrder.setPayStatus(ConfigConstants.HAVING_PAIED);
                    subOrder.setDeliverStatus(ConfigConstants.DELIVERED);
                } else if (signType.intValue() == 300) {//TODO 需要和boss定义代客下单的类型，暂定为300
                    totelPrice = price.subtract(discountAmount);
                    subOrder.setStateStr(ConfigConstants.PENDING_DELIVERY);
                    subOrder.setPayStatus(ConfigConstants.HAVING_PAIED);
                    subOrder.setDeliverStatus(ConfigConstants.UNSHIPPED_DELIVERY);
                }
                subOrder.setId(subOrderId);
                subOrder.setOuterItemId(outerItemId);
                subOrder.setTotalFee(totelPrice.setScale(2).toString());
                subOrder.setUserPhone(bossOrder.getTelephone());
                subOrder.setBaseOrderId(bossOrder.getTrade_no());
                subOrder.setPayTime(DateUtil.formatDateTimeStr(bossOrder.getDeal_time(), "yyyy-MM-dd hh:mm:ss").getTime());
                subOrder.setOrderChannel(ConfigConstants.ORDER_CHANNEL_BOSS);
                subOrder.setBatchNo(bossOrder.getBatch_no());
                subOrder.setCreatedBy(0L);
                subOrder.setCreatedTime(new Date().getTime());
                subOrder.setUpdatedTime(new Date().getTime());
                baseResponse = subOrderService.add(subOrder);

                if (baseResponse != null && baseResponse.getReturnCode().intValue() == ReturnCodeEnum.CODE_1000.getCode().intValue()) {
                    ProductInfo productInfo = new ProductInfo();
                    productInfo.setId(IdWorker.getId());
                    productInfo.setSubOrderId(subOrderId);
                    productInfo.setOuterItemId(outerItemId);
                    productInfo.setOid(String.valueOf(resultMap.get("productId")));
                    productInfo.setPicThumbPath((String) resultMap.get("productImage"));
                    productInfo.setSkuPropertiesName((String) resultMap.get("productSize"));
                    productInfo.setPrice((String) resultMap.get("price"));
                    productInfo.setDiscountFee(discountAmount.setScale(2).toString());
                    productInfo.setTitle(bossOrder.getCoupon_name());
                    productInfo.setCreatedBy(0L);
                    productInfo.setStateStr(ConfigConstants.PENDING_DELIVERY);
                    productInfo.setDeliverStatus(ConfigConstants.UNSHIPPED_DELIVERY);
                    productInfo.setCreatedTime(new Date().getTime());
                    productInfo.setUpdatedTime(new Date().getTime());
                    baseResponse = productInfoService.add(productInfo);
                }
            }
        } catch (Exception e) {
            log.error("boss订单入库异常：{}", e);
            baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1005.getCode());
            baseResponse.setDataInfo("程序异常");
        }
        return baseResponse;
    }

    @PostMapping(value = "/query")
    public BaseResponse bossOrderQuery(@RequestBody(required = false) BossOrder bossOrder) {

        log.info("boss订单query参数：{}", JsonUtil.objectToJson(bossOrder).toString());

        BaseResponse baseResponse = new BaseResponse();
        SubOrderParam subOrderParam = new SubOrderParam();
        subOrderParam.setBaseOrderId(bossOrder.getTrade_no());
        List<SubOrder> subOrderList = subOrderService.queryList(subOrderParam);
        // boss订单只存在一条订单数据，所以只取一条
        SubOrder subOrder = subOrderList.get(0);
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("subOrderId", subOrder.getId());
        List<ProductInfo> productInfoList = productInfoService.queryList(queryParam);
        SubOrderResponse subOrderResponse = new SubOrderResponse();
        BeanUtils.copyProperties(subOrder, subOrderResponse);
        subOrderResponse.setProductInfoList(productInfoList);
        baseResponse.setDataInfo(subOrderResponse);
        return baseResponse;
    }
}
