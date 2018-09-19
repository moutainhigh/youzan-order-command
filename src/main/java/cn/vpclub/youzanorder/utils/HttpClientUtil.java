package cn.vpclub.youzanorder.utils;

import cn.vpclub.moses.core.enums.ReturnCodeEnum;
import cn.vpclub.moses.core.model.response.BackResponseUtil;
import cn.vpclub.moses.core.model.response.BaseResponse;
import cn.vpclub.moses.utils.common.JsonUtil;
import cn.vpclub.moses.utils.constant.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClientUtil {

    private static final int connectTimeout = 8000;
    private static PoolingHttpClientConnectionManager connManager = null;
    private static CloseableHttpClient httpclient = null;


    public static String postJsonBody(String url, Object object, String encoding) {
        HttpPost post = new HttpPost(url);

        try {
            post.setHeader("Content-type", "application/json");
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(8000)
                    .setConnectTimeout(8000)
                    .setConnectionRequestTimeout(8000)
                    .setExpectContinueEnabled(false).build();
            post.setConfig(requestConfig);
            String str1 = JsonUtil.objectToJson(object);
            post.setEntity(new StringEntity(str1, encoding));
            log.info("[HttpUtils Post] begin invoke url:" + url + " , params:" + str1);
            CloseableHttpResponse response = httpclient.execute(post);

            try {
                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        String str = EntityUtils.toString(entity, encoding);
                        log.info("[HttpUtils Post]Debug response, url :" + url + " , response string :" + str);
                        String var9 = str;
                        return var9;
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        } catch (UnsupportedEncodingException var30) {
            log.error("UnsupportedEncodingException", var30);
        } catch (Exception var31) {
            log.error("Exception", var31);
            throw new RuntimeException("请求超时");
        } finally {
            post.releaseConnection();
        }

        return "";
    }

    public static BaseResponse postHttp(String urlStr, String str, String encoding) {

        BaseResponse response = BackResponseUtil.getBaseResponse(ReturnCodeEnum.CODE_1000.getCode());

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(60000);// 设置连接超时 60秒
            conn.setReadTimeout(60000);// 设置读取超时60秒
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), encoding);
            osw.write(str);

            log.info("[HttpUtils Post] begin invoke url:" + urlStr + " , params:" + str);
            conn.connect();
            osw.flush();
            osw.close();

            int code = conn.getResponseCode();
            response.setReturnCode(code);
            String result = "";

            if (code == 200) {

                // 获取响应报文，避免乱码
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), SystemConstant.UTF8));

                //逐行读取
                String inputLine;
                while (StringUtils.isNotEmpty(inputLine = br.readLine())) {
                    result += inputLine;
                }

                br.close();
                response.setReturnCode(ReturnCodeEnum.CODE_1000.getCode());
                response.setDataInfo(result);
            }

        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException", e);
            response.setReturnCode(ReturnCodeEnum.CODE_1005.getCode());
            response.setMessage("不支持的字符集编码");
        } catch (Exception e) {
            log.error("Exception", e);
            response.setReturnCode(ReturnCodeEnum.CODE_1007.getCode());
            response.setMessage("请求超时");
            throw new RuntimeException("请求超时");

        }

        return response;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
//            conn.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
