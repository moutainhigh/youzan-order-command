package cn.vpclub.youzanorder.service.impl;

import cn.vpclub.moses.core.enums.ReturnCodeEnum;
import cn.vpclub.moses.core.model.response.BackResponseUtil;
import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.core.model.response.ResponseConvert;
import cn.vpclub.moses.utils.common.StringUtil;
import cn.vpclub.youzanorder.constant.RefunEnum;
import cn.vpclub.youzanorder.entity.ProductInfo;
import cn.vpclub.youzanorder.entity.RefundInfo;
import cn.vpclub.youzanorder.entity.SubOrder;
import cn.vpclub.youzanorder.mapper.ProductInfoMapper;
import cn.vpclub.youzanorder.service.IRefundInfoService;
import cn.vpclub.youzanorder.service.ProductInfoService;
import cn.vpclub.youzanorder.utils.BaseUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by chentao on 2018/4/27.
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper,ProductInfo> implements ProductInfoService {

    @Autowired
    private IRefundInfoService refundInfoService;


    public ProductInfoServiceImpl(){
        super();
    }

    public ProductInfoServiceImpl(ProductInfoMapper basicMapper){
        this.baseMapper = basicMapper;
    }

    @Override
    public BaseResponse add(ProductInfo model) {
        boolean back = this.insert(model);
        BaseResponse baseResponse = ResponseConvert.convert(back);
        return baseResponse;
    }

    @Override
    public BaseResponse update(ProductInfo model) {
        BaseResponse baseResponse ;
        if (null == model || null == model.getId()) {
            baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1006.getCode());
        } else {
            boolean back = this.updateById(model);
            baseResponse = ResponseConvert.convert(back);
        }
        return baseResponse;
    }

    @Override
    public List<ProductInfo> queryList(Map<String, Object> param) {
        List<ProductInfo> list = this.baseMapper.queryList(param);
        //处理商品的退款信息
        if (!CollectionUtils.isEmpty(list)) {
            for (ProductInfo pr : list) {
                if(pr.getRefundStatus()!=RefunEnum.ProductStatus.NO_REFUND){
                    EntityWrapper<RefundInfo> ew = new EntityWrapper<>();
                    ew.eq("product_id", pr.getId());
                    List<RefundInfo> reList = refundInfoService.selectAdvance(ew).getDataInfo();
                    if(!CollectionUtils.isEmpty(reList)){
                        pr.setRefunDetail(reList.get(0));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public BaseResponse updateProductInfo(ProductInfo model) {
        BaseResponse baseResponse = new BaseResponse();
        int i = this.baseMapper.updateProductInfo(model);
        if (i > 0){
            baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1000.getCode());
        }
        return baseResponse;
    }

    @Override
    public BaseResponse<Boolean> updateAdvance(ProductInfo model, EntityWrapper<ProductInfo> wp) {
        if (model == null || wp == null) {
            return BaseUtil.response(false,false,"参数为空");
        }
        baseMapper.update(model, wp);
        return BaseUtil.response(true,true,"更新成功");
    }


    @Override
    public BaseResponse<Integer> countAdvance(EntityWrapper<ProductInfo> wp) {
        if (wp == null) {
            return BaseUtil.response(false, 0, "参数为空");
        }
        Integer i = baseMapper.selectCount(wp);
        return BaseUtil.response(true, i, "查询成功");
    }

    @Override
    public BaseResponse<List<ProductInfo>> getListBySubId(List<Long> subIds) {
        if (CollectionUtils.isEmpty(subIds)) {
            return BaseUtil.response(false, null, "参数为空");
        }
        EntityWrapper<ProductInfo> pw = new EntityWrapper<>();
        pw.in("sub_order_id", subIds);
        List<ProductInfo> list = baseMapper.selectList(pw);
        return BaseUtil.response(true, list, "查询成功");
    }
}
