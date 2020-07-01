package com.helijia.promotion.disttaskmanager.pub;

import com.helijia.promotion.disttaskmanager.domain.TaskPayload;

import java.util.List;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/7/1
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description: 任务发布接口
 */
public interface TaskPubExecutor {

    /**
     * 分发任务
     * @param taskPayloadList
     */
    void pubTask(List<TaskPayload> taskPayloadList);

    /**
     * 判断能否分发任务
     * @return
     */
    boolean canPub();




}
