package cn.vpclub.youzanorder.service;

import cn.vpclub.youzanorder.entity.OrderDetails;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * Created by zhangyingdong on 2018/5/18.
 */
public interface IOrderDetailsService extends IService<OrderDetails> {
    public List<OrderDetails> queryNoDeliverProductByItemIdAndTime(String outerItemId,String startTime,String endTime);
    public List<OrderDetails> queryNoDeliverProductNoItemIdByTime(String startTime,String endTime);
    public List<OrderDetails> queryNoDeliverProductNoItemIdAndTime();
    public List<OrderDetails> queryNoDeliverProductNoTimeByItemId(String outerItemId);

}
