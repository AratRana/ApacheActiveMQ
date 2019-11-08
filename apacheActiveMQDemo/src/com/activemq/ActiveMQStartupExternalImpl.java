package activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

public class ActiveMQStartupExternalImpl implements ActiveMQStartupInterface {
    private static final Logger logger = LoggerFactory.getLogger(ActiveMQStartupExternalImpl.class);

    @Override
    public void start() throws Exception {
        canConnectionEstablish();
    }

    private void canConnectionEstablish() throws Exception {

        ActiveMQContext context = new ActiveMQContext();
        context.loadJndiProperties();
        TopicConnection topicConnection = null;
        try {
            // Check activemq connection. If it is not able to connect, shutdown the server
            new ActiveMQConnectionFactory(ActiveMQUtil.getBrokerUrl()).createConnection();
            TopicConnectionFactory factory = (ActiveMQConnectionFactory) ActiveMQContext.getContext()
                    .lookup("distributedMsgFactory");
            topicConnection = ActiveMQUtil.getTopicConnection(factory);
            //creating topic connection only to check username and password is correct or not
            topicConnection.start();
        } catch (Exception e) {
            logger.error("Shutting down the server due to Activemq Exception", e.getMessage());
            throw e;
        } finally {
            try {
                if (topicConnection != null)
                    topicConnection.close();
            } catch (JMSException e) {
                logger.error("Unable to close topic connection", e);
            }
        }
    }
}
