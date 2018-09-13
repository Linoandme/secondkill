package com.lino.secondkill.access;

import com.lino.secondkill.domain.SecondkillUser;

public class UserContext {
    private static ThreadLocal<SecondkillUser> userHolder = new ThreadLocal<SecondkillUser>();
    public static void setUser(SecondkillUser secondkillUser){
        userHolder.set(secondkillUser);
    }
    public static SecondkillUser getUser(){
        return userHolder.get();
    }
}
