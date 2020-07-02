package com.helijia.promotion.disttaskmanager.taskpub;

import com.helijia.promotion.disttaskmanager.DistTaskManagerApplicationTests;
import com.helijia.promotion.disttaskmanager.domain.TaskPayload;
import com.helijia.promotion.disttaskmanager.listener.TaskPubListener;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/7/2
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description:
 */
public class TaskPubTest extends DistTaskManagerApplicationTests {

    @Autowired
    TaskPubListener taskPubListener;

    @Test
    public void testPubTask() {
        List<TaskPayload> taskPayloadList = new ArrayList<>();
        TaskPayload t1 = new TaskPayload();
        t1.setBatchNum(UUID.randomUUID().toString());
        t1.setTaskInfo(new String("FUCK!!!"));
        taskPayloadList.add(t1);
        try {
            taskPubListener.pubTask(taskPayloadList);
            TimeUnit.SECONDS.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
