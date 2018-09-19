package cn.vpclub.youzanorder.service;


import cn.vpclub.moses.core.model.response.BaseResponse;

/**
 * 用于发送短信
 */
public interface IMessageService {


    /**
     * 发送短信
     * @param content
     * @param phone
     * @return
     */
    BaseResponse<Boolean> sendSMS(String content, String phone);


    /**
     * 向商家发送短信
     * @param outerItemId
     * @param content
     * @return
     */
    BaseResponse<Boolean> sendSellerSMS(String outerItemId, String content);
}
