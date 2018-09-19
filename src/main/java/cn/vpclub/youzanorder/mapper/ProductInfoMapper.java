package cn.vpclub.youzanorder.mapper;

import cn.vpclub.youzanorder.entity.ProductInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by chentao on 2018/4/27.
 */
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {

    List<ProductInfo> queryList(Map<String, Object> param);

    int updateProductInfo(ProductInfo model);
}
