package com.lino.secondkill.service;

import com.lino.secondkill.dao.SecondkillDao;
import com.lino.secondkill.domain.Goods;
import com.lino.secondkill.domain.OrderInfo;
import com.lino.secondkill.domain.SecondkillOrder;
import com.lino.secondkill.domain.SecondkillUser;
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

    @Transactional
    public OrderInfo secondkill(SecondkillUser secondkillUser, GoodsVo goods) {
        //减库存，下订单，写入秒杀订单
        goodsService.reduceStock(goods);
        return orderService.createOrder(secondkillUser,goods);

    }
}
