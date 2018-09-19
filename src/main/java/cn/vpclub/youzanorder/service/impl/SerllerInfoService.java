package cn.vpclub.youzanorder.service.impl;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.utils.common.StringUtil;
import cn.vpclub.youzanorder.entity.SellerInfo;
import cn.vpclub.youzanorder.mapper.SellerInfoMapper;
import cn.vpclub.youzanorder.service.ISellerInfoService;
import cn.vpclub.youzanorder.utils.BaseUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SerllerInfoService extends ServiceImpl<SellerInfoMapper,SellerInfo> implements ISellerInfoService {

    public SerllerInfoService() {
        super();
    }

    public SerllerInfoService(SellerInfoMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public BaseResponse<SellerInfo> getById(String id) {
        if (StringUtil.isEmpty(id)) {
            return BaseUtil.response(false, null, "参数为空");
        }
        SellerInfo seller = this.baseMapper.selectById(Long.valueOf(id));
        return BaseUtil.response(true, seller, "查询成功");
    }
}
