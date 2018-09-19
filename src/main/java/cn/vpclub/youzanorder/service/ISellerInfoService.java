package cn.vpclub.youzanorder.service;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.youzanorder.entity.RefundInfo;
import cn.vpclub.youzanorder.entity.SellerInfo;
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
public interface ISellerInfoService extends IService<SellerInfo> {
     /**
      * 根据outerItemId查询商家信息
      * @param id
      * @return
      */
     BaseResponse<SellerInfo> getById(String id);

}
