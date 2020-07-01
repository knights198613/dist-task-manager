package com.helijia.promotion.disttaskmanager.conf;

import com.helijia.promotion.disttaskmanager.listener.TaskPubListener;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/7/1
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description:
 */
@Configuration
public class TaskPubListenerConf {

    @Bean(name = "taskPubListener", initMethod = "start", destroyMethod = "shutdown")
    public TaskPubListener createListener(@Qualifier("zkClient") CuratorFramework zkClient) {
        TaskPubListener taskPubListener = new TaskPubListener();
        taskPubListener.setZkClient(zkClient);
        return taskPubListener;
    }
}
