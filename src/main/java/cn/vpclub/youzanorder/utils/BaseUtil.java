package cn.vpclub.youzanorder.utils;

import cn.vpclub.moses.core.enums.ReturnCodeEnum;
import cn.vpclub.moses.core.model.request.PageBaseSearchParam;
import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.youzanorder.entity.SubOrder;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BaseUtil {

    /**
     * 判断标准的返回值是否是正确
     *
     * @param base
     * @return
     */
    public static boolean isSucceed(BaseResponse base) {
        if (base != null && base.getReturnCode() != null && base.getReturnCode() == ReturnCodeEnum.CODE_1000.getCode()) {
            return true;
        }
        return false;
    }

    /**
     * 封装返回值
     * @param succeed 是否成功
     * @param t       返回的对象
     * @param message 返回信息
     * @return
     */
    public static <T> BaseResponse<T> response(boolean succeed, T t, String message) {
        BaseResponse<T> res = new BaseResponse<>();
        res.setDataInfo(t);
        res.setMessage(message);
        if (succeed) {
            res.setReturnCode(ReturnCodeEnum.CODE_1000.getCode());
        } else {
            res.setReturnCode(ReturnCodeEnum.CODE_1005.getCode());
        }
        return res;
    }



    /**
     * 设置页码信息,也可以直接使用 com.baomidou.mybatisplus.plugins.Page
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T extends PageBaseSearchParam> T pageParam(T t, Class<T> cl) {
        if (t == null) {
            try {
                t = cl.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if (t.getPageNumber() == null) {
            t.setPageNumber(1);
        }
        if (t.getPageSize() == null) {
            t.setPageSize(10);
        }
        return t;
    }
}
