package cn.vpclub.youzanorder.service;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.youzanorder.entity.ProductInfo;
import cn.vpclub.youzanorder.entity.SubOrder;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * Created by chentao on 2018/4/27.
 */
public interface ProductInfoService extends IService<ProductInfo> {

     BaseResponse add(ProductInfo model);

     BaseResponse update(ProductInfo model);

    List<ProductInfo> queryList(Map<String, Object> paramMap);

     BaseResponse updateProductInfo(ProductInfo model);

    /**
     * 高级升级
     * @param model
     * @param wp
     * @return
     */
    BaseResponse<Boolean> updateAdvance(ProductInfo model, EntityWrapper<ProductInfo> wp);

    /**
     * 高级计数
     * @param wp
     * @return
     */
    BaseResponse<Integer> countAdvance(EntityWrapper<ProductInfo> wp);

    /**
     *
     * @param subIds
     * @return
     */
    BaseResponse<List<ProductInfo>> getListBySubId(List<Long> subIds);
}
