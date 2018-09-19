package cn.vpclub.youzanorder.mapper;


import cn.vpclub.youzanorder.entity.OrderDetails;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhangyingdong on 2018/5/18.
 */
public interface OrderDetailsMapper extends BaseMapper<OrderDetails> {
    List<OrderDetails> queryNoDeliverProductByItemIdAndTime(@Param("outerItemId") String outerItemId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<OrderDetails> queryNoDeliverProductNoItemIdByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<OrderDetails> queryNoDeliverProductNoItemIdAndTime();

    List<OrderDetails> queryNoDeliverProductNoTimeByItemId(@Param("outerItemId") String outerItemId);


}
