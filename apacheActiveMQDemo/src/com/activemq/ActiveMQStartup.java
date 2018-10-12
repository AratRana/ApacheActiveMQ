package com.activemq;

import org.apache.activemq.broker.BrokerService;

public class ActiveMQStartup {
	private final String bindAddress;
	private final String dataDirectory;
	private BrokerService broker = new BrokerService();

	public ActiveMQStartup() {
		ActiveMQContext context = new ActiveMQContext();
		context.loadJndiProperties();
		bindAddress = ActiveMQContext.getProperty("java.naming.provider.url");
		dataDirectory = ActiveMQContext.getProperty("activemq.data.directory");
	}

	// Start activemq broker service
	public void startBrokerService() {
		try {
			broker.setDataDirectory("../" + dataDirectory);
			broker.setDataDirectory("../../" + dataDirectory);
			broker.addConnector(bindAddress);
			broker.start();
		} catch (Exception e) {
			System.out.println("Failed to start Apache MQ Broker : " + e);
		}
	}

	// Stop broker service
	public void stopBrokerService() {
		try {
			broker.stop();
		} catch (Exception e) {
			System.out.println("Unable to stop the ApacheMQ Broker service " + e);
		}
	}
}
