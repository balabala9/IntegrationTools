package org.util.thread;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.util.thread.ThreadUtil;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Created by li on 17-6-15.
 */
public class test {


    /**
     * 测试FutureTask实现的接口
     * @throws ClassNotFoundException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Test
    public  void ThreadUtil() throws ClassNotFoundException, ExecutionException, InterruptedException, IllegalAccessException, InstantiationException {


        ThreadUtil<Integer> threadUntil=new ThreadUtil<>();

        Callable c1= threadUntil.callableObject(Computer.class,"add",1,2);
        Callable c2= threadUntil.callableObject(Computer.class,"reduce",3,2);

        List list= threadUntil.execute(c1,c2);

        List resList= threadUntil.getResult(list);

        System.out.println(JSON.toJSONString(resList));

    }
}
