//=========================================================================================================
//			Copyright <1995-2019> JDA Software Group, Inc. All rights reserved.
//			LICENSE OF THIS PROGRAM IS ONLY ENTITLED TO ACCESS THE CONFIGURATION(S) SPECIFIED IN ITS
//			SOFTWARE LICENSE AGREEMENT WITH JDA.  ACCESS OF ANY OTHER CONFIGURATION IS A DIRECT VIOLATION
//			OF THE TERMS OF THE SOFTWARE LICENSE AGREEMENT, AND JDA RETAINS ALL ITS LEGAL RIGHTS TO ENFORCE
//			SUCH AGREEMENT.
//			This product may be protected by one or more United States and foreign patents.
//			For information on patents, see https://www.jda.com/legal/patents.
//=========================================================================================================

package activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * @author 1021997
 */
public class ActiveMQUtil {
    private static final Logger logger = LoggerFactory.getLogger(ActiveMQUtil.class);

    /**
     * check if active mq is configured externally
     * @return
     */
    public static boolean isActivemqExternal() {
        return ActiveMQContext.getProperty("activemq.external").equals("true");
    }

    /**
     *
     * @return
     */
    private static String getActiveMQuser() {
        return ActiveMQContext.getProperty("activemq.user");
    }

    /**
     *
     * @return
     */
    private static String getActiveMQPassword() {
        return ActiveMQContext.getProperty("activemq.password");
    }

    /**
     *
     * @return
     */
    public static String getBrokerUrl() {
        return ActiveMQContext.getProperty("java.naming.provider.url");
    }

    /**
     *
     * @param qFactory
     * @returnj
     */
    public static QueueConnection getQueueConnection(QueueConnectionFactory qFactory) {
        QueueConnection qCon = null;
        try {
            if (isActivemqExternal()) {
                qCon = qFactory.createQueueConnection(getActiveMQuser(), getActiveMQPassword());
            } else {
                qCon = qFactory.createQueueConnection();
            }
        } catch (JMSException e) {
            logger.error("Error in creating queue connection", e.getMessage());
        }
        return qCon;
    }

    /**
     *
     * @param topicFactory
     * @return
     */
    public static TopicConnection getTopicConnection(TopicConnectionFactory topicFactory) {
        TopicConnection topicCon = null;
        try {
            if (isActivemqExternal()) {
                topicCon = topicFactory.createTopicConnection(getActiveMQuser(), getActiveMQPassword());
            } else {
                topicCon = topicFactory.createTopicConnection();
            }
        } catch (JMSException e) {
            logger.error("Error in creating topic connection", e.getMessage());
        }
        return topicCon;
    }
}
