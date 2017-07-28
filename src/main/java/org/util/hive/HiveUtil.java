package org.util.hive;

import org.util.CommonConst;
import org.util.StringUtil;
import org.util.hadoop.HadoopUtil;

import java.sql.*;
import java.util.Map;

/**
 * Created by li on 17-7-26.
 */
public class HiveUtil {

    private static String driveName = "org.apache.hive.jdbc.HiveDriver";
    private static String url = "jdbc:hive2://master:10000/hive";
    private static String pwd = "123456";
    private static String userName = "hadoop";

    private static HadoopUtil  hadoopUtil=new HadoopUtil();


    private static Connection connection;


    //常量
    private static final String EXTERNALTABLE = "4";

    public void initHive() {
        getConnection();
    }

    private void getConnection() {
        try {
            Class.forName(driveName);
            connection = DriverManager.getConnection(url, userName, pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //创建表 1:inner,2:分区表,3:桶表，4外表
    // map:value type
    //partitionStr:(cluvalue,clutype)
    //storedType:textfile, sequencefile,rcfile
    public static String createTable(String tableName, String tableType, String storedType, String splitChar, Map<String, String> columnArgs, String partitionStr, String externalTablePath) {

        //sql拼接

        String strSql = " create ";

        if (tableType.equals(EXTERNALTABLE)) {
            strSql += " external ";
        }
        strSql += " table " + tableName + " ( ";

        for (Map.Entry<String, String> entry : columnArgs.entrySet()) {
            strSql += entry.getKey() + " " + entry.getValue() + " ,";
        }

        strSql = strSql.substring(0, strSql.length() - 1)
                + " )";

        switch (tableType) {
            case "2":
                if (StringUtil.isBlankOrEmpty(strSql))
                    return "hive partition table " + partitionStr + "param" + CommonConst.IS_NO_BLACK;
                strSql += " partitioned by " + partitionStr;
                break;
            case "4":
                if (StringUtil.isBlankOrEmpty(externalTablePath))
                    return "hive partition table " + externalTablePath + "param" + CommonConst.IS_NO_BLACK;
                strSql += " location " + "'" + externalTablePath + "'";
                break;
        }

        strSql += " row format delimited fields terminated by " + "'" + splitChar + "'" + " stored as " + storedType;

        System.out.println(strSql);

        PreparedStatement prestmt = null;
        Integer res = null;

        try {
            prestmt = connection.prepareStatement(strSql);
            res = prestmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                prestmt.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (res.equals(0)) return "hive table " + tableName + " create complete";
        else return "hive table " + tableName + "create fail";
    }

    //删除表
    public static String dropTable(String tableName) {

        String sql = "drop table " + tableName;

        PreparedStatement prestmt = null;
        Integer res = null;
        try {
            prestmt = connection.prepareStatement(sql);
            res = prestmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                prestmt.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (res.equals(0)) return "hive table " + tableName + " delete complete";
        else return "hive table " + tableName + "delete fail";
    }


    //show databases;
    //show tables;
    //desc  表名
    //show partitions 表名
    public static String queryTableOrDatabase(String sql) {
        PreparedStatement prestmt = null;
        ResultSet resSet = null;

        String str = new String();
        try {
            prestmt = connection.prepareStatement(sql);
            resSet = prestmt.executeQuery();

            while (resSet.next()) {
                str += resSet.getString(1) + "\t";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return str;

    }


    //加载数据
    //loadType: l
    public static void loadData(String tableName, String tableType, String inpathUrl, String loadType, String partitionStr) {

        String sql = new String();
        String hdfsHost = "hdfs://master:9000";
        String localHost="file:";

        /*
        switch (loadType) {
            case "1":
                sql = "load data local inpath '" +  inpathUrl + "' into table " + tableName;
                break;

            case "2":
                sql = "load data  inpath '" + hdfsHost + inpathUrl + "' into table " + tableName;
                break;
        }*/

        hadoopUtil.copyFile(inpathUrl, "/user/hive/warehouse/");

        sql = "load data  inpath '" + hdfsHost + "/user/hive/warehouse/"+inpathUrl.split("/")[inpathUrl.split("/").length-1] + "' into table " + tableName;

        if (tableType.equals("2") && StringUtil.isBlankOrEmpty(partitionStr))
            sql += " partition " + partitionStr;


        System.out.println(sql);
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //查询（设置查询条件值）
    public static PreparedStatement getPreparedStatement(String sql) {
        PreparedStatement prestmt = null;
        try {
            prestmt = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestmt;
    }

    public static ResultSet getResultSet(PreparedStatement stmt) {
        ResultSet resultSet = null;
        try {
            resultSet = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;

    }


    //全部查询
    public static ResultSet getResultSet(String sql) {
        ResultSet resultSet = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            resultSet = stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    public static void main(String[] args) {

        HiveUtil hiveUtil = new HiveUtil();

        hiveUtil.initHive();

//        String sql = "select id from hivetest";
//
//        PreparedStatement stmt = HiveUtil.getPreparedStatement(sql);
//        ResultSet resultSet = HiveUtil.getResultSet(stmt);
//        try {
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString(2));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        Map<String, String> map = new HashMap<>();
//        map.put("id", "string");
//        map.put("name", "string");
//
//        String res = HiveUtil.createTable("t1", "1", "textfile", " ", map, "", "");
//
//       String res= HiveUtil.dropTable("t7");
//        System.out.println(res);

//       ResultSet res=  HiveUtil.getResultSet("show tables");
//
//        try {
//
//            while (res.next()) {
//                    System.out.println(res.getString(1));
//
//                }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        String str = HiveUtil.queryTableOrDatabase("desc hivetest");
        System.out.println(str);

    }

}
