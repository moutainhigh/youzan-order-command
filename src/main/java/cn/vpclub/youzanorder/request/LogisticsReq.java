package cn.vpclub.youzanorder.request;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LogisticsReq implements Serializable {

    //有赞订单号
    private String youZanOrderId;
    //有赞商品id集合
    private List<String> oidList;

}
