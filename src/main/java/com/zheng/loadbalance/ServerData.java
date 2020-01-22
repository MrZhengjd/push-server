package com.zheng.loadbalance;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerData implements Serializable,Comparable<ServerData> {

    private static final long serialVersionUID = -3893659132743931683L;

    public void setBalance(AtomicInteger balance) {
        this.balance = balance;
    }

    public void setBalanceWithStep(int expect,int update){
        balance.compareAndSet(expect,update);
    }
    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    private AtomicInteger balance;
    private  String host;
    private  Integer port;
    private int serverId;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public ServerData() {
        balance = new AtomicInteger(1);
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public ServerData( String host, Integer port) {
        balance = new AtomicInteger(1);
        this.host = host;
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServerData{" +
                "balance=" + balance +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public AtomicInteger getBalance() {
        return balance;
    }

    @Override
    public int compareTo(ServerData o) {
        return ((Integer)this.getBalance().get()).compareTo((Integer)o.getBalance().get());
    }
}
