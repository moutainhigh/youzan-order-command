package cn.vpclub.youzanorder.service.impl;

import cn.vpclub.moses.core.enums.ReturnCodeEnum;
import cn.vpclub.moses.core.model.response.BackResponseUtil;
import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.core.model.response.ResponseConvert;
import cn.vpclub.youzanorder.entity.RefundInfo;
import cn.vpclub.youzanorder.mapper.RefundInfoMapper;
import cn.vpclub.youzanorder.service.IRefundInfoService;
import cn.vpclub.youzanorder.utils.BaseUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

;import java.util.List;

/**
 * <p>
 * 记录每个商品的退款申请 服务实现类
 * </p>
 *
 * @author chenwei
 * @since 2018-05-23
 */
@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements IRefundInfoService {
    public RefundInfoServiceImpl() {
        super();
     }

    public RefundInfoServiceImpl(RefundInfoMapper baseMapper) {
        this.baseMapper = baseMapper;
     }
    /**
    * 添加方法
    * @return
    */
    @Override
    public BaseResponse add(RefundInfo model) {
        boolean back = this.insert(model);
        BaseResponse baseResponse = ResponseConvert.convert(back);
        return baseResponse;
        }
    /**
    * 删除
    * @return
    */
    @Override
    public BaseResponse delete(RefundInfo model) {
        BaseResponse baseResponse ;
        if (null == model || null == model.getId()) {
        baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1006.getCode());
        } else {
        boolean back = this.deleteById(model.getId());
        baseResponse = ResponseConvert.convert(back);
        }
        return baseResponse;
        }
    /**
    * 修改方法
    * @return
    */
    @Override
    public BaseResponse update(RefundInfo model) {
        BaseResponse baseResponse ;
        if (null == model || null == model.getId()) {
        baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1006.getCode());
        } else {
        boolean back = this.updateById(model);
        baseResponse = ResponseConvert.convert(back);
        }
        return baseResponse;
        }
    /**
    * 单个查询
    * @return
    */
    @Override
    public BaseResponse query(RefundInfo model) {
        BaseResponse baseResponse;
        if (null == model || null == model.getId()) {
        baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1006.getCode());
        } else {
        RefundInfo data = this.selectById(model.getId());
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
    public BaseResponse<List<RefundInfo>> selectAdvance(EntityWrapper<RefundInfo> ew) {
        List<RefundInfo> list = baseMapper.selectList(ew);
        return BaseUtil.response(true,list,"查询成功");
    }
}