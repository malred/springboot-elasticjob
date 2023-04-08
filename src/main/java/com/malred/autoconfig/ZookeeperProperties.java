package com.malred.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author malguy-wang sir
 * @create ---
 * zookeeper配置
 */
@Data
//@Component // 注册为bean(或者通过在启动类加@EnableConfigurationProperties(value=xxx.class)来注册)
// 这个注解就是sprngboot starter读取配置文件然后自动配置
// prefix就是properties或yaml文件上的配置的名称 xxx.xxx=xxx
@ConfigurationProperties(prefix = "elasticjob.zookeeper")
public class ZookeeperProperties {
    // zookeeper地址列表
    private String serverList;
    // zookeeper命名空间
    private String namespace;
}
