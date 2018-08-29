package com.lino.secondkill.redis;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {
    public static Logger logger = LoggerFactory.getLogger(RedisService.class);
    @Autowired
    JedisPool jp ;
    /*
    * 获取对象
    * */
    public <T> T get(KeyPrefix prefix,String key,Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis=jp.getResource();
            //真正的key
            String realKey = prefix.getPrefix()+key;
            logger.info("============================redis获取的key："+realKey);

            String str= jedis.get(realKey);
            T t= stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }
//将字符串转换成bean
    private <T> T stringToBean(String str, Class<T> clazz) {
         if(str==null || str.length()<=0||clazz==null){
             return null;
         }
        if(clazz==int.class||clazz==Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz==String.class){
            return (T)str;
        }else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(str);
        }else{
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
        
    }

    /*
    * 设置对象
    * */
    public <T> boolean set(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try {
            jedis=jp.getResource();
            //真正的key
            String realKey = prefix.getPrefix()+key;
            logger.info("================realkey:"+realKey);
            String str= beanToString(value);
            if(str==null || str.length()<=0) return false;
            //过期操作
            int second = prefix.expireSeconds();
            if(second<=0){
                //永不过期，直接生成
                jedis.set(realKey,str);
            }else{
                //设置过期时间
                jedis.setex(realKey,second,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }
//bean转换成字符串
    private <T> String beanToString(T value) {
        if(value==null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return ""+value;
        }else if(clazz==String.class){
            return (String)value;
        }else if(clazz==long.class||clazz==Long.class){
            return ""+value;
        }else{
            return JSON.toJSONString(value);
        }
    }

    //返回连接到连接池
    private void returnToPool(Jedis jedis) {
        if(jedis!=null){
            jedis.close();
        }
    }

    /*
    * 增加值
    * */
    public <T> Long incr(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try{
            jedis=jp.getResource();
            String realKey = prefix.getPrefix()+key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }
    /*
    * 减少值
    * */
    public <T> Long decr(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try{
            jedis=jp.getResource();
            String realKey = prefix.getPrefix()+key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     * */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jp.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }



    /*
     * 删除
     * */
    public <T> boolean delete(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis=jp.getResource();
            //真正的key
            String realKey = prefix.getPrefix()+key;
            Long del = jedis.del(realKey);
            return del>0;
        }finally {
            returnToPool(jedis);
        }
    }


}
