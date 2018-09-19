package cn.vpclub.youzanorder.service.impl;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.youzanorder.entity.OrderDetails;
import cn.vpclub.youzanorder.mapper.OrderDetailsMapper;
import cn.vpclub.youzanorder.service.IOrderDetailsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyingdong on 2018/5/18.
 */
@Service
@Slf4j
public class OrderDetailsServiceImpl extends ServiceImpl<OrderDetailsMapper, OrderDetails> implements IOrderDetailsService {

    public OrderDetailsServiceImpl() {
        super();
    }

    public OrderDetailsServiceImpl(OrderDetailsMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public List<OrderDetails> queryNoDeliverProductByItemIdAndTime(String outerItemId,String startTime,String endTime) {
        List<OrderDetails> orderDetailsList = null;
        orderDetailsList = baseMapper.queryNoDeliverProductByItemIdAndTime(outerItemId,startTime,endTime);
        if (CollectionUtils.isEmpty(orderDetailsList)) {
            log.info("查询失败");
        }
        return orderDetailsList;
    }

    @Override
    public List<OrderDetails> queryNoDeliverProductNoItemIdByTime(String startTime,String endTime) {

        List<OrderDetails> orderDetailsList = null;
        orderDetailsList = baseMapper.queryNoDeliverProductNoItemIdByTime(startTime,endTime);
        if (CollectionUtils.isEmpty(orderDetailsList)) {
            log.info("查询失败");
        }
        return orderDetailsList;
    }

    @Override
    public List<OrderDetails> queryNoDeliverProductNoItemIdAndTime() {
        List<OrderDetails> orderDetailsList = null;
        orderDetailsList = baseMapper.queryNoDeliverProductNoItemIdAndTime();
        if (CollectionUtils.isEmpty(orderDetailsList)) {
            log.info("查询失败");
        }
        return orderDetailsList;
    }

    @Override
    public List<OrderDetails> queryNoDeliverProductNoTimeByItemId(String outerItemId) {
        List<OrderDetails> orderDetailsList = null;
        orderDetailsList = baseMapper.queryNoDeliverProductNoTimeByItemId(outerItemId);
        if (CollectionUtils.isEmpty(orderDetailsList)) {
            log.info("查询失败");
        }
        return orderDetailsList;
    }


}
