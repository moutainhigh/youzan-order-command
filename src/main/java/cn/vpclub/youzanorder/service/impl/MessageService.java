package cn.vpclub.youzanorder.service.impl;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.utils.common.StringUtil;
import cn.vpclub.youzanorder.entity.SellerInfo;
import cn.vpclub.youzanorder.service.IMessageService;
import static cn.vpclub.youzanorder.utils.BaseUtil.*;

import cn.vpclub.youzanorder.service.ISellerInfoService;
import cn.vpclub.youzanorder.utils.HttpUtils;
import cn.vpclub.youzanorder.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MessageService implements IMessageService {

    public static final String profile=  System.getenv().get("SPRING_PROFILES_ACTIVE");
    @Autowired
    private ISellerInfoService sellerInfoService;

    @Value("${system.url.smsSend}")
    private String smsUrl;

    @Override
    public BaseResponse<Boolean> sendSMS(String content, String phone) {
        StringBuilder bd = new StringBuilder();
        bd.append("telephone=").append(phone).append("&").append("content=").append(content);
        try {
            if("prod".equals(profile)) {//生产环境才发送
                String s = HttpUtils.FormPost(smsUrl, bd.toString(), 2000);
                String status = JsonUtil.getJsonParam("status", s);
                if ("success".equals(status)) {
                    return response(true, true, "发送成功");
                } else {
                    String msg = JsonUtil.getJsonParam("msg", s);
                    log.info("短信失败:" + msg);
                    log.info("短信失败:" + bd.toString());
                    return response(false, false, msg);
                }
            }else{
                return response(false, false, "非生产环境不发送");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return response(false, false, "短信接口报错");
        }
    }

    @Override
    public BaseResponse<Boolean> sendSellerSMS(String outerItemId, String content) {
        if (StringUtil.isEmpty(outerItemId) || StringUtil.isEmpty(content)) {
            response(false, false, "参数为空");
        }
        //查询商家电话
        SellerInfo seller = sellerInfoService.getById(outerItemId).getDataInfo();
        if (seller != null) {
            sendSMS(content, seller.getPhone());
        }
        return response(false,false,"商家信息不存在");
    }
}
