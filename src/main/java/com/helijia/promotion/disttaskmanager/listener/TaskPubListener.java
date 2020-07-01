package com.helijia.promotion.disttaskmanager.listener;

import com.helijia.promotion.disttaskmanager.domain.TaskPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/7/1
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description:  任务发布器监听器
 */
@Slf4j
public class TaskPubListener {

    /**
     * 线程休眠对象
     */
    private Object object = new Object();

    /**
     * 任务分发的根容器节点
     */
    private String containerPath = "/jobRoot";
    /**
     * 任务批次状态节点路径
     */
    private String taskBatchStatusPath = "/jobBatchStatus";

    private CuratorFramework zkClient;

    private String namespace;

    public void start() {
        //校验必要成员
        volation();
        //创建任务批次状态节点
        createTaskBatchStatusNode();
        //监听任务节点
        listenerTaskNodes();
    }

    public void shutdown() {
       synchronized (object) {
           object.notify();
       }
    }

    /**
     * 开始监听任务分发节点
     */
    private void listenerTaskNodes() {

        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, getContainerPath(), true);

        PathChildrenCacheListener cacheListener = (zkClient, event)->{
            System.out.println("事件类型："+ event.getType());
            if(null != event.getData()) {
                System.out.println("节点数据："+ event.getData().getPath()+" = "+ new String(event.getData().getData()));
            }
        };

        Thread t1 = new Thread(() -> {
            System.out.println("$$$$$$$$$$$$$$$$$$$");
            try {
                pathChildrenCache.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            pathChildrenCache.getListenable().addListener(cacheListener);
            synchronized (object) {
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("################");

        }, "taskPubListener");
        t1.start();
    }

    /**
     * 校验必要成员
     */
    private void volation() {
        if(Objects.isNull(zkClient)) {
            throw new IllegalArgumentException("zkClient must not be null");
        }
        if(StringUtils.isEmpty(namespace)) {
            throw new IllegalArgumentException("namespace must not be null");
        }
    }

    /**
     * 创建任务批次状态节点
     */
    private void createTaskBatchStatusNode() {
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(taskBatchStatusPath);
    }

    /**
     * 是否能够发布任务
     * @return
     */
    private boolean canPubTask() {
        //String batchStatusMessage = new String(zkClient.getData().forPath(taskBatchStatusPath));
        return false;
    }

    /**
     * 发布任务
     * @param taskPayloadList
     */
    public void pubTask(List<TaskPayload> taskPayloadList) throws Exception{
        List<String> nodePaths = zkClient.getChildren().forPath(containerPath);
        if(!CollectionUtils.isEmpty(nodePaths)) {

        }
    }






    public void setZkClient(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }

    public String getContainerPath() {
        return containerPath;
    }

    public void setContainerPath(String containerPath) {
        this.containerPath = containerPath;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
