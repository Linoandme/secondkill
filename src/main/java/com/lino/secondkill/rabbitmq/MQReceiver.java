package com.lino.secondkill.rabbitmq;

import com.lino.secondkill.domain.OrderInfo;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.redis.RedisService;
import com.lino.secondkill.service.GoodsService;
import com.lino.secondkill.service.OrderService;
import com.lino.secondkill.service.SecondkillService;
import com.lino.secondkill.vo.GoodsVo;
import com.lino.secondkill.vo.OrderDetail;
import com.lino.secondkill.vo.SecondkillMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private SecondkillService secondkillService;
    @RabbitListener(queues = MQConfig.QUEUE)
    public void receiver(String message){
        logger.info("MQReceiver:"+message);

    }


    @RabbitListener(queues = MQConfig.SECONDKILL_QUEUE)
    public void secondkillReceiver(String message){
        logger.info("secondkillReceiver:"+message);
        SecondkillMessage sm = RedisService.stringToBean(message, SecondkillMessage.class);
        Long goodsId = sm.getGoodsId();
        SecondkillUser secondkillUser = sm.getSecondkillUser();
        //减库存，下订单，写入秒杀订单
        GoodsVo goods = goodsService.getGoodsByGoodsId(goodsId);
        OrderInfo orderInfo =  secondkillService.secondkill(secondkillUser,goods);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setGoodsVo(goods);
        orderDetail.setOrderInfo(orderInfo);


    }
}
