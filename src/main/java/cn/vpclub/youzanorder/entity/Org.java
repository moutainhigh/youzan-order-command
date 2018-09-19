package cn.vpclub.youzanorder.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *  机构表
 */
@Data
@TableName("org")
public class Org extends Model<Org> {

    /**
     * 主键id
     */
    private Long id;

    /**
     * code 清结算编码
     */
    private String code;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
