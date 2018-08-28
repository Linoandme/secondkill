package com.lino.secondkill.service;

import com.lino.secondkill.dao.OrderDao;
import com.lino.secondkill.domain.OrderInfo;
import com.lino.secondkill.domain.SecondkillOrder;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.vo.GoodsVo;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderDao orderDao;

    public SecondkillOrder getSecondkillOrderByUseridGoodsid(Long id, long goods_id) {
        return orderDao.getSecondkillOrderByUseridGoodsid(id,goods_id);
    }
    @Transactional
    public OrderInfo createOrder(SecondkillUser secondkillUser, GoodsVo goods) {
        //写入普通订单表
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreate_date(new Date());
        orderInfo.setDelivery_addr_id((long)123);
        orderInfo.setGoods_count(1);
        orderInfo.setGoods_id(goods.getGoods_id());
        orderInfo.setGoods_name(goods.getGoods_name());
        orderInfo.setGoods_price(goods.getGoods_price());
        orderInfo.setOrder_channel(1);
        orderInfo.setStatus(0);
        orderInfo.setUser_id(secondkillUser.getId());
        orderDao.insert(orderInfo);
        //注意：
        long order_id =orderInfo.getId();
        logger.info("==============返回来的order_id:"+order_id);


        //写入秒杀订单表
        SecondkillOrder secondkillOrder = new SecondkillOrder();
        secondkillOrder.setGoods_id(goods.getGoods_id());
        secondkillOrder.setOrder_id(order_id);
        secondkillOrder.setUser_id(secondkillUser.getId());
        int secondkill_order_id = orderDao.insertSecondkillOrder(secondkillOrder);


        return orderInfo;
    }
}
