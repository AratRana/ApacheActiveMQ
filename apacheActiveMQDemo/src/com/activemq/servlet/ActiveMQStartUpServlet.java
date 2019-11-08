package activemq.servlet;

import activemq.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Servlet implementation class ActiveMQStartUpServlet
 */
@WebServlet(value = "/activeMQStartUpServlet", loadOnStartup = 1)
public class ActiveMQStartUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ActiveMQStartup mqStartup = null;
	private static final Map pooledPublishers = new HashMap();

	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("starting servelt--------------");
		super.init(config);
		mqStartup = new ActiveMQStartup();
		try {
			mqStartup.start();
		} catch (Throwable e) {
			AppServerAdminUtils adminUtils = new TomcatAdminUtils();
			adminUtils.shutdownServer();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println(req.getParameter("distributedMsg"));
		String mqConfig = null;
		String distributedMsg = req.getParameter("distributedMsg");
		String simpleMsg = req.getParameter("simpleMsg");
		if (distributedMsg != null && !distributedMsg.equals(""))
			mqConfig = "distributedMsg";
		else if (simpleMsg != null && !simpleMsg.equals(""))
			mqConfig = "simpleMsg";
		MQPublisher publisher = acquirePublisher(mqConfig);
		try {
			publisher.publish(mqConfig);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			releasePublisher(publisher);
		}
	}

	@SuppressWarnings("unchecked")
	private void releasePublisher(MQPublisher publisher) {
		if (publisher == null)
			return;
		@SuppressWarnings("rawtypes")
		LinkedList publishers;
		TestPublisher poolablePublisher = (TestPublisher) publisher;
		publishers = getPooledPublishers(poolablePublisher.getConfigurationName());
		synchronized (publishers) {
			publishers.addLast(poolablePublisher);
		}

	}

	private MQPublisher acquirePublisher(String mqConfig) {
		LinkedList publishers = getPooledPublishers(mqConfig);
		MQPublisher publisher = getMQPubliser(publishers);
		if (publisher != null)
			return publisher;
		try {
			if (mqConfig.equals("distributedMsg"))
				return new TestPublisher(MQConfiguration.getConfiguration("distributedMsg"),
						new SendMsgToAllInstance());
			else
				return new TestPublisher(MQConfiguration.getConfiguration("simpleMsg"), new SendMsg());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private LinkedList getPooledPublishers(String mqConfig) {
		LinkedList publishers = null;
		publishers = (LinkedList) pooledPublishers.get(mqConfig);
		if (publishers == null) {
			synchronized (pooledPublishers) {
				publishers = (LinkedList) pooledPublishers.get(mqConfig);
				if (publishers == null) {
					publishers = new LinkedList();
					pooledPublishers.put(mqConfig, publishers);
				}
			}
		}
		return publishers;
	}

	private MQPublisher getMQPubliser(LinkedList publishers) {
		synchronized (publishers) {
			while (!publishers.isEmpty()) {
				TestPublisher publisher = (TestPublisher) publishers.removeFirst();
				return publisher;
			}
		}
		return null;
	}

}
