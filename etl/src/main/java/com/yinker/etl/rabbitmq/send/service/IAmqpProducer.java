package com.yinker.etl.rabbitmq.send.service;

public interface IAmqpProducer {

	void send (String queueName, String message);
	
}
