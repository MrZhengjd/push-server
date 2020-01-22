package com.zheng.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class ZookeeperCuratorUtil {

    private final static int CONNECTION_TIMEOUT = Integer.valueOf(ResourceBundle.getBundle("zookeeper").getString("zookeeper.connect.timeout"));
    //    private final static int SESSION_TIMEOUT = Integer.valueOf(ResourceBundle.getBundle("zookeeper").getString("zookeeper.session.timeout"));
    public final static String SERVER = ResourceBundle.getBundle("zookeeper").getString("zookeeper.server.location");

    private final int RETRY_TIME = Integer.MAX_VALUE;
    private final int RETRY_INTERVAL = Integer.valueOf(ResourceBundle.getBundle("zookeeper").getString("zookeeper.retry.interval"));

    private CuratorFramework curator = null;

    private volatile static ZookeeperCuratorUtil client;

    private static Map<String, Map<String, String>> zkCacheMap = new HashMap<>();

    private CuratorFramework newCurator(String zkServer) {
        return CuratorFrameworkFactory.builder().connectString(zkServer).retryPolicy(new RetryNTimes(RETRY_TIME, RETRY_INTERVAL))
                .connectionTimeoutMs(CONNECTION_TIMEOUT).build();
    }

    private ZookeeperCuratorUtil(String server) {
        if (curator == null) {
            curator = newCurator(server);
            curator.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                @Override
                public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                    if (connectionState == ConnectionState.LOST) {
                        log.info("lost connection ");
                    } else if (connectionState == ConnectionState.CONNECTED) {
                        log.info(" connection server");
                    } else if (connectionState == ConnectionState.RECONNECTED) {
                        log.info("reconnection zookeeper");
                        for (ZkStateListener s : stateListeners) {
                            s.reconnected();
                        }
                    }
                }
            });
            curator.start();
        }
    }


    public List<String> getChilden(String path) throws Exception {
        if (curator.checkExists().forPath(path) != null) {
            List<String> childrenPath = curator.getChildren().forPath(path);
            return childrenPath;
        } else {
            log.error("no path exist on server");
            return null;
        }
    }

    public static ZookeeperCuratorUtil getInstance() {
        if (client == null) {
            synchronized (ZookeeperCuratorUtil.class) {
                if (client == null) {
                    client = new ZookeeperCuratorUtil(SERVER);
                }
            }
        }
        return client;
    }



    //    public void writePersistenPath(String path){
//        try {
//            StringBuilder builder = new StringBuilder(path);
//            String writePath = curator.create()
//                    .withMode(CreateMode.PERSISTENT).forPath(builder.toString(), null);
//
//        }catch (Exception e){
//            log.error("zookeeper error " + e);
//        }
//    }

    public void writePersistance(String path, String content)throws Exception{
        StringBuilder builder = new StringBuilder(path);
        if (curator.checkExists().forPath(path)!=null){
            updateNodeContent(path,content);
            return;
        }
        curator.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT).forPath(builder.toString(), content.getBytes("utf-8"));
    }
    public void write(String path, String content) throws Exception {
        StringBuilder builder = new StringBuilder(path);
        if (curator.checkExists().forPath(path)!=null){
            updateNodeContent(path,content);
            return;
        }
        curator.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL).forPath(builder.toString(), content.getBytes("utf-8"));

    }
    public void updateNodeContent(String path, String content) throws Exception {
        try {
            StringBuilder builder = new StringBuilder(path);
            curator.setData().forPath(builder.toString(), content.getBytes("utf-8"));
        }catch (Exception e){
            log.error("update node error");
        }
    }
    public void close() {
        if (curator != null) {
            curator.close();
            curator = null;
        }
        zkCacheMap.clear();
    }

    public List<String> readAll(String path) throws Exception {
        String parentPath = path;
        Map<String, String> cacheMap = zkCacheMap.get(path);
        List<String> list = new ArrayList<String>();
        if (cacheMap != null) {
            log.debug("read all from cache,path=" + path);
            list.addAll(cacheMap.values());
            return list;
        }
        if (curator.checkExists().forPath(path) == null) {
            log.debug("path [{}] is not exists,return null", path);
            return null;
        } else {
            cacheMap = new HashMap<String, String>();
            List<String> children = curator.getChildren().usingWatcher(new ZKWatcher(parentPath, path)).forPath(path);
            if (children == null || children.size() == 0) {
                log.debug("path [{}] has no children,return null", path);
                return null;
            } else {
                log.debug("read all from zookeeper,path=" + path);
                String basePath = path;
                for (String child : children) {
                    path = basePath + "/" + child;
                    byte[] b = curator.getData().usingWatcher(new ZKWatcher(parentPath, path))
                            .forPath(path);
                    String value = new String(b, "utf-8");
                    if (StringUtils.isNotBlank(value)) {
                        list.add(value);
                        cacheMap.put(path, value);
                    }
                }
            }
            zkCacheMap.put(parentPath, cacheMap);
            return list;
        }
    }

    public String readByPath(String path) throws Exception {
        String parentPath = path.substring(0, path.lastIndexOf("/"));

        Map<String, String> cacheMap = zkCacheMap.get(parentPath);
        if (cacheMap != null && (cacheMap.get(path) != null || "" != cacheMap.get(path))) {
            return cacheMap.get(path);
        }
        if (curator.checkExists().forPath(path) != null) {
            byte[] results = curator.getData().usingWatcher(new ZKWatcher(parentPath, path)).forPath(path);
            String value = new String(results, "utf-8");
            cacheMap = new HashMap<>();
            cacheMap.put(path,value);
            zkCacheMap.put(parentPath,cacheMap);
            return value;
        }
        return null;
    }

    public String readRandom(String path) throws Exception {
        String parentPath = path;
        Map<String, String> cacheMap = zkCacheMap.get(parentPath);
        if (cacheMap != null && !cacheMap.isEmpty()) {
            log.debug("get random value from cache,path=" + path);
            return getRandomValue4Map(cacheMap);
        }
        if (curator.checkExists().forPath(parentPath) == null) {
            log.debug("path [{}] is not exists,return null", path);
            return null;
        }
        log.debug("read random from zookeeper,path=", path);
        cacheMap = new HashMap<>();
        List<String> list = curator.getChildren().usingWatcher(new ZKWatcher(parentPath, path)).forPath(path);
        if (list == null || list.size() == 0) {
            log.debug("path [{}] has no children return null", path);
            return null;
        }
        Random rand = new Random();
        String child = list.get(rand.nextInt(list.size()));
        path = path + "/" + child;
        byte[] b = curator.getData().usingWatcher(new ZKWatcher(parentPath, path))
                .forPath(path);
        String value = new String(b, "utf-8");
        if (StringUtils.isNotBlank(value)) {
            cacheMap.put(path, value);
            zkCacheMap.put(parentPath, cacheMap);
        }
        return value;

    }

    private String getRandomValue4Map(Map<String, String> cacheMap) {

        Object[] values = cacheMap.values().toArray();
        Random random = new Random();

        return values[random.nextInt(values.length)].toString();
    }

    private final Set<ZkStateListener> stateListeners = new CopyOnWriteArraySet<ZkStateListener>();

    public void addStateListener(ZkStateListener listener) {
        stateListeners.add(listener);
    }

    private class ZKWatcher implements CuratorWatcher {

        private String parentPath;
        private String path;

        public ZKWatcher(String parentPath, String path) {
            this.parentPath = parentPath;
            this.path = path;
        }

        @Override
        public void process(WatchedEvent watchedEvent) throws Exception {
            Map<String, String> cacheMap = zkCacheMap.get(parentPath);
            if (cacheMap == null) {
                cacheMap = new HashMap<>();
            }
            if (watchedEvent.getType() == Watcher.Event.EventType.NodeDataChanged ||
                    watchedEvent.getType() == Watcher.Event.EventType.NodeCreated) {

                byte[] data = curator.getData().usingWatcher(this).forPath(path);
                cacheMap.put(path, new String(data, "utf-8"));
                log.info("add cache={}", new String(data, "utf-8"));
            } else if (watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted) {
                cacheMap.remove(path);
                log.info("remove path " + path);
            } else if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                cacheMap.clear();
                List<String> childrens = curator.getChildren().usingWatcher(new ZKWatcher(parentPath, path))
                        .forPath(path);
                if (childrens != null && !childrens.isEmpty()) {
                    for (String children : childrens) {
                        String childPath = parentPath + "/" + children;
                        byte[] b = curator.getData().usingWatcher(new ZKWatcher(parentPath, childPath))
                                .forPath(childPath);
                        String value = new String(b, "utf-8");
                        if (StringUtils.isNotBlank(value)) {
                            cacheMap.put(childPath, value);
                        }

                    }
                }
            }
            zkCacheMap.put(path, cacheMap);
        }
    }
}
