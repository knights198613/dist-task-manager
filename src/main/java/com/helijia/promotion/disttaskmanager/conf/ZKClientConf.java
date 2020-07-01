package com.helijia.promotion.disttaskmanager.conf;

import com.helijia.promotion.disttaskmanager.properties.ZKProperties;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/6/30
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description:
 */
@Configuration
public class ZKClientConf {

    @Autowired
    ZKProperties zkProperties;

    @Bean(name = "zkClient", destroyMethod = "close")
    public CuratorFramework createClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zkProperties.getMillSeconds(), zkProperties.getRetryTimes());
        CuratorFramework client = CuratorFrameworkFactory
                .newClient(zkProperties.getAddress(), retryPolicy);
        client.start();
        //client.usingNamespace(zkProperties.getNameSpace());
        return client;
    }
}
