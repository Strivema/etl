package com.yinker.etl.rabbitmq.send.service.impl;

import com.yinker.etl.rabbitmq.send.service.IAmqpProducer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("etlSendService")
public class EtlSendService implements IAmqpProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void send (String queueName, String message) {
        amqpTemplate.convertAndSend(queueName, message);
    }
}
