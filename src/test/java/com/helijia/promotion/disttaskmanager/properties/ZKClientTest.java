package com.helijia.promotion.disttaskmanager.properties;

import com.helijia.promotion.disttaskmanager.DistTaskManagerApplicationTests;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/6/30
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description:
 */
public class ZKClientTest extends DistTaskManagerApplicationTests {
    @Autowired
    CuratorFramework client;

    @Test
    public void testAA() {
        String jobOperationPath = "/jobOperationTask";
        String nodePath1 = "/"+UUID.randomUUID().toString();
        String nodePath2 = "/"+UUID.randomUUID().toString();
        String init = "init";
        try {
            String rs1 = client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(jobOperationPath+nodePath1, init.getBytes());
            String rs2 = client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(jobOperationPath+nodePath2, init.getBytes());
            TimeUnit.SECONDS.sleep(120);
            List<String> ls = client.getChildren().forPath(jobOperationPath);
            if(!CollectionUtils.isEmpty(ls)) {
                for(String nodePath : ls) {
                    String content = new String(client.getData().forPath(jobOperationPath+"/"+nodePath));
                    System.out.println("循环获取节点内容："+content);
                }
            }
            System.out.println(ls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
