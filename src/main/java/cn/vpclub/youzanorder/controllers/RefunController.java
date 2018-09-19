package cn.vpclub.youzanorder.controllers;

import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.web.controller.AbstractController;
import cn.vpclub.youzanorder.request.RefunPay;
import cn.vpclub.youzanorder.request.RefunResult;
import cn.vpclub.youzanorder.service.IRefunService;
import cn.vpclub.youzanorder.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理退款相关逻辑的控制器
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/refun")
public class RefunController extends AbstractController {

    private IRefunService refunService;

    /**
     * 处理有赞的退款信息
     * @return
     */
    @PostMapping(value = "/eventMsg")
    public BaseResponse eventMsg(@RequestBody String json) {
        log.info("退款推送json:"+json);
        RefunPay msg = JsonUtil.turnObj(json, RefunPay.class);
        BaseResponse<Boolean> result = refunService.handleRefun(msg);
        return result;
    }

    /**
     * 退款款申请的处理接口
     * @param req
     * @return
     */
    @PostMapping(value = "/result")
    public BaseResponse result(@RequestBody RefunResult req) {
        BaseResponse<Boolean> result = refunService.refunRespon(req);
        return result;
    }
}
