package com.lino.secondkill.redis;

public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
}
