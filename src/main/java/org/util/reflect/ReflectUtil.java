package org.util.reflect;


import com.alibaba.fastjson.JSON;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by li on 17-6-15.
 */
public class ReflectUtil<T> {


    //与class api相比无用
    //获取完整的包名(带package关键字)
    public static Package getPackageName(Class<?> className0) {
        return className0.getPackage();
    }


    //获取完整的包名+类名
    public static String getPackageToClassName(Class<?> className0) {
        return className0.getName();
    }

    //返回此class的超类
    public static Class<?> getSuperclass(Class<?> childClassName0) {
        return childClassName0.getSuperclass();
    }

    public static Class[] getInterfaces(Class<?> className0) {
        return className0.getInterfaces();
    }

    //与class api相比无用


    //查看类的全部属性　属性名　属性类型　
    public static String getFields(Class<?> className) {

        List<FieldBean> list = new ArrayList<FieldBean>();
        Field[] fields = className.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {

            FieldBean bean = new FieldBean();

            bean.setName(fields[i].getName());
            bean.setType(fields[i].getType().toString());
            bean.setModifier(Modifier.toString(fields[i].getModifiers()));

            list.add(bean);
        }

        return JSON.toJSONString(list);
    }


    //操作类的某个属性
    public Object modifiyProperty(Class<?> className, String propertyName, T value ) throws NoSuchFieldException, IllegalAccessException, InstantiationException {

        Object obj = className.newInstance();

        Field field = className.getDeclaredField(propertyName);

        field.setAccessible(true);

        field.set(obj,value);

        return field.get(obj);

    }


    //查看构造函数的　函数名　　参数个数　　类型

    public static String getConstructorInfo(Class<?> className) {

        List<FunBean> list = new ArrayList<FunBean>();

        Constructor<?>[] constructors = className.getDeclaredConstructors();

        for (int i = 0; i < constructors.length; i++) {
            String name = constructors[i].getName();
            int paramCount = constructors[i].getParameterCount();

            List<Class<?>> parameterTypeList = new ArrayList<Class<?>>();

            for (int j = 0; j < paramCount; j++) {
                parameterTypeList.add(constructors[i].getParameterTypes()[j]);
            }

            FunBean bean = new FunBean();
            bean.setFunName(name);
            bean.setParaTypeNum(String.valueOf(paramCount));
            bean.setParameterTypeList(parameterTypeList);

            list.add(bean);

        }
        return JSON.toJSONString(list);

    }

    //查看　method 函数名　参数　参数个数
    public static String getMethodInfo(Class<?> className) {

        List<FunBean> list = new ArrayList<FunBean>();

        Method[] methods = className.getMethods();

        for (int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            int paramCount = methods[i].getParameterCount();

            List<Class<?>> parameterTypeList = new ArrayList<Class<?>>();


            for (int j = 0; j < paramCount; j++) {
                parameterTypeList.add(methods[i].getParameterTypes()[j]);
            }

            FunBean bean = new FunBean();
            bean.setFunName(name);
            bean.setParaTypeNum(String.valueOf(paramCount));
            bean.setParameterTypeList(parameterTypeList);

            list.add(bean);

        }

        return JSON.toJSONString(list);
    }


    //查看给定函数名　参数　类型
    public static String getSingMethodInfo(String methodJson, String methodName) {

        List<FunBean> resList = new ArrayList<FunBean>();

        List<FunBean> list = JSON.parseArray(methodJson, FunBean.class);

        Iterator<FunBean> it = list.iterator();

        while (it.hasNext()) {
            FunBean next = it.next();

            if (methodName.equals(next.getFunName())) {

                FunBean bean = new FunBean();
                bean.setFunName(next.getFunName());
                bean.setParaTypeNum(next.getParaTypeNum());
                bean.setParameterTypeList(next.getParameterTypeList());

                resList.add(bean);

            }
        }

        return JSON.toJSONString(resList);
    }


    public static Object invoke(Class<?> className, String funName, String methodJson, String[] params) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

        String res = getSingMethodInfo(methodJson, funName);

        List<FunBean> jsonObject = JSON.parseArray(res, FunBean.class);
        List<Class<?>> list = jsonObject.get(0).getParameterTypeList();
        Class<?>[] parameterTypes = new Class<?>[list.size()];

//        if(list.length!=params.length) break;
        for (int i = 0; i < list.size(); i++) {
            parameterTypes[i] = list.get(i);
        }
        Method method = className.getMethod(funName, parameterTypes);
        return method.invoke(className.newInstance(), params);

    }


    private static Method getMethod0(Class<?> className, String funName, String[] argsType) throws NoSuchMethodException {

        Class<?>[] parameterTypes = new Class[argsType.length];

        for (int i = 0; i < argsType.length; i++) {

            if (argsType[i] == "String") parameterTypes[i] = String.class;

            if (argsType[i] == "Integer" || argsType[i] == "int") parameterTypes[i] = Integer.class;

            if (argsType[i] == "float" || argsType[i] == "Float") parameterTypes[i] = Float.class;

            if (argsType[i] == "Double" || argsType[i] == "Double") parameterTypes[i] = Double.class;

        }
        return className.getMethod(funName, parameterTypes);
    }


    //调用方法获取结果
    public static Object invoke(Class<?> className, String funName, String[] argsType, Object... args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Method method = getMethod0(className, funName, argsType);
        return method.invoke(className.newInstance(), args);
    }


}
