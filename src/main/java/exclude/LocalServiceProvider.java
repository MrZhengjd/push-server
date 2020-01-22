package exclude;

import com.zheng.util.SocketAddressUtil;

import java.util.ResourceBundle;

public class LocalServiceProvider implements ServiceProvider {
    private final static String serviceName = ResourceBundle.getBundle("application").getString("service.name");
    @Override
    public ServiceData getServiceByName() {
        ServiceData serviceData = new ServiceData();

        serviceData.setHost(SocketAddressUtil.ip);
        serviceData.setPort(Integer.valueOf(SocketAddressUtil.port));
        serviceData.setServiceName(serviceName);
        return serviceData;
    }

    
}
