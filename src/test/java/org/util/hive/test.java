package org.util.hive;

import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by li on 17-7-28.
 */
public class test {
    @Test
    public void testCreatTable() {
        HiveUtil hiveUtil = new HiveUtil();
        hiveUtil.initHive();

        Map<String, String> clumap = new HashMap<String, String>();
        clumap.put("id", "string");
        clumap.put("name", "string");
        clumap.put("age", "int");
        //内表

        String res = HiveUtil.createTable("innerTable", "1", "textfile", "\t", clumap, "", "");

        System.out.println(res);
    }

    @Test
    public void TestLoadData() {
        HiveUtil hiveUtil = new HiveUtil();
        hiveUtil.initHive();

        HiveUtil.loadData("innerTable", "1", "/home/li/test/inner.txt", "1", "");
    }

    @Test
    public void lookDatabase() {

        HiveUtil hiveUtil = new HiveUtil();
        hiveUtil.initHive();

        String sql = "show databases";
        String sql1 = "show tables";
        String sql3 = "show tables '*h*'";
        String sql2 = "desc t1";
        String sql4 = "show partitions t1";
        String res = HiveUtil.queryTableOrDatabase(sql4);

        System.out.println(res);

    }

    @Test
    public void getTableData() throws SQLException {
        HiveUtil hiveUtil = new HiveUtil();
        hiveUtil.initHive();

        String sql = "select * from innerTable";

        PreparedStatement stmt = HiveUtil.getPreparedStatement(sql);
        ResultSet set = HiveUtil.getResultSet(stmt);

        while (set.next()) {
            System.out.println(set.getString(1));
            System.out.println(set.getString(2));
            System.out.println(set.getString(3));

        }
    }
}
