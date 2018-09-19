package cn.vpclub.youzanorder.service;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.youzanorder.entity.Org;
import cn.vpclub.youzanorder.entity.pay.PayRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.baomidou.mybatisplus.service.IService;

public interface PayService extends IService<Org> {

    public BaseResponse sendSynMSG(PayRequest request) throws JsonProcessingException;

}
