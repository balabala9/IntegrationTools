package org.util.hadoop;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 17-7-7.
 */
public class HadoopUtil {
    private Configuration conf;

    static {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }


    HadoopUtil() {
        conf = new Configuration();

        conf.set("fs.default.name", "hdfs://master:9000");//如果不写只能本地操作
        conf.set("dfs.web.ugi", "hadoop,hadoop");
        conf.setBoolean("dfs.support.append", true);

    }

    public String readFile(String url, String encode) {

        InputStream in = null;
        StringBuffer sb = new StringBuffer();

        String str = "";

        try {
            in = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));

            while ((str = reader.readLine()) != null) {
                sb.append(str).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public String readFile_v1(String url, String encode) {

        InputStream in = null;

        try {
            in = new URL(url).openStream();

            return IOUtils.toString(in, encode);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
        return null;

    }

    public String readFile_v2(String filePath) {


        FSDataInputStream fsin = null;
        try {
            FileSystem fs = FileSystem.get(URI.create(filePath), conf);
            fsin = fs.open(new Path(filePath));

            return IOUtils.toString(fsin);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fsin);
        }
        return null;
    }


    public String createDir(String dirPath) {

        try {
            FileSystem fs = FileSystem.get(URI.create(dirPath), conf);


            if (!fs.exists(new Path(dirPath))) {

                boolean res = fs.mkdirs(new Path(dirPath));

                return String.valueOf(res);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "this file exsit";
    }


    public String writeDate(String filePath, boolean isOverwrite, String context) {

        FSDataOutputStream fsDataOutputStream = null;

        try {
            FileSystem fs = FileSystem.get(URI.create(filePath), conf);

            if (!fs.exists(new Path(filePath))) {
                fsDataOutputStream = fs.create(new Path(filePath), isOverwrite);
                fsDataOutputStream.write(Bytes.toBytes(context));

                fsDataOutputStream.flush();

                return "complete write data";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fsDataOutputStream != null) {
                    fsDataOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "this's file exist";
    }
//有问题
//    public void appendDate(String path,String context){
//
//        FSDataOutputStream fsDataOutputStream=null;
//
//        try {
//            FileSystem fs = FileSystem.get(URI.create(path),conf);
//            fsDataOutputStream=fs.append(new Path(path));
//            fsDataOutputStream.write(Bytes.toBytes(context));
//
//            fsDataOutputStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                fsDataOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    public String deleteFileOrDir(String path) {

        try {
            FileSystem fs = FileSystem.get(URI.create(path), conf);
            Path tmpPath = new Path(path);

            if (fs.exists(tmpPath)) {
                fs.delete(tmpPath, true);
                return "complete delete";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "file or dir not exit";
    }

    public String filestatus(String path) {

        try {
            FileSystem fs = FileSystem.get(URI.create(path), conf);
            Path tmpPath = new Path(path);

            if (fs.exists(tmpPath)) {
                FileStatus fileStatus = fs.getFileStatus(tmpPath);


                Filebean bean = new Filebean();

                bean.setAccessTime(String.valueOf(fileStatus.getAccessTime()));
                bean.setGroup(fileStatus.getGroup());
                bean.setLen(String.valueOf(fileStatus.getLen()));
                bean.setModificationTime(String.valueOf(fileStatus.getModificationTime()));
                bean.setPath(String.valueOf(fileStatus.getPath()));
                bean.setOwner(fileStatus.getOwner());
                bean.setReplication(String.valueOf(fileStatus.getReplication()));
                bean.setBlockSize(String.valueOf(fileStatus.getBlockSize()));
                bean.setPermission(String.valueOf(fileStatus.getPermission()));
                bean.setIsFile(String.valueOf(fileStatus.isFile()));
                bean.setIsDir(String.valueOf(fileStatus.isDirectory()));

                return JSON.toJSONString(bean);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String listPath(String path) {

        List<String> list = new ArrayList<String>();
        try {
            FileSystem fs = FileSystem.get(URI.create(path), conf);
            Path tmpPath = new Path(path);

            if (fs.exists(tmpPath) && fs.isDirectory(tmpPath)) {
                FileStatus[] listPath = fs.listStatus(tmpPath);

                for (FileStatus singlePath : listPath) {
                    list.add(singlePath.getPath().toString());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(list);
    }
}
