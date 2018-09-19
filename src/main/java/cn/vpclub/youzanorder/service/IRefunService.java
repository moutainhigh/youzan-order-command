package cn.vpclub.youzanorder.service;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.youzanorder.request.RefunPay;
import cn.vpclub.youzanorder.request.RefunResult;

/**
 * 处理退款的相关接口
 */
public interface IRefunService {

    /**
     * 处理有赞转发来的标准数据
     * @param msg
     * @return
     */
    BaseResponse<Boolean> handleRefun(RefunPay msg);

    /**
     * 卖家对退款的处理
     * @param req productId 商品的主键
     * @param req result 同意或拒绝
     * @param req msg 卖家留言
     * @return
     */
    BaseResponse<Boolean> refunRespon(RefunResult req);
}
