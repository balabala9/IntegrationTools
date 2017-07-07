package org.util.mapreduce;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.util.hbase.CHbaseUtil;
import org.util.redis.PropertiesStr;
import org.util.redis.RedisUntil;
import redis.clients.jedis.ShardedJedis;

import java.io.*;
import java.util.*;

/**
 * Created by li on 17-7-5.
 */
public class MapreduceDemo {

    private static final CHbaseUtil cHbaseUtil;


    static {

        cHbaseUtil = new CHbaseUtil();

        PropertiesStr propertiesStr = new PropertiesStr("config1.properties");

        RedisUntil.initRedisPool();
    }


    public void initData(String filename) {

        cHbaseUtil.creatHbaseTable("word", "family");
        String rowKey = UUID.randomUUID().toString();

        ShardedJedis shardedJedis = RedisUntil.getRedisObject();

        shardedJedis.del("wordkey");

        try {
            InputStream inputStream = new FileInputStream(filename);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            StringBuffer stringBuffer = new StringBuffer();

            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            //redis
            shardedJedis.hset("wordkey", rowKey, filename);
//
//            if(cHbaseUtil.isExist("word")){
//                cHbaseUtil.deleteHbaseTable("word");
//            }

            cHbaseUtil.insertData(cHbaseUtil.getHTable("word"), rowKey, "family", "content", stringBuffer.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            RedisUntil.returnRedisObject(shardedJedis);
        }
    }

    public String viewData() {

        ShardedJedis shardeJedis = RedisUntil.getRedisObject();

        try {
            Map<String, String> map = new HashMap<>();

            Map<String, String> resmap = shardeJedis.hgetAll("wordkey");

            for (Map.Entry<String, String> entry : resmap.entrySet()) {
                String res = cHbaseUtil.getRowData(cHbaseUtil.getHTable("word"), entry.getKey());

                List<HBaseResBean> list = JSON.parseArray(res, HBaseResBean.class);

                String vlaue = list.get(0).getValue();
                String rowkey = list.get(0).getRow();
                writeFile(vlaue, "/home/li/IdeaProjects/mavenDemo/src/main/java/org/mavendemo/hadoopAndHbase/w1/word1");

                map.put(rowkey, vlaue);
            }
            return JSON.toJSONString(map);

        } catch (Exception e) {

        } finally {
            RedisUntil.returnRedisObject(shardeJedis);
        }
        return null;
    }


    public void writeFile(String context, String path) throws IOException {

        try {

            FileOutputStream outputStream = new FileOutputStream(new File(path));

            outputStream.write(Bytes.toBytes(context));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class IokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

        private IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) {

            try {
                StringTokenizer tokenizer = new StringTokenizer(value.toString());

                while (tokenizer.hasMoreTokens()) {
                    word.set(tokenizer.nextToken());
                    context.write(word, one);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public static class countTotal extends Reducer<Text, IntWritable, Text, IntWritable> {

        private IntWritable res = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) {

            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            try {
                res.set(sum);
                context.write(key, res);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void initMapreduce(String path1, String path2) {

        Configuration configuration = new Configuration();

        try {

            Job job = Job.getInstance(configuration);
            job.setMapperClass(IokenizerMapper.class);
            job.setJarByClass(MapreduceDemo.class);
            job.setJobName("wordcount");

            job.setCombinerClass(countTotal.class);
            job.setReducerClass(countTotal.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            FileInputFormat.addInputPath(job, new Path(path1));
            FileOutputFormat.setOutputPath(job, new Path(path2));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        String[] otherArgs = new GenericOptionsParser(args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage WordCount <int> <out>");
            System.exit(2);
        }
        MapreduceDemo mapreduceDemo = new MapreduceDemo();
        mapreduceDemo.viewData();
        mapreduceDemo.initMapreduce(args[0], args[1]);

    }

}
