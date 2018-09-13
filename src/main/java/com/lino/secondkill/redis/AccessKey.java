package com.lino.secondkill.redis;

public class AccessKey extends BasePrefix {

    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }


    public static AccessKey with(int expireSeconds){
        return new AccessKey(expireSeconds,"accessKey");
    }

}
