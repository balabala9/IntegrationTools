package org.util.thread;

import org.util.reflect.ReflectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by li on 17-6-20.
 */
public class ThreadUtil<V> {
    /**
     * @param className  　class类
     * @param methodName 调用方法名
     * @param params     方法参数集
     * @return 调用方法本身返回值和返回类型V（和 Callable<V>对应）
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Callable callableObject(Class<?> className, String methodName, Object... params) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        Callable<V> callable = new Callable<V>() {
            @Override
            public V call() throws Exception {
                String methodJson = ReflectUtil.getMethodInfo(className);
                return (V) ReflectUtil.invoke(className, methodName, methodJson, params);
            }
        };

        return callable;
    }


    public List<FutureTask<V>> execute(Callable<V>... args){

        List<FutureTask<V>>  futureList=new ArrayList<FutureTask<V>>();

        for (int i=0;i<args.length;i++){

            FutureTask<V> future=new FutureTask<V>(args[i]);
            new Thread(future).start();

            futureList.add(future);

        }

        return futureList;
    }


    public  List<Object>  getResult(List<FutureTask<V>> list) throws ExecutionException, InterruptedException {

        List<Object> resultList=new ArrayList<Object>();

        for (int i=0;i<list.size();i++){

            resultList.add(list.get(i).get());

        }

        return resultList;
    }
}
