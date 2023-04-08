package com.malred.autoconfig;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author malguy-wang sir
 * @create ---
 * zookeeper自动配置类
 */
@Configuration
// 当某个配置项有值就自动执行该类
@ConditionalOnProperty("elasticjob.zookeeper.server-list")
public class ZookeeperAutoConfig {
    // 注入配置类
    @Autowired
    private ZookeeperProperties zookeeperProperties;

    /**
     * 注册中心配置
     */
    @Bean(initMethod = "init") // 执行init方法来初始化
    public CoordinatorRegistryCenter zkCenter() {
        var serverList = zookeeperProperties.getServerList();
        var namespace = zookeeperProperties.getNamespace();
        // 参数1: zk的地址(集群就写多个,中间用逗号隔开),参数2: 命名空间
        var zc =
                new ZookeeperConfiguration(serverList, namespace);
        return new ZookeeperRegistryCenter(zc);
        // 初始化注册中心
//        crc.init();
//        return crc;
    }
}
