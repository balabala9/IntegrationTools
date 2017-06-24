package org.learnSource.httpclient;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by li on 17-6-24.
 */
public class HttpClientDemo {

    @Test
    public void simpleGet() throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpget = new HttpGet("http://baidu.com");

        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpget);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.close();
        }

        System.out.println(response);

    }

    @Test
    public void uriConsist() {

        try {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("www.google.com")
                    .setPath("/search")
                    .setParameter("q", "httpclient")
                    .setParameter("btnG", "Google Search")
                    .setParameter("aq", "f")
                    .setParameter("oq", "")
                    .build();

            HttpGet httpget = new HttpGet(uri);

            System.out.println(httpget.getURI());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void responseConsist() {

        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        System.out.println(response.getProtocolVersion());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().getReasonPhrase());
        System.out.println(response.getStatusLine().getProtocolVersion());


    }

    @Test
    public void headerMessage() {

        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
        response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

        Header h1 = response.getFirstHeader("Set-Cookie");
        System.out.println(h1);


        Header h2 = response.getLastHeader("Set-Cookie");
        System.out.println(h2);


        Header[] hs = response.getHeaders("Set-Cookie");
        System.out.println(hs.length);

    }


    @Test
    public void headerMessageIterator() {

        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");

        HeaderIterator headerIterator = response.headerIterator();

        while (headerIterator.hasNext()) {

            Object object = headerIterator.next();//object (key,value)
            System.out.println(object);
        }
    }

    @Test
    public void headerMessageElementIterator() {

        BasicHttpResponse basicHttpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        basicHttpResponse.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");

        HeaderElementIterator it = new BasicHeaderElementIterator(basicHttpResponse.headerIterator("Set-Cookie"));

        while (it.hasNext()) {

            HeaderElement element = it.nextElement();
            System.out.println(element.getName() + "=" + element.getValue());

            NameValuePair[] params = element.getParameters();

            for (int i = 0; i < params.length; i++) {
                System.out.println(params[i]);
            }
        }

    }


    @Test
    public void headerMessageEntity() throws IOException {

        StringEntity entity = new StringEntity("important message", ContentType.create("text/plain", "utf-8"));

        System.out.println(entity.getContentType());
        System.out.println(entity.getContentLength());
        System.out.print(EntityUtils.toString(entity));

    }


    @Test
    public void headerMessageResponseEntity() throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("https://www.baidu.com/");
        //在请求中明确定义不要进行压缩
        httpGet.addHeader("Accept-Encoding", "identity");

        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {

            HttpEntity httpEntity = response.getEntity();
//          方法2
//            System.out.println("----------"+EntityUtils.toString(new BufferedHttpEntity(httpEntity)) );

            if (httpEntity != null) {

                InputStream inputStream = httpEntity.getContent();

                System.out.println(httpEntity.getContentLength());//其葩显示－1
//                entity内容方法1
//                System.out.println("----"+EntityUtils.toString(httpEntity));
//                System.out.println("-----");
                try {

                    byte[] buffer = new byte[1024];
                    int len = -1;

                    while ((len = inputStream.read(buffer)) != -1) {

                        System.out.println(new String(buffer));

                    }

                    System.out.println(response.toString());//显示头信息

                } catch (Exception e) {

                } finally {
                    inputStream.close();
                }
            }
        } finally {
            httpClient.close();
        }
    }


    @Test
    public void headerMessageProductEntity(){

        CloseableHttpClient httpClient=HttpClients.createDefault();

        StringEntity entity=new StringEntity("this is message", ContentType.create("text/plain","utf-8"));

        HttpPost post=new HttpPost();

        post.setEntity(entity);


    }

    @Test
    public void httpContext() throws IOException {

        CloseableHttpClient httpClient=HttpClients.createDefault();

        RequestConfig requestConfig=RequestConfig.custom()
                .setSocketTimeout(1000)
                .setConnectTimeout(1000)
                .build();

        HttpGet httpGet=new HttpGet("http://baidu.com");

        httpGet.setConfig(requestConfig);

        HttpContext context= HttpClientContext.create();
        context.setAttribute("key", "value");

        CloseableHttpResponse response=httpClient.execute(httpGet, context);

        System.out.println(EntityUtils.toString(response.getEntity()));


    }
}
