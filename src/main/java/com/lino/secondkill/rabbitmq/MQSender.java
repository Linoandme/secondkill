package com.lino.secondkill.rabbitmq;

import com.lino.secondkill.redis.RedisService;
import com.lino.secondkill.vo.SecondkillMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class MQSender {
    private static Logger logger = LoggerFactory.getLogger(MQSender.class);
    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object message){
        String msg = RedisService.beanToString(message);
        logger.info("MQSender="+message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }

    public void secondkillSend(SecondkillMessage message) {
        String bean = RedisService.beanToString(message);
        logger.info("secondkillSend="+bean);
        amqpTemplate.convertAndSend(MQConfig.SECONDKILL_QUEUE,bean);

    }
}
