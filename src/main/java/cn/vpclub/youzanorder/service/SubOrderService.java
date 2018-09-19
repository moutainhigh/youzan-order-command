package cn.vpclub.youzanorder.service;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.core.model.response.PageDataResponse;
import cn.vpclub.youzanorder.entity.SubOrder;
import cn.vpclub.youzanorder.request.CreateOrderRequest;
import cn.vpclub.youzanorder.request.ProductInfoRequest;
import cn.vpclub.youzanorder.request.SubOrderParam;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * Created by chentao on 2018/4/24.
 */
public interface SubOrderService extends IService<SubOrder> {
     BaseResponse add(SubOrder model);

     BaseResponse update(SubOrder model);



     PageDataResponse<SubOrder> findList(SubOrderParam pageParam);

     BaseResponse query(SubOrder model);

     List<SubOrder> queryList(SubOrderParam param);

     //----------   create by huangqi-----------


    /**
     * 查询特殊状态的退款订单,运营人员使用
     * @param param
     * @return
     */
    PageDataResponse<SubOrder> findListSpecial(SubOrderParam param);
    /**
     * 查询判断订单是否已经存在
     * @param orderId
     * @return
     */
    BaseResponse<Integer> orderCount(String orderId);

    /**
     * 插入有赞订单
     * @param order
     * @return
     */
    BaseResponse<Boolean> insertOrder(CreateOrderRequest order);

    /**
     * 处理已经完成和已关闭交易的订单
     * @param order
     * @return
     */
    BaseResponse<Boolean> updateOrder(CreateOrderRequest order);

    /**
     * 有赞订单确认收货
     * @param order
     * @return
     */
    BaseResponse<Boolean> confirmReceipt(CreateOrderRequest order);

    /**
     * 根据有赞id查询所有分订单
     * @param orderId
     * @return
     */
    BaseResponse<List<SubOrder>> getByYouZan(String orderId);

    BaseResponse test();
}
