package cn.vpclub.youzanorder.entity.pay.xmlObject;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@XmlRootElement( name = "apijsuss" )
public class apijsuss {
    private apiheader apiheader;
    private apicontent apicontent;
    Map apisecurity=new HashMap();
}
