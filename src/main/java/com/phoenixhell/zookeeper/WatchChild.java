package com.phoenixhell.zookeeper;

/**
 * @author phoenixhell
 * @create 2021/1/30 0030-上午 10:07
 */
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

@Slf4j
public class WatchChild extends  BaseConfig {
    private ZooKeeper zooKeeper = null;

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public static void main(String[] args) throws Exception {
        WatchChild watchChild = new WatchChild();
        watchChild.setZooKeeper(watchChild.getZookeeper());
        //下面2个模拟客户端程序不关接收server反馈
        System.in.read();
//        Thread.sleep(Long.MAX_VALUE);
    }

    public ZooKeeper getZookeeper() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent event) {
                //只监控path路径的子节点变化
                if(event.getType().equals(Event.EventType.NodeChildrenChanged) && event.getPath().equals(NODE_PATH)){
                    showChildNode(NODE_PATH);
                }else{
                    //可以注册其他路径比如"/" 当前情况直接写也可以 集群回来再看
                    showChildNode(NODE_PATH);
                }
            }
        });
    }

    public void showChildNode(String nodePath) {
        try {
            List<String> list = zooKeeper.getChildren(NODE_PATH, true);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}