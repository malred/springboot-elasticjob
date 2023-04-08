package com.malred.bootjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.malred.autoconfig.ElasticSimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
//@ElasticSimpleJob(
//        jobName = "mySimpleJob",
//        cron = "0/10 * * * * ?",
//        shardingTotalCount = 2,
//        overwrite = true
//)
@Component
public class MySimpleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("分片项: " + shardingContext.getShardingItem() +
                ",总分片数: " + shardingContext.getShardingTotalCount());
    }
}
