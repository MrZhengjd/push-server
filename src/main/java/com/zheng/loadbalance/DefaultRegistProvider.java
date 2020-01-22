package com.zheng.loadbalance;

import com.zheng.service.ZookeeperCuratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultRegistProvider implements RegistProvider {
    private final ZookeeperCuratorUtil curatorUtil = ZookeeperCuratorUtil.getInstance();
    @Override
    public void regist(Object context) throws Exception {
        ZookeeperRegistContext registContext = (ZookeeperRegistContext) context;
        ServerData data = (ServerData) registContext.getData();
        String path = registContext.getPath();
        String tempPath = path +"/"+data.getHost()+":"+data.getPort();
//        ZkClient client = registContext.getZkClient();
//
//        try {
//            client.createEphemeral(tempPath,data.getBalance().toString());
//        }catch (ZkNoNodeException e){
//            log.error("node don't exist "+ e);
////            String parentDir = path.substring(0,path.lastIndexOf("/"));
//            client.createPersistent(path,true);
//            regist(registContext);
//        }catch (Exception e ){
//            log.error("node don't exist "+ e);
//        }
        String content = data.getHost()+":"+data.getPort() +":"+data.getBalance();
        curatorUtil.write(tempPath,content);
    }



    @Override
    public void unRegist(Object context) throws Exception {
        return;
    }
}
