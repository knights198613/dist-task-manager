package com.helijia.promotion.disttaskmanager.listener;

import com.alibaba.fastjson.JSON;
import com.helijia.promotion.disttaskmanager.domain.TaskFinishedPayLoad;
import com.helijia.promotion.disttaskmanager.domain.TaskPayload;
import com.helijia.promotion.disttaskmanager.enums.TaskBatchStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
    private final String TASK_ROOT = "/taskRoot";
    /**
     * 任务批次状态节点路径
     */
    private final String TASK_BATCH_STATUS_PATH = TASK_ROOT + "/taskBatchStatus";

    /**
     * 任务根节点 路径
     */
    private final String TASK_NODE_PATH = TASK_ROOT +"/task";
    /**
     * 任务状态根节点 路径
     */
    private final String TASK_STATUS_NODE_PATH = TASK_ROOT +"/taskStatus";

    private CuratorFramework zkClient;

    private String namespace;
    /**
     * 任务容器
     */
    private ConcurrentHashMap<String, List<TaskPayload>> taskPayLoadMap = new ConcurrentHashMap<>();

    public void start() throws Exception{
        //校验必要成员
        volation();
        //设置zkClient namespace
        initZkClientNameSpace();

        //创建任务批次状态节点
        createTaskBatchStatusNode();
        //监听任务节点
        listenerTaskStatusNodes();
    }

    public void shutdown() {
       synchronized (object) {
           object.notify();
       }
    }

    /**
     * 设置zkClient namespace
     */
    private void initZkClientNameSpace() {
       zkClient = zkClient.usingNamespace(getNamespace());
    }

    /**
     * 开始监听任务状态节点
     */
    private void listenerTaskStatusNodes() {

        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, TASK_STATUS_NODE_PATH, true);

        PathChildrenCacheListener cacheListener = (zkClient, event)->{
            log.info("事件类型："+ event.getType());
            if(null != event.getData()) {
                log.info("节点数据："+ event.getData().getPath()+" = "+ new String(event.getData().getData()));
                if(PathChildrenCacheEvent.Type.CHILD_UPDATED.equals(event.getType())) {
                    String message = new String(event.getData().getData());
                    try {
                        TaskFinishedPayLoad taskFinishedPayLoad = JSON.parseObject(message, TaskFinishedPayLoad.class);
                        doJudgeFinished(taskFinishedPayLoad);
                    } catch (Exception e) {
                        log.error("执行反馈结果异常。message={}.", message, e);
                    }

                }
            }
        };

        Thread t1 = new Thread(() -> {
            log.info("TaskPubListener has running !!!!!");
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
            log.info("TaskPubListener has stopped !!!!!");

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
    private void createTaskBatchStatusNode() throws Exception {
        //zkClient.delete().forPath(TASK_BATCH_STATUS_PATH);
        Stat stat = zkClient.checkExists().forPath(TASK_BATCH_STATUS_PATH);
        if(Objects.isNull(stat)) {
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(TASK_BATCH_STATUS_PATH, "create".getBytes());
        }
    }

    /**
     * 是否能够发布任务 true:可以发布  false:不能发布
     * @return
     */
    private boolean canPubTask() throws Exception{
        String batchStatusMessage = new String(zkClient.getData().forPath(TASK_BATCH_STATUS_PATH));
        if(batchStatusMessage.equals(TaskBatchStatusEnum.FINISHED.name()) || batchStatusMessage.equals("create")) {
            return true;
        }
        return false;
    }

    /**
     * 设置任务批次节点状态信息
     * @param message
     * @return
     */
    private void setTaskBatchStatusNode(String message) throws Exception {
        zkClient.setData().forPath(TASK_BATCH_STATUS_PATH, message.getBytes());
    }

    /**
     * 发布任务 true:发布成功  false:发布失败
     * @param taskPayloadList
     */
    public boolean pubTask(List<TaskPayload> taskPayloadList) throws Exception{
        List<String> taskNodePaths = zkClient.getChildren().forPath(TASK_NODE_PATH);
        if(!CollectionUtils.isEmpty(taskNodePaths)) {
            if(canPubTask()) {
                //设置任务批次节点状态
                setTaskBatchStatusNode(TaskBatchStatusEnum.INIT.name());
                //开始发布任务
                for(TaskPayload taskPayload : taskPayloadList) {
                    for(String taskNodePath : taskNodePaths) {
                        List<TaskPayload> taskPayloadList1 = taskPayLoadMap.getOrDefault(taskPayload.getBatchNum(), new ArrayList<>());
                        taskPayloadList1.add(taskPayload);
                        zkClient.setData().forPath(TASK_NODE_PATH+"/"+taskNodePath, JSON.toJSONString(taskPayload).getBytes());
                        taskPayLoadMap.put(taskPayload.getBatchNum(), taskPayloadList1);
                    }
                }
                //设置任务批次节点状态
                setTaskBatchStatusNode(TaskBatchStatusEnum.RUNNING.name());
                return true;
            }
            return false;
        }else {
            throw new RuntimeException("没有对应的任务订阅者领取待发布的任务！！！");
        }
    }


    /**
     * 判断一批任务是否执行完成
     * @param taskFinishedPayLoad
     */
    private void doJudgeFinished(TaskFinishedPayLoad taskFinishedPayLoad) throws Exception{
        String batchNum = taskFinishedPayLoad.getBatchNum();
        List<TaskPayload> payloadList = taskPayLoadMap.get(batchNum);
        TaskPayload payload = payloadList.stream().filter(item->{
            if(item.getTaskId().equals(taskFinishedPayLoad.getTaskId())) {
                return true;
            }
            return false;
        }).findFirst().orElseGet(null);
        if(Objects.nonNull(payload)) {
            payloadList.remove(payload);
        }
        if(CollectionUtils.isEmpty(payloadList)) {
            taskPayLoadMap.remove(batchNum);
            setTaskBatchStatusNode(TaskBatchStatusEnum.FINISHED.name());
            log.info("HAHAHAHAHAHA 任务已经被执行完了！！！！！！");
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
}
