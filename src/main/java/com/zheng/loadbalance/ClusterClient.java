package com.zheng.loadbalance;

import org.apache.curator.CuratorZookeeperClient;

public abstract class ClusterClient {
    public abstract void connect();

    public abstract String getAppServer();
    private CuratorZookeeperClient client;

    public ClusterClient(CuratorZookeeperClient client) {
        this.client = client;
    }

    public void join(String client){


    }
}
