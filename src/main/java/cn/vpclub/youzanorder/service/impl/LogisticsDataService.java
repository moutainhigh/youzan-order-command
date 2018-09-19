package cn.vpclub.youzanorder.service.impl;

import cn.vpclub.moses.core.enums.ReturnCodeEnum;
import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.utils.common.StringUtil;
import cn.vpclub.youzanorder.common.LogisticsData;
import cn.vpclub.youzanorder.common.LogisticsOrg;
import cn.vpclub.youzanorder.common.YouZanData;
import cn.vpclub.youzanorder.constant.ConfigConstants;
import cn.vpclub.youzanorder.entity.ProductInfo;
import cn.vpclub.youzanorder.entity.SubOrder;
import cn.vpclub.youzanorder.mapper.SubOrderMapper;
import cn.vpclub.youzanorder.request.LogisticsReq;
import cn.vpclub.youzanorder.service.ILogisticsDataService;
import cn.vpclub.youzanorder.service.ProductInfoService;
import cn.vpclub.youzanorder.service.SubOrderService;
import cn.vpclub.youzanorder.utils.BaseUtil;
import cn.vpclub.youzanorder.utils.HttpUtils;
import cn.vpclub.youzanorder.utils.JsonUtil;
import cn.vpclub.youzanorder.utils.vaild.DataValidUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
public class LogisticsDataService implements ILogisticsDataService {

    public static List<LogisticsOrg> orgList;
    private static Map<Long, String> orgMap = new HashMap<>(256);
    @Value("${system.url.youZanLogicOrg}")
    private String youZanOrg;

    @Value("${system.url.youZanLogicNum}")
    private String youZanLogstic;

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private SubOrderService subOrderService;

    @Autowired
    private SubOrderMapper subOrderMapper;

    static {
        String orgs = "[{\"display\":1,\"name\":\"申通快递\",\"id\":1},{\"display\":1,\"name\":\"圆通速递\",\"id\":2},{\"display\":1,\"name\":\"中通快递\",\"id\":3},{\"display\":1,\"name\":\"韵达快递\",\"id\":4},{\"display\":1,\"name\":\"天天快递\",\"id\":5},{\"display\":1,\"name\":\"百世快递\",\"id\":6},{\"display\":1,\"name\":\"顺丰速运\",\"id\":7},{\"display\":1,\"name\":\"邮政快递包裹\",\"id\":8},{\"display\":0,\"name\":\"EMS经济快递\",\"id\":10},{\"display\":1,\"name\":\"EMS\",\"id\":11},{\"display\":0,\"name\":\"邮政平邮\",\"id\":12},{\"display\":0,\"name\":\"德邦快递\",\"id\":13},{\"display\":1,\"name\":\"联昊通\",\"id\":16},{\"display\":1,\"name\":\"全峰快递\",\"id\":17},{\"display\":1,\"name\":\"全一快递\",\"id\":18},{\"display\":1,\"name\":\"城市100\",\"id\":19},{\"display\":1,\"name\":\"汇强快递\",\"id\":20},{\"display\":1,\"name\":\"广东EMS\",\"id\":21},{\"display\":1,\"name\":\"速尔\",\"id\":22},{\"display\":1,\"name\":\"飞康达速运\",\"id\":23},{\"display\":1,\"name\":\"宅急送\",\"id\":25},{\"display\":1,\"name\":\"联邦快递\",\"id\":27},{\"display\":1,\"name\":\"德邦物流\",\"id\":28},{\"display\":1,\"name\":\"中铁快运\",\"id\":30},{\"display\":1,\"name\":\"信丰物流\",\"id\":31},{\"display\":1,\"name\":\"龙邦速递\",\"id\":32},{\"display\":1,\"name\":\"天地华宇\",\"id\":33},{\"display\":1,\"name\":\"快捷速递\",\"id\":34},{\"display\":1,\"name\":\"新邦物流\",\"id\":36},{\"display\":1,\"name\":\"能达速递\",\"id\":37},{\"display\":1,\"name\":\"优速快递\",\"id\":38},{\"display\":1,\"name\":\"国通快递\",\"id\":40},{\"display\":1,\"name\":\"其他\",\"id\":41},{\"display\":0,\"name\":\"顺丰快递\",\"id\":42},{\"display\":1,\"name\":\"AAE\",\"id\":43},{\"display\":1,\"name\":\"安信达\",\"id\":44},{\"display\":1,\"name\":\"百福东方\",\"id\":45},{\"display\":1,\"name\":\"BHT\",\"id\":46},{\"display\":1,\"name\":\"邦送物流\",\"id\":47},{\"display\":1,\"name\":\"传喜物流\",\"id\":48},{\"display\":1,\"name\":\"大田物流\",\"id\":49},{\"display\":1,\"name\":\"D速快递\",\"id\":50},{\"display\":1,\"name\":\"递四方\",\"id\":51},{\"display\":0,\"name\":\"飞康达物流\",\"id\":52},{\"display\":1,\"name\":\"飞快达\",\"id\":53},{\"display\":1,\"name\":\"如风达\",\"id\":54},{\"display\":1,\"name\":\"风行天下\",\"id\":55},{\"display\":1,\"name\":\"飞豹快递\",\"id\":56},{\"display\":1,\"name\":\"港中能达\",\"id\":57},{\"display\":1,\"name\":\"广东邮政\",\"id\":58},{\"display\":1,\"name\":\"共速达\",\"id\":59},{\"display\":1,\"name\":\"汇通快运\",\"id\":60},{\"display\":0,\"name\":\"华宇物流\",\"id\":61},{\"display\":1,\"name\":\"恒路物流\",\"id\":62},{\"display\":1,\"name\":\"华夏龙\",\"id\":63},{\"display\":1,\"name\":\"海航天天\",\"id\":64},{\"display\":1,\"name\":\"海盟速递\",\"id\":65},{\"display\":1,\"name\":\"华企快运\",\"id\":66},{\"display\":1,\"name\":\"山东海红\",\"id\":67},{\"display\":1,\"name\":\"佳吉物流\",\"id\":68},{\"display\":1,\"name\":\"佳怡物流\",\"id\":69},{\"display\":1,\"name\":\"加运美\",\"id\":70},{\"display\":1,\"name\":\"京广速递\",\"id\":71},{\"display\":1,\"name\":\"急先达\",\"id\":72},{\"display\":1,\"name\":\"晋越快递\",\"id\":73},{\"display\":1,\"name\":\"捷特快递\",\"id\":74},{\"display\":1,\"name\":\"金大物流\",\"id\":75},{\"display\":1,\"name\":\"嘉里大通\",\"id\":76},{\"display\":1,\"name\":\"康力物流\",\"id\":77},{\"display\":1,\"name\":\"跨越物流\",\"id\":78},{\"display\":1,\"name\":\"龙邦物流\",\"id\":79},{\"display\":1,\"name\":\"蓝镖快递\",\"id\":80},{\"display\":1,\"name\":\"隆浪快递\",\"id\":81},{\"display\":1,\"name\":\"门对门\",\"id\":82},{\"display\":1,\"name\":\"明亮物流\",\"id\":83},{\"display\":1,\"name\":\"全晨快递\",\"id\":84},{\"display\":1,\"name\":\"全际通\",\"id\":85},{\"display\":1,\"name\":\"全日通\",\"id\":86},{\"display\":0,\"name\":\"如风达快递\",\"id\":87},{\"display\":1,\"name\":\"三态速递\",\"id\":88},{\"display\":1,\"name\":\"盛辉物流\",\"id\":89},{\"display\":0,\"name\":\"速尔物流\",\"id\":90},{\"display\":1,\"name\":\"盛丰物流\",\"id\":91},{\"display\":1,\"name\":\"上大物流\",\"id\":92},{\"display\":1,\"name\":\"赛澳递\",\"id\":94},{\"display\":1,\"name\":\"圣安物流\",\"id\":95},{\"display\":1,\"name\":\"穗佳物流\",\"id\":96},{\"display\":1,\"name\":\"优速物流\",\"id\":97},{\"display\":1,\"name\":\"万家物流\",\"id\":98},{\"display\":1,\"name\":\"万象物流\",\"id\":99},{\"display\":1,\"name\":\"新蛋奥硕物流\",\"id\":100},{\"display\":1,\"name\":\"香港邮政\",\"id\":101},{\"display\":1,\"name\":\"运通快递\",\"id\":102},{\"display\":1,\"name\":\"远成物流\",\"id\":103},{\"display\":1,\"name\":\"亚风速递\",\"id\":104},{\"display\":1,\"name\":\"一邦速递\",\"id\":105},{\"display\":1,\"name\":\"源伟丰快递\",\"id\":106},{\"display\":1,\"name\":\"元智捷诚\",\"id\":107},{\"display\":1,\"name\":\"越丰物流\",\"id\":108},{\"display\":1,\"name\":\"源安达\",\"id\":109},{\"display\":1,\"name\":\"原飞航\",\"id\":110},{\"display\":1,\"name\":\"忠信达快递\",\"id\":111},{\"display\":1,\"name\":\"芝麻开门\",\"id\":112},{\"display\":1,\"name\":\"银捷速递\",\"id\":113},{\"display\":1,\"name\":\"中邮物流\",\"id\":114},{\"display\":1,\"name\":\"中速快件\",\"id\":115},{\"display\":1,\"name\":\"中天万运\",\"id\":116},{\"display\":1,\"name\":\"河北建华\",\"id\":117},{\"display\":1,\"name\":\"乐捷递\",\"id\":118},{\"display\":1,\"name\":\"立即送\",\"id\":119},{\"display\":1,\"name\":\"通和天下\",\"id\":120},{\"display\":1,\"name\":\"微特派\",\"id\":121},{\"display\":1,\"name\":\"一统飞鸿\",\"id\":122},{\"display\":1,\"name\":\"郑州建华\",\"id\":123},{\"display\":1,\"name\":\"山西红马甲\",\"id\":125},{\"display\":1,\"name\":\"陕西黄马甲\",\"id\":126},{\"display\":1,\"name\":\"快速递\",\"id\":127},{\"display\":1,\"name\":\"安能物流\",\"id\":128},{\"display\":1,\"name\":\"新顺丰\",\"id\":129},{\"display\":1,\"name\":\"钱报速运\",\"id\":130},{\"display\":1,\"name\":\"日日顺\",\"id\":131},{\"display\":1,\"name\":\"神盾快运\",\"id\":132},{\"display\":1,\"name\":\"京华亿家\",\"id\":133},{\"display\":1,\"name\":\"南方传媒物流\",\"id\":134},{\"display\":1,\"name\":\"成都商报物流\",\"id\":135},{\"display\":1,\"name\":\"冻到家物流\",\"id\":136},{\"display\":1,\"name\":\"亚马逊物流\",\"id\":137},{\"display\":1,\"name\":\"京东快递\",\"id\":138},{\"display\":1,\"name\":\"e邮宝\",\"id\":139},{\"display\":1,\"name\":\"思迈\",\"id\":140},{\"display\":1,\"name\":\"UPS\",\"id\":141},{\"display\":1,\"name\":\"南京100\",\"id\":142},{\"display\":1,\"name\":\"民航快递\",\"id\":143},{\"display\":1,\"name\":\"贝海国际速递\",\"id\":144},{\"display\":1,\"name\":\"CJ物流\",\"id\":145},{\"display\":1,\"name\":\"央广购物\",\"id\":146},{\"display\":1,\"name\":\"易时联国际速递\",\"id\":147},{\"display\":1,\"name\":\"风先生\",\"id\":148},{\"display\":1,\"name\":\"耀启物流\",\"id\":149},{\"display\":1,\"name\":\"内蒙EMS\",\"id\":150},{\"display\":1,\"name\":\"小红帽\",\"id\":151},{\"display\":1,\"name\":\"PCA\",\"id\":152},{\"display\":1,\"name\":\"诚义物流\",\"id\":153},{\"display\":1,\"name\":\"秦远国际物流\",\"id\":154},{\"display\":1,\"name\":\"万家康快递\",\"id\":155},{\"display\":1,\"name\":\"澳邮中国快运\",\"id\":156},{\"display\":1,\"name\":\"一号线国际速递\",\"id\":157},{\"display\":1,\"name\":\"EWE国际物流\",\"id\":158},{\"display\":1,\"name\":\"爱送配送\",\"id\":159},{\"display\":1,\"name\":\"POSTNZ\",\"id\":160},{\"display\":1,\"name\":\"FASTGO\",\"id\":161},{\"display\":1,\"name\":\"天越物流\",\"id\":162},{\"display\":1,\"name\":\"德中物流\",\"id\":163},{\"display\":1,\"name\":\"行必达\",\"id\":164},{\"display\":1,\"name\":\"EFS快递\",\"id\":165},{\"display\":1,\"name\":\"中邮速递\",\"id\":166},{\"display\":1,\"name\":\"一号仓\",\"id\":167},{\"display\":1,\"name\":\"速通达跨境物流\",\"id\":168},{\"display\":1,\"name\":\"五亨国际\",\"id\":170},{\"display\":1,\"name\":\"迅物流\",\"id\":171},{\"display\":1,\"name\":\"中环国际_澳洲\",\"id\":172},{\"display\":1,\"name\":\"美仓快递\",\"id\":173},{\"display\":1,\"name\":\"澳通速递\",\"id\":174},{\"display\":1,\"name\":\"济南猎豹速递\",\"id\":175},{\"display\":1,\"name\":\"澳运速递\",\"id\":176},{\"display\":1,\"name\":\"优达生鲜\",\"id\":177},{\"display\":1,\"name\":\"P2UEXPRESS\",\"id\":178},{\"display\":1,\"name\":\"黑猫宅急便\",\"id\":179},{\"display\":1,\"name\":\"快客快运\",\"id\":180},{\"display\":1,\"name\":\"当当物流\",\"id\":181},{\"display\":1,\"name\":\"百世快运\",\"id\":182},{\"display\":1,\"name\":\"艾瑞斯远\",\"id\":183},{\"display\":1,\"name\":\"PCAExpress\",\"id\":184},{\"display\":1,\"name\":\"斑马物联网\",\"id\":186},{\"display\":1,\"name\":\"泛捷国际速运\",\"id\":187},{\"display\":1,\"name\":\"黄马甲快递\",\"id\":188},{\"display\":1,\"name\":\"蓝天快递\",\"id\":189},{\"display\":1,\"name\":\"银河物流\",\"id\":190},{\"display\":1,\"name\":\"海龟国际速运\",\"id\":191},{\"display\":1,\"name\":\"申通国际\",\"id\":192},{\"display\":1,\"name\":\"安鲜达\",\"id\":193},{\"display\":1,\"name\":\"闪送\",\"id\":194},{\"display\":1,\"name\":\"我的物流\",\"id\":195},{\"display\":1,\"name\":\"黑狗快递\",\"id\":196},{\"display\":1,\"name\":\"富腾达快递\",\"id\":197},{\"display\":1,\"name\":\"程光快递\",\"id\":198},{\"display\":1,\"name\":\"吉祥邮\",\"id\":199},{\"display\":1,\"name\":\"天时海淘转运\",\"id\":200},{\"display\":1,\"name\":\"澳大利亚AOL快递\",\"id\":201},{\"display\":1,\"name\":\"亿翔快递\",\"id\":202},{\"display\":1,\"name\":\"中澳国际物流\",\"id\":203},{\"display\":1,\"name\":\"全速快递\",\"id\":204},{\"display\":1,\"name\":\"极客快递\",\"id\":205},{\"display\":1,\"name\":\"金岸物流\",\"id\":206},{\"display\":1,\"name\":\"中联速运\",\"id\":207},{\"display\":1,\"name\":\"e邮客\",\"id\":208},{\"display\":1,\"name\":\"天马迅达\",\"id\":209},{\"display\":1,\"name\":\"DHL\",\"id\":220},{\"display\":1,\"name\":\"中铁物流\",\"id\":221},{\"display\":1,\"name\":\"美淘优递\",\"id\":222},{\"display\":1,\"name\":\"澳速物流\",\"id\":223},{\"display\":1,\"name\":\"转运中国\",\"id\":224},{\"display\":1,\"name\":\"安能快递\",\"id\":225},{\"display\":1,\"name\":\"华美国际快递\",\"id\":226},{\"display\":1,\"name\":\"佳慧尔快递\",\"id\":227},{\"display\":1,\"name\":\"西邮寄\",\"id\":228},{\"display\":1,\"name\":\"九曳供应链\",\"id\":229},{\"display\":1,\"name\":\"速必达希杰物流\",\"id\":230},{\"display\":1,\"name\":\"盛辉物流\",\"id\":231},{\"display\":1,\"name\":\"虎跃快运\",\"id\":232},{\"display\":1,\"name\":\"品骏快递\",\"id\":233},{\"display\":1,\"name\":\"狂派速递\",\"id\":234},{\"display\":1,\"name\":\"永利八达通\",\"id\":235},{\"display\":1,\"name\":\"特急送\",\"id\":236},{\"display\":1,\"name\":\"捷安达国际速递\",\"id\":237},{\"display\":1,\"name\":\"C&C全球快递\",\"id\":238},{\"display\":1,\"name\":\"全时达快递\",\"id\":239},{\"display\":1,\"name\":\"香港萬威\",\"id\":240},{\"display\":1,\"name\":\"小蜜蜂邮包\",\"id\":241},{\"display\":1,\"name\":\"长江国际速递\",\"id\":242},{\"display\":1,\"name\":\"环球速运\",\"id\":243},{\"display\":1,\"name\":\"笨鸟国际\",\"id\":244},{\"display\":1,\"name\":\"澳洲极地快递\",\"id\":245},{\"display\":1,\"name\":\"汇通天下\",\"id\":246},{\"display\":1,\"name\":\"晟邦物流\",\"id\":247},{\"display\":1,\"name\":\"速购速递\",\"id\":248},{\"display\":1,\"name\":\"易客满\",\"id\":249},{\"display\":1,\"name\":\"中通快运\",\"id\":250},{\"display\":1,\"name\":\"方舟国际速递\",\"id\":251},{\"display\":1,\"name\":\"新西兰邮政\",\"id\":252},{\"display\":1,\"name\":\"壹米滴答\",\"id\":253},{\"display\":1,\"name\":\"金社裕农物流\",\"id\":254},{\"display\":1,\"name\":\"安世通\",\"id\":255},{\"display\":1,\"name\":\"耀飞快递\",\"id\":256},{\"display\":1,\"name\":\"报通快递\",\"id\":257},{\"display\":1,\"name\":\"一运全成\",\"id\":258},{\"display\":1,\"name\":\"神马快递\",\"id\":259},{\"display\":1,\"name\":\"伊藤配送\",\"id\":260},{\"display\":1,\"name\":\"优邦速运\",\"id\":261},{\"display\":1,\"name\":\"一速递\",\"id\":262},{\"display\":1,\"name\":\"怡佳转运\",\"id\":263}]";
        orgList = JsonUtil.turnObjList(orgs, LogisticsOrg.class);
        if (!CollectionUtils.isEmpty(orgList)) {
            for (LogisticsOrg org : orgList) {
                orgMap.put(org.getId(), org.getName());
            }
        }
    }

    public static Map<Long, String> getOrgMap() {
        return orgMap;
    }

    @Override
    public BaseResponse<List<LogisticsOrg>> getYouZanLogOrgs() {
        BaseResponse<List<LogisticsOrg>> res = new BaseResponse<>();
        res.setDataInfo(orgList);
        res.setReturnCode(ReturnCodeEnum.CODE_1000.getCode());
        res.setMessage("查询成功");
        return res;
    }

    @Override
    public BaseResponse<Boolean> updateOrderLogs(String orderId, String tradeOrderId, List<String> productId,
                                                 String logId, String logOrgId) {
        BaseResponse<Boolean> res = new BaseResponse<>();
        if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(tradeOrderId) ||
                CollectionUtils.isEmpty(productId) || StringUtils.isEmpty(logId) || StringUtils.isEmpty(logOrgId)) {
            return BaseUtil.response(false, false, "参数为空");
        }
        String youzanRes = null;//有赞回复的内容
        Long logOrg_id = Long.valueOf(logOrgId);
        YouZanData data = new YouZanData();
        try {// 请求有赞
            Map<String, String> map = new HashMap<>();
            Map<String, Long> map1 = new HashMap<>();
            //多个商品对应一个物流单号
            for (String product : productId) {
                map.put(product, logId);
                map1.put(product, logOrg_id);
            }
            //封装有赞的数据格式
            data.setTradeOrderId(tradeOrderId);
            data.setProduct(map);
            data.setCourier(map1);
            String str = JsonUtil.turnJson(data);
            youzanRes = HttpUtils.JsonPost(youZanLogstic, str, 10000);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("发货请求:" + JsonUtil.turnJson(data));
            log.info("发货失败" + youzanRes);
            return BaseUtil.response(false, false, "物流更新失败,请稍后重试");
        }
        try {
            if (JsonUtil.isYouZanError(youzanRes)) {
                //解析错误原因
                String message = JsonUtil.getJsonParam("message", youzanRes);
                if (!(message.indexOf("订单已经发过货") > -1)) {
                    log.info("发货请求:" + JsonUtil.turnJson(data));
                    log.info("发货失败" + youzanRes);
                    String[] split = message.split("返回错误原因:");
                    String msgJson = split[1];
                    String msg = JsonUtil.getJsonParam("msg", msgJson);
//                      String showMsg = split[0] + msg;
                    return BaseUtil.response(false, false, msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("有赞回复格式错误" + youzanRes);
            return BaseUtil.response(false, false, "物流更新失败,请稍后重试");
        }
        Long subOrderId = Long.valueOf(orderId);
        Long dateTime = System.currentTimeMillis();
        //把订单的商品修改成发货中状态
        EntityWrapper<ProductInfo> pw = new EntityWrapper<>();
        pw.eq("sub_order_id", subOrderId).in("oid", productId);
        ProductInfo produ = new ProductInfo();
        produ.setUpdatedTime(dateTime);
        produ.setLogisticsCompanyId(logOrgId);//物流公司id
        produ.setLogisticsNumber(logId);//物流单号
        produ.setLogisticsCompanyName(orgMap.get(logOrg_id));//物流公司名称
        produ.setStateStr(ConfigConstants.PENDING_RECEIVED);//待收货
        produ.setDeliverStatus(ConfigConstants.DELIVERED);//已发货
        productInfoService.updateAdvance(produ, pw);
        //如果所有商品都已发货,就更新订单状态
        EntityWrapper<ProductInfo> pw1 = new EntityWrapper<>();
        pw1.eq("sub_order_id", subOrderId).ne("deliver_status", ConfigConstants.DELIVERED);
        BaseResponse<Integer> resData = productInfoService.countAdvance(pw1);
        if (BaseUtil.isSucceed(resData) && resData.getDataInfo() == 0) {
            SubOrder subOrder = new SubOrder();
            subOrder.setId(subOrderId);
            subOrder.setDeliverStatus(ConfigConstants.DELIVERED);//发货状态 已发货
            subOrder.setStateStr(ConfigConstants.PENDING_RECEIVED);//订单状态 待收货
            subOrder.setUpdatedTime(dateTime);
            subOrderService.updateById(subOrder);
        }
        return BaseUtil.response(true, true, "更新成功");
    }


    @Override
    public BaseResponse<Boolean> updateLogisticsBatch(List<LogisticsData> batch) {
        //数据校验
        if (CollectionUtils.isEmpty(batch)) {
            return BaseUtil.response(false, false, "参数为空");
        }
        for (int i = 0; i < batch.size(); i++) {
            DataValidUtils.Result result = DataValidUtils.validation(batch.get(i));
            if (!result.isSuc()) {
                return BaseUtil.response(false, false, "第" + i + 1 + "条数据:" + result.getMsg());
            }
        }
        List<String> subOrderIds = new ArrayList<>(1000);
        Long dateTime = System.currentTimeMillis();
        //封装有赞的数据格式
        //region 循环调用有赞接口
        ProductInfo produ = new ProductInfo();
        for (LogisticsData data : batch) {
            String logreq = null;
            String youzanRes = null;//有赞回复的内容
            Long logOrg_id = Long.valueOf(data.getLogOrgId());
            //多个商品对应一个物流单号
            Map<String, String> map = new HashMap<>();
            Map<String, Long> map1 = new HashMap<>();
            for (String product : data.getOidList()) {
                map.put(product, data.getLogId());
                map1.put(product, logOrg_id);
            }
            Map<String, Object> youzanData = new HashMap<>();
            youzanData.put("tradeOrderId", data.getBaseOrderId());
            youzanData.put("product", map);
            youzanData.put("courier", map1);
            logreq = JsonUtil.turnJson(youzanData);
            try {// 请求有赞
                youzanRes = HttpUtils.JsonPost(youZanLogstic, logreq, 10000);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("批量物流失败请求:" + logreq);
                continue;
            }
            //region 更新商品物流
            try {
              if (JsonUtil.isYouZanError(youzanRes)) {
                    //解析错误原因
                    String message = JsonUtil.getJsonParam("message", youzanRes);
                    if (!(message.indexOf("订单已经发过货") > -1)) {
                        log.info("批量物流失败请求:" + message);
                        String[] split = message.split("返回错误原因:");
                        String msgJson = split[1];
                        String msg = JsonUtil.getJsonParam("msg", msgJson);
//                      String showMsg = split[0] + msg;
                        continue;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("物流回复格式错误" + youzanRes);
                continue;
            }
            //更新商品状态
            Long subOrderId = Long.valueOf(data.getOrderId());
            //把订单的商品修改成发货中状态
            EntityWrapper<ProductInfo> pw = new EntityWrapper<>();
            pw.eq("sub_order_id", subOrderId).in("oid", data.getOidList());
            produ.setUpdatedTime(dateTime);
            produ.setLogisticsNumber(data.getLogId());//物流单号
            produ.setLogisticsCompanyId(data.getLogOrgId());//物流公司id
            produ.setLogisticsCompanyName(orgMap.get(logOrg_id));//物流公司名称
            produ.setStateStr(ConfigConstants.PENDING_RECEIVED);//待收货
            produ.setDeliverStatus(ConfigConstants.DELIVERED);//已发货
            productInfoService.updateAdvance(produ, pw);
            //endregion
            //把成功请求的id储存下来
            subOrderIds.add(data.getOrderId());
        }
        //endregion
        //分批次update order状态
        if (!CollectionUtils.isEmpty(subOrderIds)) {
            int batchCount = 50;//50条记录一次更新
            List<String> tempList = new ArrayList<>(50);
            for (int i = 0; i < subOrderIds.size(); i++) {
                tempList.add(subOrderIds.get(i));
                if ((i + 1) % batchCount == 0 || (i + 1) == subOrderIds.size()) {
                    subOrderMapper.updateLogisticsStatusBatch(tempList);
                    tempList.clear();
                }
            }
        }
        log.info("批量物流处理完成");
        return BaseUtil.response(true,true,"操作成功");
    }
}
