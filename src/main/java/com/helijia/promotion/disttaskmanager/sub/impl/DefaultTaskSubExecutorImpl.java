package com.helijia.promotion.disttaskmanager.sub.impl;

import com.alibaba.fastjson.JSON;
import com.helijia.promotion.disttaskmanager.domain.TaskFinishedPayLoad;
import com.helijia.promotion.disttaskmanager.domain.TaskPayload;
import com.helijia.promotion.disttaskmanager.enums.TaskStatusEnum;
import com.helijia.promotion.disttaskmanager.sub.TaskSubExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public List<TaskFinishedPayLoad> doExecute(List<TaskPayload> taskPayloadList) {
        System.out.println(JSON.toJSONString(taskPayloadList));
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            log.error("##########");
        }
        List<TaskFinishedPayLoad> taskFinishedPayLoadList = new ArrayList<>();
        for(TaskPayload taskPayload : taskPayloadList) {
            TaskFinishedPayLoad taskFinishedPayLoad = new TaskFinishedPayLoad();
            taskFinishedPayLoad.setBatchNum(taskPayload.getBatchNum());
            taskFinishedPayLoad.setTaskStatusEnum(TaskStatusEnum.SUCCESS);
            taskFinishedPayLoadList.add(taskFinishedPayLoad);
        }
        return taskFinishedPayLoadList;
    }
}
