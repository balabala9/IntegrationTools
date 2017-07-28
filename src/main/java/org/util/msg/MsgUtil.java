package org.util.msg;


import org.util.httpclient.HttpUtil;

import java.io.IOException;


import java.net.URISyntaxException;

import java.security.NoSuchAlgorithmException;


/**
 * Created by li on 17-7-26.
 */
public class MsgUtil {

    private static final String msgUrl = "http://api.sms.cn/sms/";

    private static final String userId = "bala";
    private static final String pwd = "27d3ade40bb1fc3d61f2efca397a5e5c";

    private static final String template="100006";
    private static String encode = "UTF-8";

    public static String sendMsg(String msg, String mobile) {

        //组建请求地址
        String urlStr = msgUrl +
                "?ac=send&uid=" + userId +
                "&pwd=" + pwd +
                "&encode=" + encode +
                "&template="+template+
                "&mobile=" + mobile +
                "&content=" + msg;

        System.out.println("URL:" + urlStr);

        String responseRes = HttpUtil.httpPost(urlStr, "","gbk");

        System.out.println(responseRes);

        return responseRes;

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, URISyntaxException {

        String res = MsgUtil.sendMsg("10002", "13821532192");
        System.out.println(res);
    }
}
