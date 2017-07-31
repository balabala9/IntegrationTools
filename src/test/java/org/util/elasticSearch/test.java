package org.util.elasticSearch;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by li on 17-7-31.
 */
public class test {
    @Test
    public void testCreatIndex() {
        ElasticSearchUtil elasticSearchUtil = new ElasticSearchUtil("master", 9300, "el");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "li");
        map.put("nickname", "lili");

        String res = elasticSearchUtil.elCreateIndex(JSON.toJSONString(map), "shop", "userInfo");
        System.out.println(res);
    }
}
