package cn.vpclub.youzanorder.common;

import java.io.Serializable;
import java.util.Map;

public class YouZanData implements Serializable {
    private String tradeOrderId;
    private Map<String,String> product;
    private Map<String,Long> courier;

    public String getTradeOrderId() {
        return tradeOrderId;
    }

    public void setTradeOrderId(String tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
    }

    public Map<String, String> getProduct() {
        return product;
    }

    public void setProduct(Map<String, String> product) {
        this.product = product;
    }

    public Map<String, Long> getCourier() {
        return courier;
    }

    public void setCourier(Map<String, Long> courier) {
        this.courier = courier;
    }
}
