package cn.vpclub.youzanorder.service.impl;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.utils.common.IdWorker;
import cn.vpclub.moses.utils.common.StringUtil;
import cn.vpclub.youzanorder.constant.RefunEnum;
import cn.vpclub.youzanorder.entity.ProductInfo;
import cn.vpclub.youzanorder.entity.RefundInfo;
import cn.vpclub.youzanorder.entity.SubOrder;
import cn.vpclub.youzanorder.request.RefunPay;
import cn.vpclub.youzanorder.request.RefunResult;
import cn.vpclub.youzanorder.service.*;
import cn.vpclub.youzanorder.utils.BaseUtil;
import cn.vpclub.youzanorder.utils.HttpUtils;
import cn.vpclub.youzanorder.utils.JsonUtil;
import cn.vpclub.youzanorder.utils.MsgStrUtil;
import cn.vpclub.youzanorder.utils.vaild.DataValidUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class RefunService implements IRefunService {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private SubOrderService subOrderService;

    @Autowired
    private IRefundInfoService refundInfoService;
    @Autowired
    private IMessageService messageService;
    @Value("${system.url.youZanRefunPayAgree}")
    private String payAgreeUrl; //同意退款地址

    @Value("${system.url.youZanRefunPayRefuse}")
    private String payRefusedUrl;//不同意退款地址

    @Value("${system.url.youZanRefunGoodAgree}")
    private String goodAgreeUrl;//同意退货地址

    @Value("${system.url.youZanRefunGoodRefuse}")
    private String goodRefusedUrl;//不同意退货地址


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public BaseResponse<Boolean> handleRefun(RefunPay msg) {
        log.info("退款推送:" + JsonUtil.turnJson(msg));
        DataValidUtils.Result val = DataValidUtils.validation(msg);
        if (!val.isSuc()) {
            return BaseUtil.response(false, false, val.getMsg());
        }
        Long dateTime = System.currentTimeMillis();
        List<SubOrder> subList = subOrderService.getByYouZan(msg.getTid()).getDataInfo();
        if (!CollectionUtils.isEmpty(subList)) {
            List<Long> subIds = new ArrayList<>();
            for (SubOrder order : subList) {
                subIds.add(order.getId());
            }
            List<ProductInfo> productList = productInfoService.getListBySubId(subIds).getDataInfo();
            if (!CollectionUtils.isEmpty(productList)) {
                SubOrder mainOrder = null;
                ProductInfo mainproduct = null;
                //找到商品
                for (ProductInfo p : productList) {
                    if (p.getOid().equals(String.valueOf(msg.getOid()))) {
                        mainproduct = p;
                        //找到订单
                        for (SubOrder order : subList) {
                            if (order.getId().equals(p.getSubOrderId())) {
                                mainOrder = order;
                            }
                        }
                    }
                }

                if (mainOrder != null && mainproduct != null) {
                    BigDecimal reFee = new BigDecimal(msg.getRefunded_fee());//退款金额
                    //处理退款表
                    EntityWrapper<RefundInfo> ew = new EntityWrapper<>();
                    ew.eq("tid", msg.getTid()).eq("oid", msg.getOid());
                    List<RefundInfo> list = refundInfoService.selectAdvance(ew).getDataInfo();
                    String refund_type = msg.getRefund_type();
                    int reType = refund_type.equals("REFUND_ONLY") ? 1 : 2;
                    String state = msg.getRefund_state();//退款状态
                    String currentState = "";
                    if (CollectionUtils.isEmpty(list)) {
                        RefundInfo re = new RefundInfo();
                        re.setTid(msg.getTid());
                        re.setSubOrderId(mainOrder.getId());
                        re.setOid(msg.getOid());
                        re.setRefundState(state);
                        re.setId(IdWorker.getId());
                        re.setCreateTime(dateTime);
                        re.setUpdateTime(dateTime);
                        re.setRefundType(reType);
                        re.setOid(msg.getOid());
                        re.setPayType(mainOrder.getPayType());
                        re.setProductId(mainproduct.getId());
                        re.setRefundId(msg.getRefund_id());
                        re.setRefundedFee(reFee);
                        re.setRefundReason(RefunEnum.Reason.getDesc(msg.getRefund_reason()));
                        re.setRefundMsg("买家:" + RefunEnum.Reason.getDesc(msg.getRefund_reason()) + "\n");
                        refundInfoService.insert(re);
                    } else {
                        RefundInfo re = list.get(0);
                        //如果存在记录的情况下,推送新建,说明是重复申请
                        if (RefunEnum.SAFE_NEW.equals(state)) {
                            state = RefunEnum.SAFE_HANDLED;
                            re.setRefundMsg(re.getRefundMsg() + "买家:" + RefunEnum.Reason.getDesc(msg.getRefund_reason()) + "\n");
                        } else if (RefunEnum.SAFE_REJECT.equals(state)) {
                            //如果上一次也是拒绝,说明是客服介入
                            if (re.getRefundState().equals(state)) {
                                state = RefunEnum.SAFE_INVOLVED;
                                re.setRefundMsg(re.getRefundMsg() + "买家:" + RefunEnum.Reason.getDesc(msg.getRefund_reason()) + "\n");
                            }
                        }
                        re.setRefundType(reType);
                        re.setUpdateTime(dateTime);
                        re.setLastRefundState(re.getRefundState());
                        currentState = re.getRefundState();//当前状态
                        re.setRefundState(state);
                        re.setRefundReason(RefunEnum.Reason.getDesc(msg.getRefund_reason()));
                        refundInfoService.update(re);
                    }
                    mainproduct.setRefundedFee(reFee);
                    //新增的退款
                    if (RefunEnum.SAFE_NEW.equals(state)) {
                        mainproduct.setRefundStatus(RefunEnum.ProductStatus.WAIT_HANDLE);//待处理
                        mainOrder.setRefundStatus(RefunEnum.OrderStatus.WAIT_HANDLE);//待处理
                        subOrderService.updateById(mainOrder);
                        productInfoService.updateById(mainproduct);
                        //向卖家发送短信
                        String refunMsg = MsgStrUtil.orderRefunMsg(mainOrder, mainproduct);
                        messageService.sendSellerSMS(mainOrder.getOuterItemId(), refunMsg);
                    } else if (RefunEnum.SAFE_HANDLED.equals(state) || RefunEnum.SAFE_INVOLVED.equals(state)) {//再次发起退款
                        mainproduct.setRefundStatus(RefunEnum.ProductStatus.WAIT_HANDLE);//待处理
                        mainOrder.setRefundStatus(RefunEnum.OrderStatus.HANDLE_MANUAL);//人工处理中
                        subOrderService.updateById(mainOrder);
                        productInfoService.updateById(mainproduct);
                    } else if (RefunEnum.SAFE_CLOSE.equals(state) || RefunEnum.SAFE_FINISHED.equals(state)) {
                        //如果是拒绝状态下的结束,把商品状态改为未申请(为了发货按钮可点)
                        if (currentState.equals(RefunEnum.SAFE_REJECT)) {
                            mainproduct.setRefundStatus(RefunEnum.ProductStatus.NO_REFUND);//未申请
                        } else {
                            mainproduct.setRefundStatus(RefunEnum.ProductStatus.HANDLE_OVER);//维权结束
                        }
                        //订单下是否还有待处理的商品
                        boolean onWait=false;
                        for (ProductInfo p : productList) {
                            if (p.getSubOrderId().longValue() == mainOrder.getId().longValue()) {
                                if(p.getId().longValue()!=mainproduct.getId().longValue()&&
                                        p.getRefundStatus()== RefunEnum.ProductStatus.WAIT_HANDLE){
                                    onWait=true;
                                }
                            }
                        }
                        if (!onWait) {
                            mainOrder.setRefundStatus(RefunEnum.OrderStatus.HANDLE_CLOSE);//维权结束
                            subOrderService.updateById(mainOrder);
                        }
                            productInfoService.updateById(mainproduct);
                        }
                } else {
                    log.error("退款订单或商品不存在:" + msg.getTid() + ",oid=" + msg.getOid());
                }
            } else {
                log.error("退款商品不存在:" + msg.getTid());
            }
        } else {
            log.error("退款订单不存在:" + msg.getTid());
        }

        return BaseUtil.response(true, true, "处理成功");
    }


    @Override
    public BaseResponse<Boolean> refunRespon(RefunResult req) {
        //参数校验
        if (req == null) {
            return BaseUtil.response(false, false, "参数为空");
        }
        DataValidUtils.Result result = DataValidUtils.validation(req);
        if (!result.isSuc()) {
            return BaseUtil.response(false, false, result.getMsg());
        }
        //查询商品退货信息
        EntityWrapper<RefundInfo> ew = new EntityWrapper<>();
        ew.eq("product_id", req.getProductId());
        List<RefundInfo> reList = refundInfoService.selectAdvance(ew).getDataInfo();
        RefundInfo refundInfo = null;//退款详情
        if (CollectionUtils.isEmpty(reList)) {
            return BaseUtil.response(false, false, "没有查询到退款信息");
        }
        refundInfo = reList.get(0);
        if (refundInfo.getRefundType() == RefunEnum.REFUND_AND_RETURN && req.getResult()) {
            if (StringUtil.isEmpty(req.getPhone()) || StringUtil.isEmpty(req.getAddress())
                    || StringUtil.isEmpty(req.getName())) {
                return BaseUtil.response(false, false, "收货信息不能为空");
            }
        }

        //退款流程处理
        String productId = req.getProductId();
        ProductInfo product = productInfoService.selectById(Long.valueOf(productId));
        if (product == null) {
            return BaseUtil.response(false, false, "商品id错误");
        } else if (product.getRefundStatus() != RefunEnum.ProductStatus.WAIT_HANDLE) {
            return BaseUtil.response(false, false, "商品不需处理");
        }
        Map<String, String> reqData = new HashMap<>();
        reqData.put("refundId", refundInfo.getRefundId());
        reqData.put("message", req.getMsg());
        reqData.put("mobile", req.getPhone());
        reqData.put("address", req.getAddress());
        reqData.put("name", req.getName());
        reqData.put("phone", req.getPhone());
        reqData.put("post", req.getPost());
        String reqJson = JsonUtil.turnJson(reqData);
        String resJson = null;
        //如果是退款退货,需要先调用退货接口
        try {
            if (req.getResult()) {
                product.setRefundStatus(RefunEnum.ProductStatus.HANDLE_YES);//同意
                refundInfo.setRefundState(RefunEnum.SAFE_ACCEPT);//卖家接受
                refundInfo.setRefunAddress(req.getAddress());
                refundInfo.setRefunName(req.getName());
                refundInfo.setRefunPhone(req.getPhone());
//                if (refundInfo.getRefundType() == RefunEnum.REFUND_AND_RETURN) {
//                    log.info("退款同意请求" + reqJson);
//                    log.info("退款同意地址" + payAgreeUrl);
//                    resJson = HttpUtils.JsonPost(payAgreeUrl, reqJson, 10000);
//                    log.info("退款回复"+resJson);
////                    if (!JsonUtil.isYouZanError(resJson)) {
//                    if (true) {
//                        log.info("退货同意请求" + reqJson);
//                        log.info("退货同意地址" + goodAgreeUrl);
//                        resJson = HttpUtils.JsonPost(goodAgreeUrl, reqJson, 10000);
//                        log.info("退货回复"+resJson);
//                    }
//                } else {
//                    log.info("退款同意请求" + reqJson);
//                    log.info("退款同意地址" + payAgreeUrl);
//                    resJson = HttpUtils.JsonPost(payAgreeUrl, reqJson, 10000);
//                    log.info("退款回复"+reqJson);
//                }
            } else {
                refundInfo.setRefundState(RefunEnum.SAFE_REJECT);//卖家拒绝
                product.setRefundStatus(RefunEnum.ProductStatus.HANDLE_NO);//不同意
//                if (refundInfo.getRefundType() == RefunEnum.REFUND_AND_RETURN) {
//                        log.info("退款拒绝请求" + reqJson);
//                        log.info("退款拒绝地址" + payRefusedUrl);
//                        resJson = HttpUtils.JsonPost(payRefusedUrl, reqJson, 10000);
//                    log.info("退款回复"+resJson);
////                    if (!JsonUtil.isYouZanError(resJson)) {
//                    if (true) {
//                        log.info("退货拒绝请求" + reqJson);
//                        log.info("退货拒绝地址" + goodRefusedUrl);
//                        resJson = HttpUtils.JsonPost(goodRefusedUrl, reqJson, 10000);
//                        log.info("退货回复"+resJson);
//                    }
//                } else {
//                    log.info("退款拒绝请求" + reqJson);
//                    log.info("退款拒绝地址" + payRefusedUrl);
//                    resJson = HttpUtils.JsonPost(payRefusedUrl, reqJson, 10000);
//                    log.info("退款回复"+resJson);
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("退款处理异常:" + reqJson);
            return BaseUtil.response(false, false, "有赞请求异常");
        }
        //判断请求是否成功
//        try {
//            if (JsonUtil.isYouZanError(resJson)) {
//                log.info("退款处理失败" + refundInfo.getRefundId() + ":" + resJson);
//                String message = JsonUtil.getJsonParam("message", resJson);
//                String resMsg = JsonUtil.getJsonParam("msg", message);
//                return BaseUtil.response(false, false, resMsg);
//            }
//        } catch (Exception e) {
//            log.info("退款处理失败" + refundInfo.getRefundId() + ":" + resJson);
//            return BaseUtil.response(false, false, "退款处理失败");
//        }
        productInfoService.updateById(product);
        refundInfo.setRefundMsg(refundInfo.getRefundMsg() + "商家:" + req.getMsg() + "\n");
        refundInfoService.update(refundInfo);
        //如果该订单没有待处理商品,订单标记已处理
        EntityWrapper<ProductInfo> pw = new EntityWrapper<>();
        pw.eq("sub_order_id", product.getSubOrderId()).eq("refund_status", RefunEnum.ProductStatus.WAIT_HANDLE);
        Integer i = productInfoService.countAdvance(pw).getDataInfo();
        if (i == 0) {
            SubOrder order = new SubOrder();
            order.setId(product.getSubOrderId());
            order.setRefundStatus(RefunEnum.OrderStatus.HANDLE_DONE);
            subOrderService.updateById(order);
        }
        return BaseUtil.response(true, true, "处理成功");
    }
}
