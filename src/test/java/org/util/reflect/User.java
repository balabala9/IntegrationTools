package org.util.reflect;

/**
 * Created by li on 17-6-16.
 */
public class User extends Person implements IUser{
    private String name;
    private String age;
    public  String nickname;


    User(){
        this.name="li";
        this.age="18";
        this.nickname="mumu";
    }

    User(String name, String age, String nickname){
        this.name=name;
        this.age=age;
        this.nickname=nickname;
    }
    public String toString(){

        return name+"年龄"+age+"岁";
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
