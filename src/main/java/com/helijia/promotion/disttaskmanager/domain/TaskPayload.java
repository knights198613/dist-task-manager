package com.helijia.promotion.disttaskmanager.domain;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/7/1
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description:  任务信息负载体
 */

public class TaskPayload<T> implements Serializable {

    private static final long serialVersionUID = -7258447190802714239L;
    /**
     * 任务批次号
     */
    private String batchNum;
    /**
     * 任务信息
     */
    private T taskInfo;
    /**
     * 任务id
     */
    private String taskId;

    public TaskPayload() {
        this.taskId = UUID.randomUUID().toString();
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public T getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(T taskInfo) {
        this.taskInfo = taskInfo;
    }

    public String getTaskId() {
        return taskId;
    }
}
