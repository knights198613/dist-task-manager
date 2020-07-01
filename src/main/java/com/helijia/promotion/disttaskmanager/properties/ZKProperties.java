package com.helijia.promotion.disttaskmanager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: wuma (wuma@helijia.com)
 * @createDate: 2020/6/30
 * @company: (C) Copyright WWW.HELIJIA.COM 2020
 * @since: JDK 1.8
 * @description:
 */
@ConfigurationProperties(prefix = "zookeeper", ignoreUnknownFields = true)
@Data
@Component
public class ZKProperties {

    private String address;

    private Integer millSeconds;

    private Integer retryTimes;

}
