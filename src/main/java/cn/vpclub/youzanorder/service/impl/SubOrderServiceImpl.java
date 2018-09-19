package cn.vpclub.youzanorder.service.impl;

import cn.vpclub.moses.core.enums.ReturnCodeEnum;
import cn.vpclub.moses.core.model.response.*;
import cn.vpclub.moses.utils.common.IdWorker;
import cn.vpclub.moses.utils.common.JsonUtil;
import cn.vpclub.moses.utils.common.StringUtil;
import cn.vpclub.youzanorder.constant.ConfigConstants;
import cn.vpclub.youzanorder.constant.RefunEnum;
import cn.vpclub.youzanorder.entity.ProductInfo;
import cn.vpclub.youzanorder.entity.SubOrder;
import cn.vpclub.youzanorder.entity.pay.PayRequest;
import cn.vpclub.youzanorder.entity.pay.SubOrderDto;
import cn.vpclub.youzanorder.mapper.SubOrderMapper;
import cn.vpclub.youzanorder.request.CreateOrderRequest;
import cn.vpclub.youzanorder.request.ProductInfoRequest;
import cn.vpclub.youzanorder.request.SubOrderParam;
import cn.vpclub.youzanorder.service.IMessageService;
import cn.vpclub.youzanorder.service.PayService;
import cn.vpclub.youzanorder.service.ProductInfoService;
import cn.vpclub.youzanorder.service.SubOrderService;
import cn.vpclub.youzanorder.utils.BaseUtil;
import cn.vpclub.youzanorder.utils.MsgStrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by chentao on 2018/4/24.
 */
@Service
@Slf4j
public class SubOrderServiceImpl extends ServiceImpl<SubOrderMapper, SubOrder> implements SubOrderService {

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private IMessageService messageService;

    public SubOrderServiceImpl() {
        super();
    }

    public SubOrderServiceImpl(SubOrderMapper baseMapper) {

        this.baseMapper = baseMapper;

    }


    @Override
    public BaseResponse add(SubOrder model) {
        boolean back = this.insert(model);
        BaseResponse baseResponse = ResponseConvert.convert(back);
        return baseResponse;
    }

    @Override
    public BaseResponse update(SubOrder model) {
        BaseResponse baseResponse;
        if (null == model || null == model.getId()) {
            baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1006.getCode());
        } else {
            boolean back = this.updateById(model);
            baseResponse = ResponseConvert.convert(back);
        }
        return baseResponse;
    }

    @Override
    public PageDataResponse<SubOrder> findList(SubOrderParam pageParam) {
        //设置分页信息
        PageDataResponse<SubOrder> pageData = new PageDataResponse<>();
        if (null != pageParam) {
            if (null == pageParam.getPageNumber()) {
                pageParam.setPageNumber(1);
            }
            if (null == pageParam.getPageSize()) {
                pageParam.setPageSize(10);
            }
            List<SubOrder> dataList = this.baseMapper.findDataByPageParam(pageParam);
            Integer dataTotal = 0;
            if (null != dataList && !dataList.isEmpty()) {
                if (null != pageParam.getPageNumber() && null != pageParam.getPageSize()) {
                    // 分页查询
                    dataTotal = this.baseMapper.findDataTotalByPageParam(pageParam);
                } else {
                    // 不分页查询
                    dataTotal = dataList.size();
                }
                //判断让卖家
                for (SubOrder or : dataList) {
                    if (or.getRefundStatus() == RefunEnum.OrderStatus.WAIT_HANDLE) {
                        or.setRedError(true);
                    } else {
                        or.setRedError(false);
                    }
                }
            }

            pageData.setPageData(dataList);
            pageData.setTotalRecord(dataTotal);
            pageData.setPageSize(pageParam.getPageSize());
            pageData.setPageNumber(pageParam.getPageNumber());
            pageData.setReturnCode(ReturnCodeEnum.CODE_1000.getCode());
        }
        return pageData;
    }

    /**
     * 单个查询
     *
     * @return
     */
    @Override
    public BaseResponse query(SubOrder model) {
        BaseResponse baseResponse;
        if (null == model || null == model.getId()) {
            baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1006.getCode());
        } else {
            SubOrder data = this.selectById(model.getId());
            if (null != data) {
                baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1000.getCode());
                baseResponse.setDataInfo(data);
            } else {
                baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1002.getCode());
            }
        }
        return baseResponse;
    }

    @Override
    public List<SubOrder> queryList(SubOrderParam param) {
        List<SubOrder> dataList = this.baseMapper.findDataByPageParam(param);
        return dataList;
    }


    @Override
    public PageDataResponse<SubOrder> findListSpecial(SubOrderParam param) {
        param = param == null ? new SubOrderParam() : param;
        if (null == param.getPageNumber()) {
            param.setPageNumber(1);
        }
        if (null == param.getPageSize()) {
            param.setPageSize(10);
        }
        EntityWrapper<SubOrder> ew = new EntityWrapper<>();
        ew.in("refund_status", Arrays.asList(RefunEnum.OrderStatus.HANDLE_MANUAL, RefunEnum.OrderStatus.HANDLE_DONE));
        if (!StringUtil.isEmpty(param.getStartTime())) {
            ew.ge("order_time", Long.valueOf(param.getStartTime()));
        }
        if (!StringUtil.isEmpty(param.getEndTime())) {
            ew.le("order_time", Long.valueOf(param.getEndTime()));
        }
        if (!StringUtil.isEmpty(param.getStateStr())) {
            ew.eq("state_str", param.getStateStr());
        }
        if (!StringUtil.isEmpty(param.getBaseOrderId())) {
            ew.eq("base_order_id", param.getBaseOrderId());
        }
        if (!StringUtil.isEmpty(param.getPhone())) {
            ew.eq("phone", param.getPhone());
        }
        Page<SubOrder> page = new Page<>();
        page.setCurrent(param.getPageNumber());//pageNo 默认1
        page.setSize(param.getPageSize());//pageSize 默认10
        List<SubOrder> dataList = baseMapper.selectPage(page, ew);
        if (!CollectionUtils.isEmpty(dataList)) {
            for (SubOrder order : dataList) {
                order.setRedError(true);
            }
        }
        Integer total = baseMapper.selectCount(ew);
        PageDataResponse<SubOrder> pageData = new PageDataResponse<>();
        pageData.setPageData(dataList);
        pageData.setTotalRecord(total);
        pageData.setPageSize(param.getPageSize());
        pageData.setPageNumber(param.getPageNumber());
        pageData.setReturnCode(ReturnCodeEnum.CODE_1000.getCode());
        return pageData;
    }

    @Override
    public BaseResponse<Integer> orderCount(String orderId) {
        BaseResponse<Integer> result = new BaseResponse<>();
        if (StringUtil.isEmpty(orderId)) {
            return BaseUtil.response(false, 0, "参数为空");
        }
        EntityWrapper<SubOrder> wp = new EntityWrapper<>();
        wp.eq("base_order_id", orderId);
        Integer i = baseMapper.selectCount(wp);
        result.setDataInfo(i == null ? 0 : i);
        result.setReturnCode(ReturnCodeEnum.CODE_1000.getCode());
        return result;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public BaseResponse<Boolean> insertOrder(CreateOrderRequest order) {
        BaseResponse<Boolean> result = new BaseResponse<>();
        //查询是否有重复订单
        BaseResponse<Integer> count = orderCount(order.getOrderId());
        if (!(count.getDataInfo() > 0)) {
            try {
                //获取所有商品
                List<Map<String, Object>> productList = JsonUtil.jsonToList(order.getProductList());
                if (!CollectionUtils.isEmpty(productList)) {
                    //封装商家和商品的对应关系
                    Map<String, ArrayList> orgDetail = new HashMap<>();
                    Set<String> orgIds = new HashSet<>();
                    for (Map<String, Object> productMap : productList) {
                        String outerItemId = (String) productMap.get("outer_item_id");
                        boolean add = orgIds.add(outerItemId);
                        if (add) {
                            ArrayList product = orgDetail.get(outerItemId);
                            if (product == null) {
                                product = new ArrayList();
                                orgDetail.put(outerItemId, product);
                            }
                            product.add(productMap);
                        } else {
                            ArrayList product = orgDetail.get(outerItemId);
                            product.add(productMap);
                        }
                    }
                    Long dateTime = System.currentTimeMillis();
                    //region 插入订单和商品数据
                    for (String outerItemId : orgDetail.keySet()) {
                        List<Map<String, Object>> products = orgDetail.get(outerItemId);
                        BigDecimal total_fee = new BigDecimal("0.00");
                        //统计总金额
                        for (Map<String, Object> productMap : products) {
                            BigDecimal payfee = new BigDecimal((String) productMap.get("total_fee"));
                            total_fee = total_fee.add(payfee);
                        }
                        Long subOrderId = IdWorker.getId();
                        SubOrder subOrder = new SubOrder();
                        subOrder.setId(subOrderId);
                        subOrder.setOuterItemId(outerItemId);
                        subOrder.setTotalFee(total_fee.setScale(2).toString());
                        subOrder.setUserPhone(order.getUserPhone());
                        subOrder.setOpenId(order.getOpenId());
                        subOrder.setBaseOrderId(order.getOrderId());
                        subOrder.setName(order.getName());
                        subOrder.setStateStr(ConfigConstants.PENDING_DELIVERY);//待发货
                        subOrder.setPayStatus(ConfigConstants.HAVING_PAIED);
                        subOrder.setPayTime(order.getPayTime());
                        subOrder.setDeliverStatus(ConfigConstants.UNSHIPPED_DELIVERY);//未发货
                        subOrder.setProvince(order.getProvince());
                        subOrder.setCity(order.getCity());
                        subOrder.setAddress(order.getAddress());
                        subOrder.setPhone(order.getPhone());
                        subOrder.setOrderChannel(ConfigConstants.ORDER_CHANNEL_YOUZAN);
                        subOrder.setCreatedBy(0L);
                        subOrder.setCreatedTime(dateTime);
                        subOrder.setUpdatedTime(dateTime);
                        subOrder.setOrderTime(order.getCreateTime());//有赞的订单时间
                        subOrder.setPayType(order.getPayType());
                        subOrder.setBuyerMessage(order.getBuyerMessage());
                        boolean back = this.insert(subOrder);
                        ProductInfo firstP = null;
                        if (back) {
                            //region 循环插入商品
                            boolean first = true;
                            for (Map<String, Object> productMap : products) {
                                ProductInfo productInfo = new ProductInfo();
                                productInfo.setId(IdWorker.getId());
                                productInfo.setSubOrderId(subOrderId);
                                productInfo.setIsVirtual(Integer.valueOf(String.valueOf(productMap.get("is_virtual"))));
                                productInfo.setOuterItemId(String.valueOf(productMap.get("outer_item_id")));
                                productInfo.setPicThumbPath(String.valueOf(productMap.get("pic_thumb_path")));
                                productInfo.setItemType(Integer.valueOf(String.valueOf(productMap.get("item_type"))));
                                productInfo.setNum(String.valueOf(productMap.get("num")));
                                productInfo.setRefundedFee(new BigDecimal(String.valueOf(productMap.get("refunded_fee"))));
                                productInfo.setNumIid(String.valueOf(productMap.get("num_iid")));
                                productInfo.setOid(String.valueOf(productMap.get("new_oid_str")));
                                productInfo.setTitle(String.valueOf(productMap.get("title")));
                                productInfo.setFenxiaoPayment(String.valueOf(productMap.get("fenxiao_payment")));
                                productInfo.setDiscountFee(String.valueOf(productMap.get("discount_fee")));
                                List<Map<String, Object>> buyerMessages = (List<Map<String, Object>>) productMap.get("buyer_messages");
                                productInfo.setBuyerMessages(JsonUtil.objectToJson(buyerMessages));
                                productInfo.setIsPresent(Integer.valueOf(String.valueOf(productMap.get("is_present"))));
                                productInfo.setPrice(String.valueOf(productMap.get("price")));
                                productInfo.setFenxiaoPrice(String.valueOf(productMap.get("fenxiaoPrice")));
                                productInfo.setTotalFee(String.valueOf(productMap.get("total_fee")));
                                productInfo.setAlias(String.valueOf(productMap.get("alias")));
                                productInfo.setPayment(String.valueOf(productMap.get("payment")));
                                productInfo.setOuterSkuId(String.valueOf(productMap.get("outer_sku_id")));
                                productInfo.setSkuUniqueCode(String.valueOf(productMap.get("sku_unique_code")));
                                productInfo.setIsSend(Integer.valueOf(String.valueOf(productMap.get("is_send"))));
                                productInfo.setItemId(String.valueOf(productMap.get("item_id")));
                                productInfo.setSkuId(String.valueOf(productMap.get("sku_id")));
                                productInfo.setSkuPropertiesName(String.valueOf(productMap.get("sku_properties_name")));
                                productInfo.setPicPath(String.valueOf(productMap.get("pic_path")));
                                productInfo.setItemRefundState(String.valueOf(productMap.get("item_refund_state")));
                                productInfo.setUnit(String.valueOf(productMap.get("unit")));
                                List<Map<String, Object>> orderPpromotionDetails = (List<Map<String, Object>>) productMap.get("order_promotion_details");
                                productInfo.setOrderPromotionDetails(JsonUtil.objectToJson(orderPpromotionDetails));
                                productInfo.setAllowSend(Integer.valueOf(String.valueOf(productMap.get("allow_send"))));
                                productInfo.setSellerNick(String.valueOf(productMap.get("seller_nick")));
                                productInfo.setCreatedBy(0L);
                                productInfo.setStateStr(ConfigConstants.PENDING_DELIVERY);
                                productInfo.setDeliverStatus(ConfigConstants.UNSHIPPED_DELIVERY);
                                productInfo.setCreatedTime(dateTime);
                                productInfo.setUpdatedTime(dateTime);
                                BaseResponse productData = productInfoService.add(productInfo);
                                if (!BaseUtil.isSucceed(productData)) {
                                    throw new SQLException("商品插入失败!");
                                }
                                if (first) {
                                    firstP = productInfo;
                                    first = false;
                                }
                            }
                            //endregion
                            //向商家发送短信
                            String content = MsgStrUtil.orderNewMsg(subOrder, firstP);
                            messageService.sendSellerSMS(subOrder.getOuterItemId(), content);
                        } else {
                            throw new SQLException("订单插入失败!");
                        }
                    }
                    //endregion
                } else {
                    result.setDataInfo(false);
                    result.setReturnCode(ReturnCodeEnum.CODE_1006.getCode());
                    result.setMessage("商品为空");
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                log.error("失败json:" + JsonUtil.objectToJson(order));
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setDataInfo(false);
                result.setReturnCode(ReturnCodeEnum.CODE_1006.getCode());
                result.setMessage("插入失败");
                return result;
            }
        }
        result.setDataInfo(true);
        result.setReturnCode(ReturnCodeEnum.CODE_1000.getCode());
        return result;
    }


    @Override
    public BaseResponse<Boolean> updateOrder(CreateOrderRequest order) {
        String status = order.getStatus();
        if (ConfigConstants.TRADE_SUCCESS.equals(status) || ConfigConstants.TRADE_CLOSED.equals(status)) {
            SubOrder subOrder = new SubOrder();
            subOrder.setUpdatedTime(System.currentTimeMillis());
            EntityWrapper<SubOrder> pw = new EntityWrapper<>();
            pw.eq("base_order_id", order.getOrderId());
            if (ConfigConstants.TRADE_SUCCESS.equals(status)) {
                subOrder.setStateStr(ConfigConstants.ORDER_COMPLETED);//交易完成
                baseMapper.update(subOrder, pw);
            } else if (ConfigConstants.TRADE_CLOSED.equals(status)) {
                subOrder.setStateStr(ConfigConstants.ORDER_CLOSE);
                baseMapper.update(subOrder, pw);
            }
        }
        return BaseUtil.response(true, true, "操作成功");
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public BaseResponse<Boolean> confirmReceipt(CreateOrderRequest order) {
        BaseResponse<Boolean> result = new BaseResponse<>();
        if (order == null || StringUtil.isEmpty(order.getOrderId())) {
            return BaseUtil.response(false, false, "参数为空");
        }
        EntityWrapper<SubOrder> swp = new EntityWrapper<>();
        swp.eq("base_order_id", order.getOrderId());
        List<SubOrder> subOrderList = baseMapper.selectList(swp);
        try {
            if (!CollectionUtils.isEmpty(subOrderList)) {
                Long time = System.currentTimeMillis();
                List<Long> subId = new ArrayList<>();
                for (SubOrder subOrder : subOrderList) {
                    subId.add(subOrder.getId());
                }
                //更新所有订单的状态
                EntityWrapper<SubOrder> swp1 = new EntityWrapper<>();
                swp1.in("id", subId);
                SubOrder sub = new SubOrder();
                sub.setStateStr(ConfigConstants.CONFRIM_DELIVERY);
                sub.setUpdatedTime(time);
                baseMapper.update(sub, swp1);
                //更新所有订单下商品的状态
                EntityWrapper<ProductInfo> wp = new EntityWrapper<>();
                wp.in("sub_order_id", subId);
                ProductInfo product = new ProductInfo();
                product.setStateStr(ConfigConstants.CONFRIM_DELIVERY);
                product.setUpdatedTime(time);
                productInfoService.updateAdvance(product, wp);
            } else {
                log.error("确认收货失败(订单不存在):" + JsonUtil.objectToJson(order));
                return BaseUtil.response(false, false, "订单不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("确认收货失败:" + JsonUtil.objectToJson(order));
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
            result.setDataInfo(false);
            result.setReturnCode(ReturnCodeEnum.CODE_1006.getCode());
            result.setMessage("确认收货失败");
            return result;
        }
        result.setDataInfo(true);
        result.setReturnCode(ReturnCodeEnum.CODE_1000.getCode());
        return result;
    }

    @Override
    public BaseResponse<List<SubOrder>> getByYouZan(String orderId) {
        if (StringUtil.isEmpty(orderId)) {
            return BaseUtil.response(false, null, "参数为空");
        }
        EntityWrapper<SubOrder> wp = new EntityWrapper<>();
        wp.eq("base_order_id", orderId);
        List<SubOrder> list = baseMapper.selectList(wp);
        return BaseUtil.response(true, list, "查询成功");
    }

    @Override
    public BaseResponse test() {
        EntityWrapper<SubOrder> s = new EntityWrapper<>();
        Map<String, Object> map = new HashMap<>();
        map.put("outer_item_id", "4010");
        map.put("total_fee", "136.00");
        s.eq("outer_item_id", "4607");
//        s.eq("base_order_id", "E20180427151217082700006");
        s.or().eq("outer_item_id", "4608");
//        s.allEq(map);
//        s.in("total_fee", "136.00");
        //s.and("total_fee", "136.00");
        //把查询对象封装成map
//        List<Map<String, Object>> maps = baseMapper.selectMaps(s);
        //把查询对象序列化
//        List<SubOrder> list = this.baseMapper.selectList(s);
        List<SubOrder> list = this.baseMapper.selectList(s);
//        Page<SubOrder> page = new Page<>();
//        page.setCurrent(4);//pageNo 默认1
//        page.setSize(1);//pageSize 默认10
//        page.set
//        List<SubOrder> subOrders = baseMapper.selectPage(page, new EntityWrapper<>());
//        System.out.println(list);
        return BaseUtil.response(true, list, "查询成功");
    }
}
