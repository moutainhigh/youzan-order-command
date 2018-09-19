package cn.vpclub.youzanorder.mapper;

import cn.vpclub.youzanorder.entity.SubOrder;
import cn.vpclub.youzanorder.request.SubOrderParam;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by chentao on 2018/4/24.
 */
@Mapper
public interface SubOrderMapper extends BaseMapper<SubOrder> {

    List<SubOrder> findDataByPageParam(SubOrderParam pageParam);

    Integer findDataTotalByPageParam(SubOrderParam pageParam);

    /**
     * 批量修改订单的物流状态
     * @param ids id集合
     * @return
     */
    Integer updateLogisticsStatusBatch(@Param("ids") List<String> ids);
}
