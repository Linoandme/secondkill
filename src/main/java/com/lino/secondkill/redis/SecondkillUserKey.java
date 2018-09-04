package com.lino.secondkill.redis;

public class SecondkillUserKey extends BasePrefix {
    private static final int TOKEN_EXPIRE=3600*24*5;

    //private 防止外面给实例化了
    private SecondkillUserKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }
    private SecondkillUserKey(String prefix) {
        super(prefix);
    }


    public static SecondkillUserKey token=new SecondkillUserKey(TOKEN_EXPIRE,"token");
    public static SecondkillUserKey getById=new SecondkillUserKey("getById");
}
