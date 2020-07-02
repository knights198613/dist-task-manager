package com.helijia.promotion.disttaskmanager.sub.impl;

import com.alibaba.fastjson.JSON;
import com.helijia.promotion.disttaskmanager.domain.TaskFinishedPayLoad;
import com.helijia.promotion.disttaskmanager.domain.TaskPayload;
import com.helijia.promotion.disttaskmanager.enums.TaskStatusEnum;
import com.helijia.promotion.disttaskmanager.sub.TaskSubExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/7/2
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description:
 */
@Slf4j
@Service
public class DefaultTaskSubExecutorImpl implements TaskSubExecutor {


    @Override
    public TaskFinishedPayLoad doExecute(TaskPayload taskPayload) {
        log.info(JSON.toJSONString("接收到的任务：" + taskPayload));
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            log.error("##########");
        }

        TaskFinishedPayLoad taskFinishedPayLoad = new TaskFinishedPayLoad();
        taskFinishedPayLoad.setBatchNum(taskPayload.getBatchNum());
        taskFinishedPayLoad.setTaskStatusEnum(TaskStatusEnum.SUCCESS);
        taskFinishedPayLoad.setTaskId(taskPayload.getTaskId());

        return taskFinishedPayLoad;
    }
}
