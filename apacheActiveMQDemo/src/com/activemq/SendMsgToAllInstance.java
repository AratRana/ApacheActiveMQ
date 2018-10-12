package com.activemq;

import javax.jms.Message;
import javax.jms.MessageListener;

public class SendMsgToAllInstance implements MessageListener {

	@Override
	public void onMessage(Message arg0) {
		System.out.println("distributed message-------------");
		
		// We have call to dao layer to to fetch some data and cached it

	}

}
