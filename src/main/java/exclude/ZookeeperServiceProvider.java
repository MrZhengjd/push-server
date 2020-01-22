package exclude;

import com.zheng.service.ZookeeperCuratorUtil;
import com.zheng.util.SocketAddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.net.InetAddress;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZookeeperServiceProvider extends AbstractServiceProvider implements ServiceRegist, Watcher {
    private ZookeeperCuratorUtil util;
    private final static String PROVIDER = ResourceBundle.getBundle("zookeeper").getString("zookeeper.path.provider");
    private final static String CONSUMER = ResourceBundle.getBundle("zookeeper").getString("zookeeper.path.consumer");
    private static CountDownLatch latch = new CountDownLatch(1);
    private final static String serviceName = ResourceBundle.getBundle("application").getString("service.name");
    public ZookeeperServiceProvider() {

        try {
            util = ZookeeperCuratorUtil.getInstance();
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ServiceData> getItemlist() {
//        List<ServiceData> serviceData = util.getChilden(serviceName);
        return null;
    }

    @Override
    public ServiceData agolithem(List<ServiceData> serviceDatas) {
        return null;
    }

    @Override
    public ServiceData getServiceByName() {
        try {
            InetAddress address = SocketAddressUtil.getLocalHostLANAddress();
            if(address != null){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void regist(ServiceData serviceData) {
        String path = serviceData.getServiceName() + "/" + PROVIDER + serviceData.getHost();
//        String content = serviceData.getHost() + ";" + serviceData.getPort();
        try {
            util.write(path, serviceData.toString());
        } catch (Exception e) {
            log.error("zookeeper error " + e);
        }

    }

    @Override
    public void regist() {
        String serviceName = ResourceBundle.getBundle("application").getString("service.name ");

        ServiceData serviceData = new ServiceData(serviceName, SocketAddressUtil.ip,Integer.valueOf( SocketAddressUtil.port));
        regist(serviceData);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected)
            latch.countDown();
    }
}
