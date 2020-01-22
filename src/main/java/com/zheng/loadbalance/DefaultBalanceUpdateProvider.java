package com.zheng.loadbalance;

import com.zheng.service.ZookeeperCuratorUtil;
import com.zheng.util.SocketAddressUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DefaultBalanceUpdateProvider implements com.zheng.loadbalance.BalanceUpdateProvider {

    private String serverPath;
//    private ZkClient client;
    private ZookeeperCuratorUtil curatorUtil = ZookeeperCuratorUtil.getInstance();
    private final AtomicInteger balance = new AtomicInteger(1);

//    private ZookeeperCuratorUtil client;

    public DefaultBalanceUpdateProvider(String serverPath) {
        this.serverPath = serverPath +"/"+ SocketAddressUtil.ip + ":" + SocketAddressUtil.port;

    }

    @Override
    public boolean addBalance(Integer step) {
//        Stat stat = new Stat();
        while (true) {
            try {


                String result = curatorUtil.readByPath(this.serverPath);
                String[] results = result.split(":");
                String host = results[0];
                String port = results[1];
                Integer tempBalance = Integer.valueOf(results[2]);
//                Integer result =Integer.valueOf( client.readData(serverPath, stat));
                balance.weakCompareAndSet(tempBalance,tempBalance +step);
//                client.writeData(serverPath, balance.get(), stat.getVersion());
                String content = host+":"+port+":"+balance.get();
                curatorUtil.write(serverPath,content);
                return true;
            } catch (Exception e) {
                log.error("exception on add balance " + e);
                return false;
            }
        }

    }

    @Override
    public boolean reduceBalance(Integer step) {
//        Stat stat = new Stat();
        while (true) {
            try {


                String result = curatorUtil.readByPath(this.serverPath );
                String[] results = result.split(":");
                String host = results[0];
                String port = results[1];
                Integer tempBalance = Integer.valueOf(results[2]);
//                Integer result =Integer.valueOf( client.readData(serverPath, stat));
                balance.weakCompareAndSet(tempBalance,tempBalance -step);
//                client.writeData(serverPath, balance.get(), stat.getVersion());
                String content = host+":"+port+":"+balance.get();
                curatorUtil.write(serverPath,content);
                return true;
            } catch (Exception e) {
                log.error("reduce error " + e);
                return false;
            }
        }

    }
}
