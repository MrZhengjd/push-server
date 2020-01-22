package com.zheng.loadbalance;

import com.zheng.service.ZookeeperCuratorUtil;

public class ZookeeperRegistContext {

    private Long serverId;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    private String path;
    private ZookeeperCuratorUtil curatorUtil;

    public ZookeeperCuratorUtil getCuratorUtil() {
        return curatorUtil;
    }

    public void setCuratorUtil(ZookeeperCuratorUtil curatorUtil) {
        this.curatorUtil = curatorUtil;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    private Object data;

    public ZookeeperRegistContext(Long serverId,String path, Object data) {
        super();
        this.path = path;
        this.data = data;
        this.serverId = serverId;
    }
}
