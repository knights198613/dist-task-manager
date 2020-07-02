package com.helijia.promotion.disttaskmanager.conf;

import com.helijia.promotion.disttaskmanager.listener.TaskSubListener;
import com.helijia.promotion.disttaskmanager.sub.TaskSubExecutor;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TaskSubListenerConf {

    private String nameSpace = "dist-task";

    @Autowired
    TaskSubExecutor taskSubExecutor;

    @Bean(name = "taskSubListener", initMethod = "start", destroyMethod = "shutdown")
    public TaskSubListener createTaskSubListener(@Qualifier("zkClient") CuratorFramework zkClient) {
        TaskSubListener taskSubListener = new TaskSubListener();
        taskSubListener.setZkClient(zkClient);
        taskSubListener.setNamespace(nameSpace);
        taskSubListener.setTaskSubExecutor(taskSubExecutor);
        return taskSubListener;
    }
}
