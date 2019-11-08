package activemq.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

import activemq.ActiveMQContext;
import org.apache.activemq.ActiveMQConnectionFactory;


public class MQConfiguration {
	private static final Map configurations = new HashMap();
	private String mqConfig;
	private String topicName;
	private TopicConnection topicConnection = null;

	private MQConfiguration(String mqConfig, String user, String password) {
		this.mqConfig = mqConfig;

		try {
			String topicFactoryConName = ActiveMQContext.getProperty(mqConfig);
			this.topicName = (mqConfig.equals("distributedMsg") ? ActiveMQContext.getProperty("distributedTopic")
					: ActiveMQContext.getProperty("normalTopic"));
			TopicConnectionFactory factory = (ActiveMQConnectionFactory) ActiveMQContext.getContext()
					.lookup(topicFactoryConName);
			if (ActiveMQContext.getProperty("activemq.external").equals("true")) {
				this.topicConnection = factory.createTopicConnection(ActiveMQContext.getProperty("activemq.user"),ActiveMQContext.getProperty("activemq.password"));
			} else {
				this.topicConnection = factory.createTopicConnection("system","manager");
			}
			this.topicConnection.start();
		} catch (Exception e) {
			System.out.println("error: " + e);
		}
	}

	public static MQConfiguration getConfiguration(String mqConfig) {
		if (mqConfig == null || "".equals(mqConfig)) {
			throw new IllegalArgumentException("mqConfig is null or empty");
		}

		MQConfiguration config = null;

		if (config != null) {
			return config;
		}
		synchronized (configurations) {
			config = (MQConfiguration) configurations.get(mqConfig);
			if (config == null) {
				config = new MQConfiguration(mqConfig, "system", "manager");
			}
			configurations.put(mqConfig, config);
		}

		return config;
	}

	public String getMqConfig() {
		return this.mqConfig;
	}

	public TopicSession createTopicSession(boolean isTransacted, int autoAcknowledge) throws JMSException {
		if (this.topicConnection == null) {
			IllegalStateException ise = new IllegalStateException("topic connection not configured");
			throw ise;
		}
		return this.topicConnection.createTopicSession(isTransacted, autoAcknowledge);
	}

	public Topic getTopic() {
		try {
			return (Topic) ActiveMQContext.getContext().lookup(this.topicName);
		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}
}
