package cn.vpclub.youzanorder.utils;


import cn.vpclub.youzanorder.entity.ProductInfo;
import cn.vpclub.youzanorder.entity.SubOrder;

/**
 * 用于封装各种msg
 */
public class MsgStrUtil {

    /**
     * 封装新订单消息
     * @param o
     * @param p
     * @return
     */
    public static String orderNewMsg(SubOrder o, ProductInfo p) {
        if (o == null || p == null) {
            return "";
        }
        int length = p.getTitle().length();
        length=length>=6?6:length;
        StringBuilder bd = new StringBuilder();
        bd.append("您有一笔新的订单，订单编号").append(o.getBaseOrderId()).append(",").append("商品金额")
                .append(p.getTotalFee()).append(",").append("客户购买商品“")
                .append(p.getTitle().substring(0,length)).append("...”，请24小时内安排发货。");
        return bd.toString();
    }

    /**
     * 封装退款信息消息
     * @param o
     * @param p
     */
    public static String orderRefunMsg(SubOrder o, ProductInfo p) {
        if (o == null || p == null) {
            return "";
        }
        int length = p.getTitle().length();
        length=length>=6?6:length;
        StringBuilder bd = new StringBuilder();
        bd.append("您有一笔退单申请，订单编号").append(o.getBaseOrderId()).append("退款金额").append(p.getRefundedFee())
        .append(",客户购买商品“").append(p.getTitle().substring(0,length)).append("...”，请24时内完成处理。");
        return bd.toString();
    }
}
