package org.util.elasticSearch;


import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.util.StringUtil;


/**
 * Created by li on 17-7-31.
 */
public class ElasticSearchUtil {

    private TransportClient client;


    public ElasticSearchUtil(String elIp, int elPort, String clusterName) {


        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", clusterName)//使用超过一个el集群时必须设置el集群名
                .put("client.transport.sniff", true)//client可以嗅探集群的其余机器
                .build();

        //创建client
        client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(elIp, elPort));


    }

    public  String  elCreateIndex(String json, String index, String type){
        return elCreateIndex(json,index,"");
    }

    //创建索引
    public String elCreateIndex(String json, String index, String type, String id) {

        IndexRequestBuilder requestBuilder;

        if (StringUtil.isBlankOrEmpty(id)) {
            requestBuilder = client.prepareIndex(index, type);

        } else {
            requestBuilder = client.prepareIndex(index, type);
        }

        IndexResponse response = requestBuilder
                .setSource(json)
                .execute()
                .actionGet();

        ElIndexBean elIndexBean = new ElIndexBean();

        elIndexBean.setIndex(response.getIndex());
        elIndexBean.setType(response.getType());
        elIndexBean.setId(response.getId());
        elIndexBean.setVersion(response.getVersion());
        elIndexBean.setCreated(response.isCreated());

        if (true == elIndexBean.isCreated()) System.out.println(index + "索引创建成功");
        else System.out.println("索引创建失败哦");

        return JSON.toJSONString(elIndexBean);

    }

    //get索引
    public String elgetIndex(String index,String type,String id){
        client.prepareGet().execute().actionGet()
    }

}
