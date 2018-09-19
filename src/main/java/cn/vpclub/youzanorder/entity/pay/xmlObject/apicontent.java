package cn.vpclub.youzanorder.entity.pay.xmlObject;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@Accessors(chain = true)
@XmlRootElement(name = "apicontent")
public class apicontent {
    private String totordno;//总订单编号
    private String totorddesc;//订单描述
    private String serviceid;//业务编号
//    private String period;//有效期数量
//    private String periodunit;//有效期单位
    private int totordamount;//总订单金额
    private int totpayamount;//总订单支付金额
    private int totplatamount;//总订单平台营销金额
    private int totmerchantamount;//总订单商户营销金额
    private String usertype;//用户类型
    private String userid;//用户登录名
//    private String pageurl;//页面通知地址
//    private String notifyurl;//总订单商户营销金额
//    private String ticketpayflag;//电子券支付标识
    private String ticketaccount;//指定电子券账户（和包电子券商户编号）
    private String totordnum;//订单个数
//    private String sessionid;//支付令牌
//    private String refundjnl;//流水号

    //支付回调
//    private String payjnl;//流水号
//    private String corporg;//支付方式
    private String orderstatus;//SUCCESS 成功,FAILED 失败
    // 退款
    private String merchid;//清算平台中的编号
    private String businesid;//业务平台中的商户编号
    private String orderno;//这是退款使用的子订单号
    private int totalamount;//退款总额
    private int platamount;//平台营销金额
    private int cashamount;//退款现金金额
    private int merchantamount;//商户营销金额
    private String paydate;//退款日期
    private String paytime;//退款时间
    private String refundstatus;//退款状态 SUCCESS 成功 FAILED 失败
    /*CMPAY（和包手机支付）
    ALIPAY （支付宝）
    HB（花呗分期）
    WEC	（微信）
    SPDB	（浦发信用卡）
    HBD （和包贷）
    BT（中移金服分期）
    CYD（闯易贷）
    TICKET（电子券）*/
    private String paycnl;//现金支付渠道
    private String paydirect;//直接支付

    //免密支付为1
    private String provplatform;

    //是否需要验证码支付 1需要 0不需要
    private String needverify;

    private List<order> order;
}