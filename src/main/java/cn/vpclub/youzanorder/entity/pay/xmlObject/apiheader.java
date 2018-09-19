package cn.vpclub.youzanorder.entity.pay.xmlObject;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Accessors(chain = true)
@XmlRootElement( name = "apiheader" )
public class apiheader {

    String apiid;
    String applicationid;
    String businessdate;
    String userid;
    String userpassword;
    String operationtime;
    String paymentdate;
    String requestjournal;
    String securitylevel;
    String signaturemethod;
    String ipaddr;//ip

    //返回值
    String responsetype;
    String responsecode;
    String responsemessage;
}