package org.util;

/**
 * Created by li on 17-7-26.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SMSTest {

    private static final String addr = "http://api.sms.cn/sms/";
    private static final String userId = "bala";

    /*
    * 如uid是：test，登录密码是：123123
    pwd=md5(123123test),即
    pwd=b9887c5ebb23ebb294acab183ecf0769

    */
    private static final String pwd = "27d3ade40bb1fc3d61f2efca397a5e5c";

    private static final String encode = "utf8";

    private static final String templateType="";

    public static void send(String msgContent, String mobile) throws Exception {

//组建请求
        String straddr = addr +
                "?ac=send&uid="+userId+
                "&pwd="+pwd+
                "&mobile="+mobile+
                "&encode="+encode+
                "&content=" + msgContent+
                "&template="+"100006";

        StringBuffer sb = new StringBuffer(straddr);
        System.out.println("URL:"+sb);

//发送请求
        URL url = new URL(sb.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                url.openStream()));

//返回结果
        String inputline = in.readLine();
        System.out.println("Response:"+inputline);

    }


    public static void main(String[] args) {
        try {
            send("5236", "13821532192");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}