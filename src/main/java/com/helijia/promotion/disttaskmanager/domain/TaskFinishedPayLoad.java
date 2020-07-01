package com.helijia.promotion.disttaskmanager.domain;

import com.helijia.promotion.disttaskmanager.enums.TaskStatusEnum;

import java.io.Serializable;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/7/1
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description: 任务完成反馈信息负载
 */
public class TaskFinishedPayLoad implements Serializable {
    private static final long serialVersionUID = 6039197047837896120L;
    /**
     * 任务批次号
     */
    private String batchNum;
    /**
     * 回馈任务执行状态
     */
    private TaskStatusEnum taskStatusEnum;


    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public TaskStatusEnum getTaskStatusEnum() {
        return taskStatusEnum;
    }

    public void setTaskStatusEnum(TaskStatusEnum taskStatusEnum) {
        this.taskStatusEnum = taskStatusEnum;
    }
}
