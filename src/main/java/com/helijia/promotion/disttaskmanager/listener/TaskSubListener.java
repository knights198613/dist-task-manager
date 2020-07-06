package com.helijia.promotion.disttaskmanager.listener;

import com.alibaba.fastjson.JSON;
import com.helijia.promotion.disttaskmanager.domain.TaskFinishedPayLoad;
import com.helijia.promotion.disttaskmanager.domain.TaskPayload;
import com.helijia.promotion.disttaskmanager.sub.TaskSubExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/7/1
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description: 任务订阅器监听器
 */
@Slf4j
public class TaskSubListener {
    /**
     * 线程运行标识
     */
    private Object object = new Object();
    /**
     * 任务分发的根容器节点
     */
    private String TASK_ROOT = "/taskRoot";
    /**
     * 任务节点 路径
     */
    private final String TASK_NODE_PATH = TASK_ROOT +"/task/"+UUID.randomUUID().toString();
    /**
     * 任务状态节点 路径
     */
    private final String TASK_STATUS_NODE_PATH = TASK_ROOT +"/taskStatus/"+UUID.randomUUID().toString();

    private String namespace;

    private CuratorFramework zkClient;

    private TaskSubExecutor taskSubExecutor;



    public void start() throws Exception{
        //校验成员变量
        volation();
        initZkClientNameSpace();
        //注册任务节点和任务状态节点
        registerTaskAndStatusNodes();
        //监听自己注册的任务分发节点
        listenerTaskNodes();
    }


    public void shutdown() {
        object.notify();
    }

    /**
     * 设置zkClient namespace
     */
    private void initZkClientNameSpace() {
       zkClient = zkClient.usingNamespace(getNamespace());
    }

    /**
     * 校验成员
     */
    private void volation() {
        if(Objects.isNull(zkClient)) {
            throw new IllegalArgumentException("zkClient must not be null");
        }
        if(StringUtils.isEmpty(namespace)) {
            throw new IllegalArgumentException("namespace must not be null");
        }
        if(Objects.isNull(taskSubExecutor)) {
            throw  new IllegalArgumentException("taskSubExecutor must not be null");
        }
    }


    /**
     * 监听任务节点
     */
    private void listenerTaskNodes() {
        NodeCache nodeCache = new NodeCache(zkClient, TASK_NODE_PATH);
        NodeCacheListener listener = ()->{
            ChildData data = nodeCache.getCurrentData();
            if(null != data) {
                String taskMessage = new String(nodeCache.getCurrentData().getData());
                log.info("节点数据："+ taskMessage);
                TaskPayload taskPayload = null;
                try {
                    taskPayload = JSON.parseObject(taskMessage, TaskPayload.class);
                    //执行任务调度
                    TaskFinishedPayLoad taskFinishedPayLoad = taskSubExecutor.doExecute(taskPayload);
                    //执行完毕反向通知发布者
                    finishedAndModifyNode(taskFinishedPayLoad);
                } catch (Exception e) {
                    log.error(taskMessage);
                }
            }else {
                log.info("节点被删除");
            }
        };
        Thread t = new Thread(()->{
            log.info("taskSubListener has running !!!!");
            nodeCache.getListenable().addListener(listener);
            try {
                nodeCache.start();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            synchronized (object) {
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
            log.info("taskSubListener has stopped !!!!!");
        }, "taskSubListener");
        t.start();
    }






    /**
     * 注册任务接收节点和任务执行结果反馈节点
     */
    private void registerTaskAndStatusNodes() throws Exception{
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(TASK_NODE_PATH, "create".getBytes());
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(TASK_STATUS_NODE_PATH, "creat".getBytes());
    }

    /**
     * 任务执行完成 更新任务状态节点数据 反馈任务发布者
     */
    private void finishedAndModifyNode(TaskFinishedPayLoad taskFinishedPayLoad) throws Exception {
        if(Objects.nonNull(taskFinishedPayLoad)) {
            zkClient.setData().forPath(TASK_STATUS_NODE_PATH, JSON.toJSONString(taskFinishedPayLoad).getBytes());
        }
    }



    public void setZkClient(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setTaskSubExecutor(TaskSubExecutor taskSubExecutor) {
        this.taskSubExecutor = taskSubExecutor;
    }
}
