package com.malred.autoconfig;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author malguy-wang sir
 * @create ---
 * simple任务自动配置
 */
@Configuration // 这个注解会把类实例化
@AutoConfigureAfter(ZookeeperAutoConfig.class)
// 这个bean实例化就执行该类(但是该类必须比这个bean晚实例化)
@ConditionalOnBean(CoordinatorRegistryCenter.class)
public class SimpleJobAutoConfig {
    // zookeeper配置中心
    @Autowired
    private CoordinatorRegistryCenter zkCenter;
    // spring上下文
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct // 该类实例化后执行,相当于 initMethod = "init"
    public void initSimpleJob() {
        // 获取带有ElasticSimpleJob注解的bean
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ElasticSimpleJob.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();
            Class<?>[] interfaces = instance.getClass().getInterfaces();
            for (Class<?> superInterface : interfaces) {
                // 实现simplejob类,才是符合规范的simple任务类
                if (superInterface == SimpleJob.class) {
                    // 获取注解
                    ElasticSimpleJob annotation =
                            instance.getClass().getAnnotation(ElasticSimpleJob.class);
                    // 获取注解里的值
                    String jobName = annotation.jobName();
                    String cron = annotation.cron();
                    int sharingTotalCount = annotation.shardingTotalCount();
                    boolean overwrite = annotation.overwrite();
                    // 注册定时任务
                    // 1,job核心配置
                    var jcc = JobCoreConfiguration
                            // 参数1: 任务名称,参数2: cron表达式(0/10 -> 10秒执行一次),参数3: 分片项数量
                            .newBuilder(jobName, cron, sharingTotalCount).build();
                    // 2,job类型配置
                    // 参数1: 核心配置,参数2: 任务类的全类名
                    var jtc = new SimpleJobConfiguration(jcc, instance.getClass().getCanonicalName());
                    // 3,job根配置 (LiteJobConfiguration)
                    var ljc = LiteJobConfiguration.newBuilder(jtc)
                            // 有这个才能重新布置任务,否则修改不会生效
                            .overwrite(overwrite)
                            .build();
                    new JobScheduler(zkCenter, ljc).init();
                }
            }
        }
    }
}
