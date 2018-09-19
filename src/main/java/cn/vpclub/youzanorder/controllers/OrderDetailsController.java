package cn.vpclub.youzanorder.controllers;

import cn.vpclub.moses.core.enums.ReturnCodeEnum;
import cn.vpclub.moses.core.model.response.BackResponseUtil;
import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.utils.common.AccessExcelUtil;
import cn.vpclub.moses.utils.common.StringUtil;
import cn.vpclub.youzanorder.common.LogisticsData;
import cn.vpclub.youzanorder.entity.OrderDetails;
import cn.vpclub.youzanorder.service.ILogisticsDataService;
import cn.vpclub.youzanorder.service.IOrderDetailsService;
import cn.vpclub.youzanorder.service.impl.LogisticsDataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyingdong on 2018/5/18.
 */
@RestController
@RequestMapping("/orderDetails")
@Slf4j
@AllArgsConstructor
public class OrderDetailsController {
    @Autowired
    private IOrderDetailsService iOrderDetailsService;
    @Autowired
    private ILogisticsDataService iLogisticsDataService;


    /**
     * 生成excel表格并将查询的数据导入,然后下载
     *
     * @return
     */
    @GetMapping(value = "/exportExcel")
    public BaseResponse exportExcel(@RequestParam(required = false) String outerItemId, @RequestParam(required = false) String startTime,
                                    @RequestParam(required = false) String endTime, HttpServletResponse response) throws IOException {
//        request.setCharacterEncoding("UTF-8");
        log.info("OrderDetailsController exportExcel, outerItemId: {},startTime: {},endTime: {}", outerItemId, startTime, endTime);

        BaseResponse baseResponse = null;
        //以下为生成Excel操作
        // 1.创建一个workbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 2.在workbook中添加一个sheet，对应Excel中的一个sheet
        HSSFSheet sheet = wb.createSheet("sheet1");
        //由于物流公司字节超过255，需要新建隐藏sheet
//        HSSFSheet hidden = wb.createSheet("hidden");
        //获取物流公司列表
//        BaseResponse<List<LogisticsOrg>> logisticsOrgs = iLogisticsDataService.getYouZanLogOrgs();
//        List<LogisticsOrg> orgslist=logisticsOrgs.getDataInfo();
//        String[] strings = new String[orgslist.size()];
//        // 遍历集合 获取下拉列表数据id
//        for (int i = 0; i < orgslist.size(); i++) {
//            strings[i] = String.valueOf(orgslist.get(i).getId());
//        }
        String[] strings = {"EMS", "EMS经济快递", "中通快递", "国通快递", "圆通速递", "天天快递", "德邦快递", "快捷速递", "百世快递", "邮政平邮", "邮政快递包裹", "韵达快递", "顺丰速运", "申通快递", "宅急送"};
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(strings);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(0, 5000, 11, 11);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation_list);
        // 3.在sheet中添加表头第0行，老版本poi对excel行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 4.创建单元格，设置值表头，设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        // 居中格式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置表头
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("子订单编号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("主订单编号");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("商品id");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("商品标题");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("商品价格");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("购买数量");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("商品规格描述");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("收货人名称");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("收货人联系电话");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("收货人地址");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("买家备注");
        cell.setCellStyle(style);

        cell = row.createCell(11);
        cell.setCellValue("快递公司名称");
        cell.setCellStyle(style);

        cell = row.createCell(12);
        cell.setCellValue("物流单号");
        cell.setCellStyle(style);

        //查询未发货的商品
        log.info(">>>查询未发货商品");
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        String yunyingId = "999224252890615810";//运营账号的id
        if (yunyingId.equals(outerItemId) && StringUtil.isEmpty(startTime) && StringUtil.isEmpty(endTime)) {
            //非商家，没有选择时间段
            log.info("非商家，没有选择时间段");
            orderDetailsList = iOrderDetailsService.queryNoDeliverProductNoItemIdAndTime();
        } else if (StringUtil.isEmpty(outerItemId) && StringUtil.isEmpty(startTime) && StringUtil.isEmpty(endTime)) {
            //管理账号，什么都不传。查询所有
            orderDetailsList = iOrderDetailsService.queryNoDeliverProductNoItemIdAndTime();
        } else if (yunyingId.equals(outerItemId)) {
            //非商家，选了时间段
            log.info("非商家，选了时间段");
            orderDetailsList = iOrderDetailsService.queryNoDeliverProductNoItemIdByTime(startTime, endTime);
        } else if (!yunyingId.equals(outerItemId) && StringUtil.isEmpty(startTime) && StringUtil.isEmpty(endTime)) {
            //商家，没选时间段
            log.info("商家，没选时间段");
            orderDetailsList = iOrderDetailsService.queryNoDeliverProductNoTimeByItemId(outerItemId);
        } else if (yunyingId.equals(outerItemId) && StringUtil.isNotEmpty(startTime) && StringUtil.isEmpty(endTime)) {
            //非商家，选了开始时间，没选结束时间，那默认结束时间为当前时间
            endTime = String.valueOf(System.currentTimeMillis());
            orderDetailsList = iOrderDetailsService.queryNoDeliverProductNoItemIdByTime(startTime, endTime);
        } else if (!yunyingId.equals(outerItemId)&&StringUtil.isNotEmpty(outerItemId) && StringUtil.isNotEmpty(startTime) && StringUtil.isEmpty(endTime)) {
            //商家，选了开始时间，没选结束时间，那默认结束时间为当前时间
            endTime = String.valueOf(System.currentTimeMillis());
            orderDetailsList = iOrderDetailsService.queryNoDeliverProductByItemIdAndTime(outerItemId, startTime, endTime);
        } else if (StringUtil.isEmpty(outerItemId) && StringUtil.isNotEmpty(startTime) && StringUtil.isEmpty(endTime)) {
            //管理账号，选了开始时间，没选结束时间，那默认结束时间为当前时间
            log.info("管理账号，选了开始时间，没选结束时间");
            endTime = String.valueOf(System.currentTimeMillis());
            orderDetailsList = iOrderDetailsService.queryNoDeliverProductNoItemIdByTime(startTime, endTime);
        } else {
            //商家，选了时间段
            log.info("商家，选了时间段");
            orderDetailsList = iOrderDetailsService.queryNoDeliverProductByItemIdAndTime(outerItemId, startTime, endTime);
        }

        if (CollectionUtils.isNotEmpty(orderDetailsList)) {
            // 循环将数据写入Excel
            for (int i = 0; i < orderDetailsList.size(); i++) {
                row = sheet.createRow((int) i + 1);
                OrderDetails list = orderDetailsList.get(i);
                // 创建单元格，设置值
                row.createCell(0).setCellValue(list.getSubOrderId());
                row.createCell(1).setCellValue(list.getBaseOrderId());

                row.createCell(2).setCellValue(list.getOid());

                row.createCell(3).setCellValue(list.getTitle());

                row.createCell(4).setCellValue(list.getPrice());

                row.createCell(5).setCellValue(list.getNum());

                row.createCell(6).setCellValue(list.getSkuPropertiesName());

                row.createCell(7).setCellValue(list.getName());

                row.createCell(8).setCellValue(list.getPhone());

                row.createCell(9).setCellValue(list.getProvince() + list.getCity() + list.getAddress());
                row.createCell(10).setCellValue(list.getBuyerMessage());

            }
        }

        //设置列表宽度
        sheet.autoSizeColumn((short) 0); //调整第0列默认宽度
        sheet.setDefaultColumnWidth(15);
        sheet.autoSizeColumn((short) 1); //调整第1列默认宽度
        sheet.setDefaultColumnWidth(15);
        sheet.autoSizeColumn((short) 2); //调整第2列默认宽度
        sheet.setDefaultColumnWidth(15);
        sheet.autoSizeColumn((short) 3); //调整第3列宽度
        sheet.setDefaultColumnWidth(40);
        sheet.autoSizeColumn((short) 4); //调整第4列宽度
        sheet.setDefaultColumnWidth(18);
        sheet.autoSizeColumn((short) 5); //调整第5列宽度
        sheet.setDefaultColumnWidth(18);
        sheet.autoSizeColumn((short) 6); //调整第6列宽度
        sheet.setDefaultColumnWidth(40);
        sheet.autoSizeColumn((short) 7); //调整第7列宽度
        sheet.setDefaultColumnWidth(15);
        sheet.autoSizeColumn((short) 8); //调整第8列宽度
        sheet.setDefaultColumnWidth(15);
        sheet.autoSizeColumn((short) 9); //调整第9列宽度
        sheet.setDefaultColumnWidth(110);
        sheet.setDefaultColumnWidth(15);//如不设置宽度则默认是15
        HSSFDataFormat df = wb.createDataFormat(); // 此处设置数据格式
        style.setDataFormat(df.getBuiltinFormat("#,#0"));//数据格式只显示整数


        log.info(">>>将数据写入Excel完成");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        // 设置response参数，可以打开下载页面
        //清空输出流
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String fileName = "未发货订单表.xls";
        response.addHeader("Content-Disposition",
                "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
        response.setCharacterEncoding("UTF-8");

        ServletOutputStream outputstream = response.getOutputStream();  //取得输出流
        os.writeTo(outputstream);
        os.flush();//写到输出流
        os.close();     //关闭

        return baseResponse;
    }

    /**
     * 导入excel
     *
     * @return
     */
    @PostMapping(value = "/importExcel")
    public BaseResponse importExcel(@RequestParam MultipartFile file) throws IOException {
        log.info("导入excel");
        if (file == null) {
            return BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1006.getCode());
        }
        BaseResponse baseResponse = null;
        //解析excel
        List<OrderDetails> orderDetailsList = this.parseExcel(file);
        log.info("验证前有效数据SIZE ：" + orderDetailsList.size() + "条");
        //遍历验证集合里的有效数据
        for (int i = orderDetailsList.size() - 1; i >= 0; i--) {
            if (StringUtil.isEmpty(orderDetailsList.get(i).getLogisticsNumber()) || StringUtil.isEmpty(orderDetailsList.get(i).getLogisticsCompanyName())) {
                orderDetailsList.remove(i);
            }
        }
        log.info("验证后有效数据SIZE ：" + orderDetailsList.size() + "条");
        if (CollectionUtils.isEmpty(orderDetailsList)) {
            baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1005.getCode());
            baseResponse.setMessage("数据不能为空");
        }
        List<LogisticsData> logisticsData = new ArrayList<>(1000);

        //遍历集合
        for (int i = 0; i < orderDetailsList.size(); i++) {
            LogisticsData data = new LogisticsData();
            OrderDetails orderDetails = orderDetailsList.get(i);
            data.setOrderId(orderDetails.getSubOrderId());
            data.setBaseOrderId(orderDetails.getBaseOrderId());
            data.setLogId(orderDetails.getLogisticsNumber());//物流单号
            //根据物流公司名称查询物流id
            Map<Long, String> map = LogisticsDataService.getOrgMap();
            Long logOrgId = null;
            for (Map.Entry<Long, String> entry : map.entrySet()) {
                if (orderDetails.getLogisticsCompanyName().equals(entry.getValue())) {
                    logOrgId = entry.getKey();
                }
            }
            data.setLogOrgId(String.valueOf(logOrgId));
            List<String> oid = java.util.Arrays.asList(orderDetails.getOid().split(","));
            data.setOidList(oid);
            logisticsData.add(data);
        }
        log.info("请求有赞发货数据size" + logisticsData.size() + "条");
        if (CollectionUtils.isNotEmpty(logisticsData)) {
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    log.info("开始执行发送logisticsData");
                    iLogisticsDataService.updateLogisticsBatch(logisticsData);
                }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
        }

        baseResponse = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1000.getCode());
        baseResponse.setMessage("上传成功");
        return baseResponse;
    }


    private List<OrderDetails> parseExcel(MultipartFile file) throws IOException {
        //需解析的字段集合
        List<String> orderDetailsList = new ArrayList<String>();
        orderDetailsList.add("subOrderId");//将表格的第一列赋值给这个
        orderDetailsList.add("baseOrderId");
        orderDetailsList.add("oid");
        orderDetailsList.add("title");
        orderDetailsList.add("price");
        orderDetailsList.add("num");
        orderDetailsList.add("skuPropertiesName");
        orderDetailsList.add("name");
        orderDetailsList.add("phone");
        orderDetailsList.add("address");
        orderDetailsList.add("buyerMessage");
        orderDetailsList.add("logisticsCompanyName");
        orderDetailsList.add("logisticsNumber");
        return AccessExcelUtil.parseExcel(file.getInputStream(), OrderDetails.class, orderDetailsList,
                1, 0, 0);

    }


}


