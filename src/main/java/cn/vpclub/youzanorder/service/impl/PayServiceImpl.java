package cn.vpclub.youzanorder.service.impl;

import cn.vpclub.moses.core.enums.ReturnCodeEnum;
import cn.vpclub.moses.core.model.response.BackResponseUtil;
import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.youzanorder.constant.PayConstant;
import cn.vpclub.youzanorder.constant.PayProperties;
import cn.vpclub.youzanorder.entity.pay.xmlObject.*;
import cn.vpclub.youzanorder.entity.pay.PayRequest;
import cn.vpclub.youzanorder.entity.pay.SubOrderDto;
import cn.vpclub.youzanorder.mapper.OrgMapper;
import cn.vpclub.youzanorder.entity.Org;
import cn.vpclub.youzanorder.service.PayService;
import cn.vpclub.youzanorder.utils.HttpClientUtil;
import cn.vpclub.youzanorder.utils.PayUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.hisun.crypt.mac.CryptUtilImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class PayServiceImpl extends ServiceImpl<OrgMapper,Org> implements PayService {

    private PayProperties payProperties;

    @Override
    public BaseResponse sendSynMSG(PayRequest request) throws JsonProcessingException {

        CryptUtilImpl cryptUtil = new CryptUtilImpl();
        //当前操作时间
        Date opTimeDate = new Date();
        XmlMapper xmlMapper = new XmlMapper();
        String eventSN = PayUtil.getTransID("ekzf", opTimeDate);
        String thisTime = org.apache.commons.lang3.time.DateFormatUtils
                .format(opTimeDate, "yyyyMMddHHmmss");

        //渠道秘钥-统一用彩云渠道
        String signkey = PayConstant.QJS_CHANNEL_PWD_CYYP;
        signkey += thisTime;

        //用户密码加密
        String userpassword = cryptUtil.cryptDes(signkey, signkey);
        //组装报文xml
        apijsuss aj = new apijsuss();
        apiheader header = new apiheader();
        apicontent content = new apicontent();

        header.setUserid(PayConstant.QJS_CHANNEL_CYYP)
                .setUserpassword(userpassword)
                .setApiid(PayConstant.PAY_APPID)
                .setApplicationid("445")
                .setOperationtime(thisTime)
                .setBusinessdate(thisTime.substring(0, 8))
                .setRequestjournal(eventSN)
                .setSecuritylevel("1")
                .setSignaturemethod("MD5");

        //content
        content.setTotordno(request.getOrderNo().toString())
                .setTotorddesc("")
                .setServiceid(header.getUserid())
//                .setPeriod("60")
//                .setPeriodunit("3")
//                .setPayjnl(eventSN)
//                .setPaycnl("ALIPAY,WEC,CMPAY")
                .setTotordamount(request.getTotalAmount())
                .setTotpayamount(request.getTotalAmount())
                .setTotalamount(request.getTotalAmount())
                .setTotplatamount(0)
                .setTotmerchantamount(0)
                .setUsertype(request.getUserType())
                .setUserid(request.getUserId())
//                .setPageurl(payProperties.getPageUrl() + "/" + request.getOrderNo()) //支付跳转页面
//                .setNotifyurl(payProperties.getPayCallBackURL())
//                .setTicketpayflag("1")
                .setTotordnum(String.valueOf(request.getSubList().size()));

        //子订单
        order o = null;
        //子订单集合
        List<order> orderList = new ArrayList<order>();
        for (SubOrderDto sub : request.getSubList()) {
            o = new order();
            Org orgg =this.selectById(sub.getStoreId());
            log.debug("查询机构编码:"+orgg.getCode());
            o.setMerchid(orgg.getCode())
                    .setBusinesid(sub.getStoreId())
                    .setOrderno(String.valueOf(sub.getSubMerchOrderNo()))
                    .setTotalamount(sub.getPayAmount())
                    .setPayamount(sub.getPayAmount())
                    .setPlatamount(0)
                    .setMerchantamount(0)
                    .setProductionmold("01");
            orderList.add(o);
        }
        if (CollectionUtils.isNotEmpty(orderList)) {
            content.setOrder(orderList);
        }
        aj.setApiheader(header);
        aj.setApicontent(content);
        String signatureValue = PayUtil.getSignatureValueByString(xmlMapper.writeValueAsString(header),
                xmlMapper.writeValueAsString(content).replace("<order><order>", "<order>").replace("</order></order>",
                        "</order>"), signkey);
        Map apisecurity = new HashMap();
        apisecurity.put("signaturevalue", signatureValue);
        aj.setApisecurity(apisecurity);

        String xml = xmlMapper.writeValueAsString(aj);
        String context=xml.replace("<order><order>", "<order>").replace("</order></order>", "</order>");
        BaseResponse response = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1000.getCode());
        try {
            log.info("支付下单明文:" + context);
            response = HttpClientUtil.postHttp(payProperties.getServerUrl(), context, "GBK");
            log.info("清结算返回:" + response.getReturnCode() + ",返回明文:" + response.getDataInfo().toString());
        } catch (Exception e) {
            log.error("请求支付请求接口出错:{}", e);
            response.setReturnCode(ReturnCodeEnum.CODE_1007.getCode());
        }

        return response;
    }


}
