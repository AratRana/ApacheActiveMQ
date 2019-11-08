package activemq;

public class TomcatAdminUtils implements AppServerAdminUtils {
    @Override
    public void shutdownServer() {
        System.exit(1);
    }
}
