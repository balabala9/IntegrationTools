package org.util.hadoop;

import org.junit.Test;

/**
 * Created by li on 17-7-4.
 */
public class test {

    @Test
    public void readFileTest(){
        HadoopUtil hadoopUtil=new HadoopUtil();
        String res=hadoopUtil.readFile_v2("hdfs://master:9000/user/hadoop/input/file1");
        System.out.println(res);
    }


    @Test
    public void creatDirTest(){
        HadoopUtil hadoopUtil=new HadoopUtil();
        String res=hadoopUtil.createDir("hdfs://master:9000/user/hadoop/test1");
        System.out.println(res);

    }

    @Test
    public void writeDataTest(){
        HadoopUtil hadoopUtil=new HadoopUtil();
        String res=hadoopUtil.writeDate("hdfs://master:9000/user/hadoop/test1/testfile",false,"wode dddddddddddd");
        System.out.println(res);
    }

//    @Test
//    public void appendDataTest(){
//        HadoopUtil hadoopUtil=new HadoopUtil();
//        hadoopUtil.appendDate("hdfs://master:9000/user/hadoop/test1/testfile","lalalalalalalal");
//    }

    @Test
    public void deleteTest(){
        HadoopUtil hadoopUtil=new HadoopUtil();
        String res=hadoopUtil.deleteFileOrDir("hdfs://master:9000/user/hadoop/test1/testfile");
        System.out.println(res);
    }


    @Test
    public void fileStatusTest(){
        HadoopUtil hadoopUtil=new HadoopUtil();
        String res=hadoopUtil.filestatus("hdfs://master:9000/user/hadoop/");
        System.out.println(res);
    }

    @Test
    public void listFileTest(){
        HadoopUtil hadoopUtil=new HadoopUtil();
        String res=hadoopUtil.listPath("hdfs://master:9000/user/hadoop/");
        System.out.println(res);
    }
}
