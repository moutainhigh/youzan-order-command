package cn.vpclub.youzanorder.entity.pay.xmlObject;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Accessors(chain = true)
@XmlRootElement( name = "order" )
public class order {
    /**
     * 商户编号
     */
    private String merchid;
    /**
     * 业务平台中的商户编号
     */
    private String businesid;
    /**
     * 子订单编号
     */
    private String orderno;
    /**
     * 子订单总额
     */
    private int totalamount;
    /**
     * 子订单支付金额
     */
    private int payamount;
    /**
     * 子订单业务平台营销金额
     */
    private int platamount;
    /**
     * 子订单商户营销金额
     */
    private int merchantamount;
    /**
     * 商品类型
     */
    private String productionmold;

}
