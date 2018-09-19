package cn.vpclub.youzanorder.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 支付的属性
 */
@Component
@Data
public class PayProperties {

    //接口地址
    @Value("${pay.payServerUrl}")
    private String serverUrl;


    @Override
    public String toString() {
        return "PayProperties{" +
                "serverUrl='" + serverUrl + '\'' +
                '}';
    }
}
