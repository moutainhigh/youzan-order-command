package cn.vpclub.youzanorder.utils;

import cn.vpclub.moses.utils.web.HttpRequestUtil;

public class HttpUtils{

    /**
     * 发送json的POST请求
     * @param url 地址
     * @param json 数据
     * @param timeout 超时时间(毫秒)
     * @return
     */
    public static String JsonPost(String url,String json,int timeout){
        return HttpRequestUtil.sendPost(url, json, "application/json", timeout, timeout);
    }


    /**
     * @param param   "telephone=18687175493&content=测试短信"
     * @param timeout 毫秒
     * @return
     */
    public static String FormPost(String url,String param,int timeout){
        return HttpRequestUtil.sendPost(url,param,"application/x-www-form-urlencoded",timeout,timeout);
    }
}
