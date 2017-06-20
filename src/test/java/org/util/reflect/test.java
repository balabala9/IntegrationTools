package org.util.reflect;

/**
 * Created by li on 17-6-20.
 */
public class test {
    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, InstantiationException {

        ReflectUtil r= new ReflectUtil<String>();
        Object s=  r.modifiyProperty(User.class,"name","benben");
        System.out.println(s);
    }
}
