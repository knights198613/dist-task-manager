package com.helijia.promotion.disttaskmanager.sub;

import com.helijia.promotion.disttaskmanager.domain.TaskFinishedPayLoad;
import com.helijia.promotion.disttaskmanager.domain.TaskPayload;

import java.util.List;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/7/1
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description: 任务订阅调度接口
 */
public interface TaskSubExecutor {

    /**
     * 调度执行任务列表
     * @param taskPayloadList
     * @return
     */
    List<TaskFinishedPayLoad> doExecute(List<TaskPayload> taskPayloadList);




}
