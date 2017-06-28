package org.util.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by li on 17-6-27.
 */
public class PropertiesStr {


    //redis

    //redis Config
    public static int maxActive;
    public static int maxIdle;
    public static int maxWait;

    public static HashMap<String, Integer> distributedHostMap = new HashMap<String, Integer>();
    public static HashMap<String, Integer> cluterHostMap = new HashMap<String, Integer>();
    public static HashMap<String, Integer> singleHostMmap = new HashMap<String, Integer>();


    PropertiesStr(String name) {

        InputStream inputStream = ClassLoader.getSystemResourceAsStream(name);

        Properties properties = new Properties();

        try {
            properties.load(inputStream);

            maxActive= Integer.parseInt(properties.getProperty("maxTotal"));
            maxIdle= Integer.parseInt(properties.getProperty("maxIdle"));
            maxWait= Integer.parseInt(properties.getProperty("maxWaitMillis"));

            String[]  cluterhostports=properties.getProperty("cluterredishost").split(",");

            for(int i=0;i<cluterhostports.length;i++){
                String[]  cluterhostport= cluterhostports[i].split(":");
                cluterHostMap.put(cluterhostports[i],Integer.parseInt(cluterhostport[1]));
            }


            String[]  distributedhostports=properties.getProperty("distributedredishost").split(",");

            for(int i=0;i<distributedhostports.length;i++){
                String[]  distributedhostport= distributedhostports[i].split(":");
                distributedHostMap.put(distributedhostports[i],Integer.parseInt(distributedhostport[1]));
            }


            String[]  singlehostports=properties.getProperty("listenerhost").split(",");

            for(int i=0;i<singlehostports.length;i++){
                String[]  singlehostport= singlehostports[i].split(":");
                singleHostMmap.put(singlehostports[i],Integer.parseInt(singlehostport[1]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
