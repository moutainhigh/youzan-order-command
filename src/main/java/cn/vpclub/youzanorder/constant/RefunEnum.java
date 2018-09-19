package cn.vpclub.youzanorder.constant;

/**
 * 退款申请相关
 */
public class RefunEnum {

    //退款状态
    public static String SAFE_NEW = "SAFE_NEW";//买家发起
    public static String SAFE_HANDLED = "SAFE_HANDLED";//被拒绝后再次发起
    public static String SAFE_REJECT = "SAFE_REJECT";//卖家拒绝
    public static String SAFE_ACCEPT = "SAFE_ACCEPT";//卖家接受
    public static String SAFE_SEND = "SAFE_SEND";//买家发货
    public static String SAFE_NO_RECEIVE = "SAFE_NO_RECEIVE";//卖家没有收到货
    public static String SAFE_INVOLVED = "SAFE_INVOLVED";//客服介入
    public static String SAFE_CLOSE = "SAFE_CLOSE";//维权关闭
    public static String SAFE_FINISHED = "SAFE_FINISHED";//维权结束

    //退款类型
    public static byte REFUND_ONLY=1;//仅退款
    public static byte REFUND_AND_RETURN=2;//退款退货

    //商品的退款申请状态
    public class ProductStatus{
        public static final byte NO_REFUND=0;//未申请
        public static final byte WAIT_HANDLE=1;//待处理
        public static final byte HANDLE_YES=2;//同意
        public static final byte HANDLE_NO=3;//拒绝
        public static final byte HANDLE_OVER=4;//维权结束
    }

    //订单的退款申请状态
    public class OrderStatus{
       public static  final byte WAIT_HANDLE=1;//待处理
       public static  final byte HANDLE_DONE=2;//已处理
       public static  final byte HANDLE_MANUAL=3;//运营处理
       public static  final byte HANDLE_CLOSE=4;//维权结束
    }
    //退款原因
    public enum Reason {
        //仅退款，未收到货申请原因：
        REFUND_QUALITY("质量问题"),
        REFUND_BUYWRONG("拍错/多拍/不喜欢"),
        REFUND_INCONFORMITY("商品描述不符"),
        REFUND_FAKE("假货"),
        REFUND_SENDWRONG("商家发错货"),
        REFUND_GOODSLESS("商品破损/少件"),
        REFUND_OTHER("其他"),
        //仅退款，已收到货申请原因：
        RETURNSNOT_BUYWRONG("多买/买错/不想要"),
        RETURNSNOT_NULLEXPRESS("快递无记录"),
        RETURNSNOT_GOODSLESS("少货/空包裹"),
        RETURNSNOT_NOTEXPRESS_ONTIME("未按约定时间发货"),
        RETURNSNOT_NOTRECEIVE("快递一直未送达"),
        RETURNSNOT_OTHER("快递一直未送达"),
        //退货退款，申请原因：
        RETURNS_GOODSLESS("商品破损/少件"),
        RETURNS_SENDWRONG("商家发错货"),
        RETURNS_INCONFORMITY("商品描述不符"),
        RETURNS_BUYWRONG("拍错/多拍/不喜欢"),
        RETURNS_QUALITY("质量问题"),
        RETURNS_FAKE("假货"),RETURNS_OTHER("其他");

        private String desc;

        Reason(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;

        }

        public static String getDesc(String name) {
            try {
                Reason enu = valueOf(name);
                return enu.getDesc();
            } catch (Exception e) {
                return null;
            }
        }
    }
}
