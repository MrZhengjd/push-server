package exclude;

public class ServiceProviderFactory {

    static class InstanceHolder{
        static ServiceProviderFactory instance = new ServiceProviderFactory();
    }
    public static ServiceProviderFactory getInstance(){
        return InstanceHolder.instance;
    }

    public static ServiceProvider getLocalServiceProvider(){
        return new LocalServiceProvider();
    }
    public static ServiceProvider getZookeeperServiceProvider(){
        return new ZookeeperServiceProvider();
    }
}
