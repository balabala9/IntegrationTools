package org.util.mapreduce;

import org.junit.Test;

/**
 * Created by li on 17-7-5.
 */
public class test {

    @Test
    public void initHbaseTest() {
        MapreduceDemo mapreduceDemo = new MapreduceDemo();
//        mapreduceDemo.initData("/home/li/IdeaProjects/mavenDemo/src/main/java/org/mavendemo/hadoopAndHbase/word");
        String res=mapreduceDemo.viewData();
        mapreduceDemo.initMapreduce("/home/li/IdeaProjects/mavenDemo/src/main/java/org/mavendemo/hadoopAndHbase/w1/","/home/li/IdeaProjects/mavenDemo/src/main/java/org/mavendemo/hadoopAndHbase/output/");

    }
}
