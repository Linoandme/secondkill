package com.lino.secondkill.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public static final String QUEUE="queue";
    public static final String SECONDKILL_QUEUE="secondkill_queue";
    @Bean
    public Queue queue(){
        return new Queue(SECONDKILL_QUEUE,true);
    }
}
