package com.lino.secondkill.redis;

public class UserKey extends BasePrefix {

    //private 防止外面给实例化了
    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById=new UserKey("id");
    public static UserKey getByName=new UserKey("name");
}
