package com.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import com.activemq.servlet.MQConfiguration;

public class TestPublisher implements MQPublisher {
	private final String configurationName;
	private TopicSession topicSession = null;
	private TopicPublisher topicPublisher = null;

	public TestPublisher(MQConfiguration config, Object messageListener) throws JMSException {
		if (config == null) {
			throw new IllegalArgumentException("config == null");
		}
		this.configurationName = config.getMqConfig();
		this.topicSession = config.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		this.topicPublisher = this.topicSession.createPublisher(config.getTopic());
		MessageConsumer msgConsumer = this.topicSession.createConsumer(config.getTopic());
		msgConsumer.setMessageListener((MessageListener) messageListener);
	}

	@Override
	public void publish(String msg) throws JMSException {
		this.topicPublisher.publish(createMessage(msg,this.topicSession));
	}

	private Message createMessage(String msg, Session session) throws JMSException {
		TextMessage message = session.createTextMessage(msg);
		return message;
	}

}
