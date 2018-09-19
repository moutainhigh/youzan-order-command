package cn.vpclub.youzanorder.common;

import java.io.Serializable;
import java.util.List;

public class YouZanOrder implements Serializable {

    String orderId;
    String tradeOrderId;
    List<String> productId;
    String logId;
    String logOrgId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeOrderId() {
        return tradeOrderId;
    }

    public void setTradeOrderId(String tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
    }

    public List<String> getProductId() {
        return productId;
    }

    public void setProductId(List<String> productId) {
        this.productId = productId;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getLogOrgId() {
        return logOrgId;
    }

    public void setLogOrgId(String logOrgId) {
        this.logOrgId = logOrgId;
    }

    @Override
    public String toString() {
        return "YouZanOrder{" +
                "orderId='" + orderId + '\'' +
                ", tradeOrderId='" + tradeOrderId + '\'' +
                ", productId=" + productId +
                ", logId='" + logId + '\'' +
                ", logOrgId='" + logOrgId + '\'' +
                '}';
    }
}
