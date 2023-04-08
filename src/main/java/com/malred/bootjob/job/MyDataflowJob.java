package com.malred.bootjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.malred.autoconfig.ElasticDataflowJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author malguy-wang sir
 * @create ---
 */
@Slf4j
@Component
@ElasticDataflowJob(
        jobName = "myDataflowJob",
        cron = "0/10 * * * * ?",
        shardingTotalCount = 2,
        overwrite = true,
        streamingProcess = true
)
public class MyDataflowJob implements DataflowJob<Integer> {
    // 模拟数据
    private List<Integer> list = new ArrayList<Integer>();

    {
        // 静态代码块：实例化时执行
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
    }

    @Override
    public List<Integer> fetchData(ShardingContext shardingContext) {
        List<Integer> rtnList = new ArrayList<Integer>();
        // 数字 % 分片总数 == 当前分片项
        for (Integer index : list) {
            if (index % shardingContext.getShardingTotalCount() == shardingContext.getShardingItem()) {
                rtnList.add(index);
                break;
            }
        }
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("分片项: " + shardingContext.getShardingItem() + ".当前数据: " + rtnList);
        return rtnList;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<Integer> data) {
        // 从原来的list里去掉
        list.removeAll(data);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("分片项: " + shardingContext.getShardingItem() + ".移除了数据: " + data);
    }
}
