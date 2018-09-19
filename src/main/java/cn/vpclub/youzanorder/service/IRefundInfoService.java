package cn.vpclub.youzanorder.service;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.youzanorder.entity.RefundInfo;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 记录每个商品的退款申请 服务类
 * </p>
 *
 * @author chenwei
 * @since 2018-05-23
 */
public interface IRefundInfoService extends IService<RefundInfo> {
     BaseResponse add(RefundInfo model);

     BaseResponse delete(RefundInfo model);

     BaseResponse update(RefundInfo model);

     BaseResponse query(RefundInfo model);

     BaseResponse<List<RefundInfo>> selectAdvance(EntityWrapper<RefundInfo> ew);

}
