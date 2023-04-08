package com.malred.autoconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author malguy-wang sir
 * @create ---
 * dataflow任务注解
 */
@Target(ElementType.TYPE) // 加到类上
@Retention(RetentionPolicy.RUNTIME) // 运行时依赖
public @interface ElasticDataflowJob {
    // 任务名称
    String jobName() default "";

    // cron表达式
    String cron() default "";

    // 分片总数
    int shardingTotalCount() default 1;

    // 每次启动覆盖旧(同名)任务
    boolean overwrite() default false;

    // 是否流式处理
    boolean streamingProcess() default false;
}
