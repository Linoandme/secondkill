package com.lino.secondkill.service;

import com.lino.secondkill.dao.SecondkillDao;
import com.lino.secondkill.domain.Goods;
import com.lino.secondkill.domain.OrderInfo;
import com.lino.secondkill.domain.SecondkillOrder;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.redis.GoodsKey;
import com.lino.secondkill.redis.RedisService;
import com.lino.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecondkillService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo secondkill(SecondkillUser secondkillUser, GoodsVo goods) {
        //减库存，下订单，写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if(success){
            return orderService.createOrder(secondkillUser,goods);
        }else {
            //做一个标记，表示东西已经卖完了
            setGoodsOver(goods.getGoods_id());
            return  null;
        }
    }

    private void setGoodsOver(Long goods_id) {
        redisService.set(GoodsKey.goodsOver,""+goods_id,true);
    }



    public long getSecondkillResult(Long id, long goods_id) {
        SecondkillOrder secondkillOrderByUseridGoodsid = orderService.getSecondkillOrderByUseridGoodsid(id, goods_id);
        if(secondkillOrderByUseridGoodsid!=null){
            return secondkillOrderByUseridGoodsid.getOrder_id();
        }else{
            if(!getGoodsOver(goods_id)){
                return -1;
            }else{
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goods_id) {
        return redisService.exists(GoodsKey.goodsStock,String.valueOf(goods_id));
    }


}
