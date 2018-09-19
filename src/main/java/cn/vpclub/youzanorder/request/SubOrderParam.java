package cn.vpclub.youzanorder.request;

import cn.vpclub.moses.core.model.request.PageBaseSearchParam;
import lombok.Data;

/**
 * Created by chentao on 2018/4/24.
 */
@Data
public class SubOrderParam extends PageBaseSearchParam {
    /**
     * 商户id
     */
    private String outerItemId;

    private String id;

    private String phone;

    private String stateStr;

    private String baseOrderId;

    private String startTime;

    private String endTime;

}
