package com.activemq;

import javax.jms.Message;
import javax.jms.MessageListener;

public class SendMsg  implements MessageListener {

	@Override
	public void onMessage(Message arg0) {
		System.out.println("simple message classes");
		
		// We have db insert/update call to dao layer here.
		
	}

}
