package cn.vpclub.youzanorder.request;


import cn.vpclub.youzanorder.utils.vaild.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class RefunResult implements Serializable {

   @NotNull(message = "productId为空")
   private String productId;
   @NotNull(message = "result为空")
   private Boolean result;//同意拒绝
//   @NotNull(message = "msg为空")
   private String msg;//备注

   private String address;//收货地址
   private String name;//收货人
   private String post;//收货邮编
   private String phone;//收货人电话

}
