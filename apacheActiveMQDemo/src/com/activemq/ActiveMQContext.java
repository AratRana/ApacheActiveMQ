package activemq;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ActiveMQContext {
	private static final Object singletonLock = new Object();
    static Context context = null;
    static Properties properties = null;
    private boolean loaded = false;

    public void loadJndiProperties() {
        try {
            if (!loaded) {
                synchronized (singletonLock) {
                    URL url = getClass().getClassLoader().getResource("activemq-jndi.properties");
                    properties = new Properties();
                    properties.load(url.openStream());
                    context = new InitialContext(properties);
                    loaded = true;
                }
            }
        } catch (IOException | NamingException ie) {
        	System.out.println("Failed to load apachemq jndi propIMetadataCacheerties"+ ie);
        }
    }

    public static Context getContext() {
        return context;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
