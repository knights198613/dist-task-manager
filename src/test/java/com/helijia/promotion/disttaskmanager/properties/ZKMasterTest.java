package com.helijia.promotion.disttaskmanager.properties;

import com.helijia.promotion.disttaskmanager.DistTaskManagerApplicationTests;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/6/30
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description:
 */
public class ZKMasterTest extends DistTaskManagerApplicationTests {

    @Autowired
    CuratorFramework zkClient;

    @Test
    public void testPathCache() throws Exception {
        String jobOperationPath = "/jobOperationTask";
        PathChildrenCache cache = new PathChildrenCache(zkClient, jobOperationPath, true);
        cache.start();
        PathChildrenCacheListener cacheListener = (zkClient, event)->{
            System.out.println("事件类型："+ event.getType());
            if(null != event.getData()) {
                System.out.println("节点数据："+ event.getData().getPath()+" = "+ new String(event.getData().getData()));
            }
        };
        cache.getListenable().addListener(cacheListener);

        TimeUnit.SECONDS.sleep(300);

    }
}
