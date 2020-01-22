package exclude;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceData implements Serializable,Comparable<ServiceData> {

    private String serviceName;
    private String host;

    public ServiceData(String serviceName, String host, Integer port) {
        this.serviceName = serviceName;
        this.host = host;
        this.port = port;
        balance = new AtomicInteger(1);
    }

    public ServiceData() {
        balance = new AtomicInteger(1);
    }

    private Integer port;
    private static final long serialVersionUID = -3893659132743931683L;

    public void setBalance(AtomicInteger balance) {
        this.balance = balance;
    }

    public AtomicInteger getBalance() {
        return balance;
    }

    public void setBalanceWithStep(int expect, int update){
        balance.compareAndSet(expect,update);
    }

    @Override
    public String toString() {
        return "ServiceData{" +
                "serviceName='" + serviceName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", balance=" + balance +
                '}';
    }

    private AtomicInteger balance;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public int compareTo(ServiceData o) {
        return ((Integer)this.getBalance().get()).compareTo((Integer)o.getBalance().get());
    }
}
