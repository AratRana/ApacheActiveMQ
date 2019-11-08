package activemq;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.network.NetworkConnector;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ActiveMQStartupInternalImpl implements ActiveMQStartupInterface {
    @Override
    public void start() {
        startBrokerService();
    }


    private final String bindAddress;
    private final String dataDirectory;
    private BrokerService broker = new BrokerService();
    protected final int numRestarts = 3;
    protected final int networkTTL = 2;
    protected final int consumerTTL = 2;
    protected final boolean dynamicOnly = true;
    protected final String networkBroker;
    protected final String jmxPort;

    public ActiveMQStartupInternalImpl() {
        ActiveMQContext context = new ActiveMQContext();
        context.loadJndiProperties();
        bindAddress = ActiveMQContext.getProperty("java.naming.provider.url");
        dataDirectory = ActiveMQContext.getProperty("activemq.data.directory");
        networkBroker = ActiveMQContext.getProperty("activemq.network.connector");
        jmxPort = ActiveMQContext.getProperty("activemq.jmx.port");
    }

    // Start activemq broker service
    public void startBrokerService() {
        try {
            broker.setDataDirectory("../" + dataDirectory);
            broker.setBrokerName(dataDirectory);
            broker.setUseShutdownHook(true);
            TransportConnector connector = new TransportConnector();
            connector.setUri(new URI(bindAddress));

            //broker.setPlugins(new BrokerPlugin[]{new JaasAuthenticationPlugin()});
            ManagementContext mgContext = new ManagementContext();
            if (networkBroker != null && !networkBroker.isEmpty()) {
                NetworkConnector networkConnector = broker.addNetworkConnector(networkBroker);
                networkConnector.setName(dataDirectory);
                mgContext.setConnectorPort(Integer.parseInt(jmxPort));
                broker.setManagementContext(mgContext);
                configureNetworkConnector(networkConnector);
            }

            /**
             * simple authentication
             */
            List<AuthenticationUser> users = new ArrayList<>();
            users.add(new AuthenticationUser(ActiveMQContext.getProperty("activemq.user"), ActiveMQContext.getProperty("activemq.password"), "users,admins"));
            SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin(users);
            broker.setPlugins(new BrokerPlugin[]{authentication});

            broker.setNetworkConnectorStartAsync(true);
            broker.addConnector(connector);
            broker.start();
        } catch (Exception e) {
            System.out.println("Failed to start Apache MQ Broker : " + e);
        }
    }

    private void configureNetworkConnector(NetworkConnector networkConnector) {
        networkConnector.setDuplex(true);
        networkConnector.setNetworkTTL(networkTTL);
        networkConnector.setDynamicOnly(dynamicOnly);
        networkConnector.setConsumerTTL(consumerTTL);
        //networkConnector.setStaticBridge(true);
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
