package cn.vpclub.youzanorder.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("seller_info")
public class SellerInfo {

    private Long id;
    @TableField("org_name")
    private String orgName;
    private String name;
    private String phone;
}
