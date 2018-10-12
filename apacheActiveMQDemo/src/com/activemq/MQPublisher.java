package com.activemq;

import javax.jms.JMSException;

public interface MQPublisher {
	public void publish(String msg) throws JMSException;
}
