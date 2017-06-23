package org.util.signature;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.util.encryption.Sha1;


import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;


/**
 * Created by li on 17-6-22.
 *
 * 签名
 * 1.请求参数串升序排列
 * 2.被签名串＝请求参数串＋privateKey
 * 3.sha1
 */
public class Signature {


    public static String verfyAc(String privateKey,String url) throws URISyntaxException {


        URIBuilder uri = new URIBuilder(url);

        List<NameValuePair> queryParamsList=uri.getQueryParams();

        StringBuilder str=new StringBuilder();

        queryParamsList.stream()
                .sorted(Comparator
                        .comparing(NameValuePair:: getName))
                .forEach( it -> str.append(it.getName()).append(it.getValue()));


        str.append(privateKey);


        return Sha1.getSha1(str.toString());
    }
}
