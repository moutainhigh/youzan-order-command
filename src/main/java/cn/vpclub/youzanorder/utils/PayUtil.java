package cn.vpclub.youzanorder.utils;

import com.hisun.crypt.mac.CryptUtilImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Slf4j
public class PayUtil {


    /**
     * 调用移动支付平台的各类接口，并返回响应报文
     *
     * @param src     请求报文
     * @param reqPath 请求地址
     * @return 返回报文
     */
    public static String sendHttpRequest(String src, String reqPath, String requestProperty) throws MalformedURLException,
            IOException, ProtocolException, UnsupportedEncodingException {
        String result = "";
        src = allTrimStr(src);
        log.debug("发起明文:" + src);
        URL url = new URL(reqPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        if ("json".equals(requestProperty)) {
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        } else {
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(60000);// 设置连接超时 60秒
        conn.setReadTimeout(60000);// 设置读取超时60秒
        // 输出报文，支付平台要求gbk编码，避免乱码
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "gbk");
        osw.write(src);
        conn.connect();
        osw.flush();
        osw.close();
        int code = conn.getResponseCode();
        if (code == 200) {
            // 获取响应报文，避免乱码
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                result += inputLine + "\r\n";
            }
            br.close();
            System.out.println("返回:" + result);
            log.debug("200返回:" + result);
        } else {
            result = "接口调用失败";
        }
        conn.disconnect();
        log.debug("调用结束，返回码:" + code + "，返回信息:" + result);
        return result;
    }

    /**
     * 调用移动支付平台的各类接口，并返回响应报文
     *
     * @param document 请求报文
     * @param reqPath  请求地址
     * @return 返回报文
     */
    public static String sendHttpRequestByDoc(Document document, String reqPath) throws MalformedURLException, IOException,
            ProtocolException, UnsupportedEncodingException {
        String result = "";
        Element rootE = document.getRootElement();
        String rootSrc = rootE.asXML();
        rootSrc = allTrimStr(rootSrc);
        // 发起请求
        URL url = new URL(reqPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setConnectTimeout(60000);// 设置连接超时 60秒
        conn.setReadTimeout(60000);// 设置读取超时60秒
        // 输出报文，支付平台要求gbk编码，避免乱码
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "gbk");
        osw.write(rootSrc);
        conn.connect();
        osw.flush();
        osw.close();
        int code = conn.getResponseCode();
        if (code == 200) {
            // 获取响应报文，避免乱码
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                result += inputLine + "\r\n";
            }
            br.close();
        } else {
            result = "接口调用失败";
        }
        conn.disconnect();
        return result;
    }

    /**
     * 生成流水
     *
     * @param sDate
     * @return 前缀+8日期+8时间+2随机位 HHmmssSSS 转换成毫秒的范围是：0-86399999（8位）,不足补0
     */
    public static String getTransID(Date sDate) {
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String yyyyMMddHHmmssSSS = simple.format(sDate);
        String yyyyMMdd = yyyyMMddHHmmssSSS.substring(0, 8);
        int hh = Integer.parseInt(yyyyMMddHHmmssSSS.substring(8, 10));
        int mm = Integer.parseInt(yyyyMMddHHmmssSSS.substring(10, 12));
        int ss = Integer.parseInt(yyyyMMddHHmmssSSS.substring(12, 14));
        int sss = Integer.parseInt(yyyyMMddHHmmssSSS.substring(14));
        String hm = String.valueOf(((hh * 60 + mm) * 60 + ss) * 1000 + sss);//
        while (hm.length() < 8) {
            hm = "0" + hm;
        }
        Random rand = new Random();
        String strRand = String.valueOf(rand.nextInt(100));
        while (strRand.length() < 2) {
            strRand = "0" + strRand;
        }
        String transactionID = "YKSH" + yyyyMMdd + hm + strRand;

        return transactionID;
    }

    /**
     * 生成流水
     *
     * @param sDate
     * @return 前缀+8日期+8时间+2随机位 HHmmssSSS 转换成毫秒的范围是：0-86399999（8位）,不足补0
     * @frontStr 前缀
     */
    public static String getTransID(String frontStr, Date sDate) {
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String yyyyMMddHHmmssSSS = simple.format(sDate);
        String yyyyMMdd = yyyyMMddHHmmssSSS.substring(0, 8);
        int hh = Integer.parseInt(yyyyMMddHHmmssSSS.substring(8, 10));
        int mm = Integer.parseInt(yyyyMMddHHmmssSSS.substring(10, 12));
        int ss = Integer.parseInt(yyyyMMddHHmmssSSS.substring(12, 14));
        int sss = Integer.parseInt(yyyyMMddHHmmssSSS.substring(14));
        String hm = String.valueOf(((hh * 60 + mm) * 60 + ss) * 1000 + sss);//
        while (hm.length() < 8) {
            hm = "0" + hm;
        }
        Random rand = new Random();
        String strRand = String.valueOf(rand.nextInt(100));
        while (strRand.length() < 2) {
            strRand = "0" + strRand;
        }
        String transactionID = frontStr + yyyyMMdd + hm + strRand;

        return transactionID;
    }

    /**
     * 获取移动支付平台的数据签名
     */
    public static String getSignatureValue(Document document, String signkey) {
        CryptUtilImpl cryptUtil = new CryptUtilImpl();
        Element rootE = document.getRootElement();
        Element apicontentE = rootE.element("apicontent");
        Element apiheaderE = rootE.element("apiheader");
        String headContentSrc = apiheaderE.asXML() + apicontentE.asXML();
        System.out.println("headContentSrc:" + headContentSrc);
        headContentSrc = allTrimStr(headContentSrc);
        String signatureValue = cryptUtil.cryptMd5(headContentSrc, signkey);
        System.out.println("getSignatureValue:" + signatureValue);
        return signatureValue;
    }

    /**
     * 获取移动支付平台的数据签名
     */
    public static String getSignatureValueByString(String headStr, String contentStr, String signkey) {
        CryptUtilImpl cryptUtil = new CryptUtilImpl();
        String wholeStr = headStr + contentStr;
        wholeStr = allTrimStr(wholeStr);
        System.out.println("getSignatureValueByString {}:" + wholeStr);
        String signatureValue = cryptUtil.cryptMd5(wholeStr, signkey);
        return signatureValue;
    }


    /**
     * 移除所有换行和空格
     */
    public static String allTrimStr(String str) {
        str = StringUtils.remove(str, "\r");
        str = StringUtils.remove(str, "\n");
        str = StringUtils.remove(str, "\t");
        str = StringUtils.remove(str, "\b");
        return str;
    }

}
