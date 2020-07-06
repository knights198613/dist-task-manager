package com.helijia.promotion.disttaskmanager.tasksub;

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
public class TaskSubAndPubTest extends DistTaskManagerApplicationTests {

    @Autowired
    TaskPubListener taskPubListener;


    @Test
    public void testRun() {
        try {
            for(int x=1; x<10; x++) {
                new Thread(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                        List<TaskPayload> taskPayloadList = new ArrayList<>();
                        TaskPayload t1 = new TaskPayload();
                        t1.setBatchNum(UUID.randomUUID().toString());
                        t1.setTaskInfo(new String("FUCK!!!"));
                        t1.prdTaskId();
                        taskPayloadList.add(t1);
                        taskPubListener.pubTask(taskPayloadList);
                        TimeUnit.SECONDS.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, "pubThread").start();
            }


            TimeUnit.SECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
