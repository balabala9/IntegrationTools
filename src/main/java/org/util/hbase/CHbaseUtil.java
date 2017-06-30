package org.util.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;

import java.io.IOException;

/**
 * Created by li on 17-6-30.
 */
public class CHbaseUtil {

    private HBaseConfiguration configuration;

    private HBaseAdmin admin;

    private HTablePool tablePool;

    CHbaseUtil() {
        configuration = new HBaseConfiguration();//默认会从classpath查找
        try {
            admin = new HBaseAdmin(configuration);
            tablePool=new HTablePool(configuration,1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //表操作
    public void creatHbaseTable(String tableName,String... args){

        HTableDescriptor tableDescriptor=new HTableDescriptor(tableName);

        for (String cloumn :args){

            HColumnDescriptor columnDescriptor=new HColumnDescriptor(cloumn);
            tableDescriptor.addFamily(columnDescriptor);
        }

        try {
            admin.createTable(tableDescriptor);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteHbaseTable(String tableName){

        HTableDescriptor tableDescriptor=new HTableDescriptor(tableName);

        try {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteHbaseColumn(String tableName,String... args){

        HTableDescriptor tableDescriptor=new HTableDescriptor(tableName);

        for (String cloumn :args){

            try {
                admin.deleteColumn(tableName, cloumn);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public HTableInterface getHTable(String tableName){

        return tablePool.getTable(tableName);
    }

}
