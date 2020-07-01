package com.helijia.promotion.disttaskmanager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = DistTaskManagerApplication.class)
@RunWith(SpringRunner.class)
public class DistTaskManagerApplicationTests {



    @Test
    public void testRun() {

        try {
            TimeUnit.SECONDS.sleep(120);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
