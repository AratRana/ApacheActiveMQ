package activemq;

public class ActiveMQStartup {
    public void start() throws Exception {
        try {
            ActiveMQContext context = new ActiveMQContext();
            context.loadJndiProperties();
            ActiveMQStartupInterface mqStartupInterface;
            if (!ActiveMQUtil.isActivemqExternal()) {
                mqStartupInterface = new ActiveMQStartupInternalImpl();
            } else {
                mqStartupInterface = new ActiveMQStartupExternalImpl();
            }
            mqStartupInterface.start();
        } catch (Exception e) {
            throw e;
        }

    }
}
