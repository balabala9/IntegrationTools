package org.util.hbase;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 17-6-30.
 */
public class CHbaseUtil {
    private HBaseConfiguration configuration;

    private HBaseAdmin admin;

    private HTablePool tablePool;

    public CHbaseUtil() {
        configuration = new HBaseConfiguration();//默认会从classpath查找
        try {
            admin = new HBaseAdmin(configuration);
            tablePool = new HTablePool(configuration, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *创建表
     * @param tableName 表名
     * @param args　列簇
     */
    public void creatHbaseTable(String tableName, String... args) {

        HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

        for (String cloumn : args) {

            HColumnDescriptor columnDescriptor = new HColumnDescriptor(cloumn);
            tableDescriptor.addFamily(columnDescriptor);
        }

        try {
            admin.createTable(tableDescriptor);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除表
     * @param tableName 表名
     */
    public void deleteHbaseTable(String tableName) {

        HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

        try {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除列簇
     * @param tableName
     * @param args　列簇名
     */
    public void deleteHbaseColumn(String tableName, String... args) {

        HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

        for (String cloumn : args) {

            try {
                admin.deleteColumn(tableName, cloumn);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public HTableInterface getHTable(String tableName) {

        return tablePool.getTable(tableName);
    }

    /**
     * 插入单条数据
     * @param tableName
     * @param rowkey
     * @param family    列
     * @param qualifier 　限定符
     * @param value
     */
    public void insertData(HTableInterface tableName, String rowkey, String family, String qualifier, String value) {

        Put put = new Put(Bytes.toBytes(rowkey));
        put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
        try {
            tableName.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param tableName
     * @param rowkey
     * @param family
     * @param qualifiers
     * @param values
     */
    public void insertData(HTableInterface tableName, String rowkey, String family, String[] qualifiers, String[] values) {

        Put put = new Put(Bytes.toBytes(rowkey));

        for (String qualifier : qualifiers) {
            for (String value: values)
                put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
        }
        try {
            tableName.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteRowData(HTableInterface tableName, String rowkey) {

        Delete delete = new Delete(Bytes.toBytes(rowkey));

        try {
            tableName.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRowData(HTableInterface tableName, String rowkey) {

        Get get = new Get(Bytes.toBytes(rowkey));
        List<CHbaseBean> list = new ArrayList<>();
        try {
            Result row = tableName.get(get);
            for (KeyValue kv : row.raw()) {

                CHbaseBean bean = new CHbaseBean();
                bean.setRow(new String(kv.getRow()));
                bean.setFamily(new String(kv.getFamily()));
                bean.setQualifier(new String(kv.getQualifier()));
                bean.setValue(new String(kv.getValue()));

                list.add(bean);
            }

            return JSON.toJSONString(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTableData(HTableInterface tableName, String start, String stop) {

        Scan scan = new Scan();

        scan.setStartRow(Bytes.toBytes(start));
        scan.setStartRow(Bytes.toBytes(stop));

        List<CHbaseBean> list = new ArrayList<>();
        try {
            ResultScanner scanner = tableName.getScanner(scan);

            for (Result row : scanner) {

                for (KeyValue kv : row.raw()) {

                    CHbaseBean bean = new CHbaseBean();
                    bean.setRow(new String(kv.getRow()));
                    bean.setFamily(new String(kv.getFamily()));
                    bean.setQualifier(new String(kv.getQualifier()));
                    bean.setValue(new String(kv.getValue()));

                    list.add(bean);
                }
            }

            return JSON.toJSONString(list);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
