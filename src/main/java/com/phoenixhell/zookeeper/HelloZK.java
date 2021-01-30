package com.phoenixhell.zookeeper;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class HelloZK {
    private static final String CONNECT_STRING = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3 * 10000;
    private static final String NODE_PATH = "/hello";
    private static final String NODE_VALUE = "value11";

    public static void main(String[] args) {
        HelloZK helloZK = null;
        ZooKeeper zookeeper = null;
        try {
            helloZK = new HelloZK();
            zookeeper = helloZK.getZookeeper();
            Stat exists = zookeeper.exists(NODE_PATH,false);
            if(exists==null){
                helloZK.createZnode(zookeeper, NODE_PATH, NODE_VALUE);
            }
            String znodeValue = helloZK.getZnodeValue(zookeeper, NODE_PATH);
            System.out.println(znodeValue);
            log.error(znodeValue+"-------------------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (helloZK != null) {
                    helloZK.closeConnection(zookeeper);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ZooKeeper getZookeeper() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent event) {

            }
        });
    }

    public void closeConnection(ZooKeeper zooKeeper) throws InterruptedException {
        if (zooKeeper != null) {
            zooKeeper.close();
        }
    }

    public void createZnode(ZooKeeper zooKeeper, String nodPath, String nodeValue) throws KeeperException, InterruptedException {
        zooKeeper.create(nodPath, nodeValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public String getZnodeValue(ZooKeeper zooKeeper, String nodePath) throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData(nodePath, false, new Stat());
        String s=new String(data);
        //此方法不可以[18,11] 真是=就是数据了 包括string.valueof
//        String s = Arrays.toString(data);
        return s;
    }
}
