package com.helijia.promotion.disttaskmanager.tasksub;

import com.helijia.promotion.disttaskmanager.DistTaskManagerApplicationTests;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/7/2
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description:
 */
public class TaskSubTest extends DistTaskManagerApplicationTests {

    @Test
    public void testRun() {
        try {
            TimeUnit.SECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
