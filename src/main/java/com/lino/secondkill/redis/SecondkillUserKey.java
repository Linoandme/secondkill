package com.lino.secondkill.redis;

public class SecondkillUserKey extends BasePrefix {
    private static final int TOKEN_EXPIRE=60;

    //private 防止外面给实例化了
    private SecondkillUserKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }


    public static SecondkillUserKey token=new SecondkillUserKey(TOKEN_EXPIRE,"token");
}
