package com.qianfeng.analystic.model.dim.value;

import com.persist.common.KpiType;
import org.apache.hadoop.io.Writable;

/**
 * map和reduce阶段，输出的value类型的顶级父类
 * @author zjj
 */
public abstract class BaseOutputValueWritable implements Writable {
    /**
     * 获得指标
     * 整个mapreduce都要知道在统计什么kpi
     * 到底是新增用户还是活跃用户
     * @return KpiType
     */
    public abstract KpiType getKpi();
}
