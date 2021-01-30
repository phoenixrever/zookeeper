package com.phoenixhell.zookeeper;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class WatchOne extends  BaseConfig {
    private ZooKeeper zooKeeper = null;

    //setter-getter
    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public static void main(String[] args) throws Exception {
        WatchOne watchOne = new WatchOne();
        watchOne.setZooKeeper(watchOne.getZookeeper());
        if(watchOne.getZookeeper()==null){
            watchOne.createZnode(NODE_PATH,"AAA");
        }
        watchOne.getZnodeValue(NODE_PATH);
        //下面2个模拟客户端程序不关接收server反馈
        System.in.read();
//        Thread.sleep(Long.MAX_VALUE);
    }

    public ZooKeeper getZookeeper() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent event) {

            }
        });
    }

    public void createZnode(String nodPath, String nodeValue) throws KeeperException, InterruptedException {
        zooKeeper.create(nodPath, nodeValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    //客户端向服务器第一次取值之后设置watch
    public String getZnodeValue(String nodePath) throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData(nodePath, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                log.error("changes--------" + watchedEvent.getState() + "get new value again");
                try {
                    triggerEvent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Stat());
        String s=new String(data);
        System.out.println(s);
        return s;
    }

    private void triggerEvent() throws Exception{
//        byte[] data = zooKeeper.getData(NODE_PATH, false, new Stat());
        getZnodeValue(NODE_PATH);
    }
}