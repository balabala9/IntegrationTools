package org.util.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.util.ajax.JSON;

import java.io.IOException;
import java.util.Map;


/**
 * Created by li on 17-6-24.
 */
public class httpUtil {


    public static String httpPost(String url, Object o) {

        return httpPost(url, o, "UTF-8");
    }


    public static String httpPost(String url, Object o, String encoding) {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost post = new HttpPost(url);

        configHeader(post);

        //设置请求参数
        setParms(post, o, encoding);

        //设置外部网络环境
        config(post);


        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post);

            return getResponse(response, encoding);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String httpGet(String url) {
        return httpGet(url, "UTF-8");
    }


    public static String httpGet(String url, String encoding) {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpget = new HttpGet(url);


        configHeader(httpget);
        config(httpget);


        try {
            CloseableHttpResponse response = httpclient.execute(httpget);

            return getResponse(response, encoding);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    //主要用于获取和配置一些外部的网络环境
    private static void config(HttpRequestBase httpRequestBase) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000).build();

        httpRequestBase.setConfig(requestConfig);


    }

    //设置entity
    private static void setParms(HttpPost httpost, Object o, String encoding) {
        //设置entity
        String param = "";

        if (o != null) {

            if (o instanceof String) {
                param = o.toString();
            } else if (o instanceof Map) {
                param = JSON.toString(o);
            }

            StringEntity entity = null;

            //UrlEncodedFormEntity实例像上面一样使用URL编码方式来编码参数并生成下面的内容：param1=value1&param2=value2
            entity = new StringEntity(param, encoding);

            httpost.setEntity(entity);

        }
    }

    //设置请求头
    public static void configHeader(HttpRequestBase httpRequestBase) {
        //设置头部
        httpRequestBase.setHeader("Accept-Encoding", "identity");
        //请求首部--可选的，User-Agent对于一些服务器必选，不加可能不会返回正确结果
        httpRequestBase.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
    }


    private static String getResponse(CloseableHttpResponse response, String encoding) {

        HttpEntity responseHttpEntity = response.getEntity();

        if (responseHttpEntity != null) {

            long len = responseHttpEntity.getContentLength();

            if (len != -1) {
                String body = null;
                try {
                    body = EntityUtils.toString(responseHttpEntity, encoding);

                    EntityUtils.consume(responseHttpEntity);

                    return body;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}
