package cn.vpclub.youzanorder.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by zhangyingdong on 2018/5/18.
 * 订单信息包装类
 */
@ToString
public class OrderDetails extends Model<OrderDetails> {

    //子订单编号
    private String subOrderId;
    //主订单编号
    private String baseOrderId;
    //商品id
    private String oid;
    //商品标题
    private String title;
    //商品价格
    private String price;
    //购买数量
    private Long num;
    //商品规格描述
    private String skuPropertiesName;
    //收货人名称
    private String name;
    //收货人联系电话
    private String phone;
    private String province;
    private String city;
    private String buyerMessage;
    //收货人地址
    private String address;

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    //快递公司名称
    private String logisticsCompanyName;
    //物流单号
    private String logisticsNumber;


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBaseOrderId() {
        return baseOrderId;
    }

    public void setBaseOrderId(String baseOrderId) {
        this.baseOrderId = baseOrderId;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getSkuPropertiesName() {
        return skuPropertiesName;
    }

    public void setSkuPropertiesName(String skuPropertiesName) {
        this.skuPropertiesName = skuPropertiesName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogisticsCompanyName() {
        return logisticsCompanyName;
    }

    public void setLogisticsCompanyName(String logisticsCompanyName) {
        this.logisticsCompanyName = logisticsCompanyName;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
