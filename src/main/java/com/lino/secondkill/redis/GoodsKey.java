package com.lino.secondkill.redis;

public class GoodsKey extends BasePrefix {
    private  static int  expireSeconds=60;
    //private 防止外面给实例化了
    private GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static GoodsKey goodsList=new GoodsKey(expireSeconds,"goodsList");
    public static GoodsKey goodsDetail=new GoodsKey(expireSeconds,"goodsDetail");
    public static GoodsKey goodsStock=new GoodsKey(0,"goodsStock");
    public static GoodsKey goodsOver=new GoodsKey(0,"goodsOver");

}
