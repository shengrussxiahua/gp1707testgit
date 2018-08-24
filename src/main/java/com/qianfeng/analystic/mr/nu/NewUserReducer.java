package com.qianfeng.analystic.mr.nu;

import com.qianfeng.analystic.model.dim.StatsUserDimension;
import com.qianfeng.analystic.model.dim.value.map.TimeOutputValue;
import com.qianfeng.analystic.model.dim.value.reduce.TextOutputVlaue;
import com.qianfeng.common.KpiType;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * 新增用户的reducer类
 */
public class NewUserReducer extends Reducer<StatsUserDimension, TimeOutputValue, StatsUserDimension, TextOutputVlaue> {
    private static final Logger logger = Logger.getLogger(NewUserReducer.class);
    private StatsUserDimension k = new StatsUserDimension();
    private TextOutputVlaue v = new TextOutputVlaue();
    private Set<String> unique = new HashSet<String>();//用于uuid的去重统计


    @Override
    protected void reduce(StatsUserDimension key, Iterable<TimeOutputValue> values, Context context) throws IOException, InterruptedException {
        //清空set
        this.unique.clear();
        //循环map阶段传过来的value
        for (TimeOutputValue tv : values) {
            //将uuid取出来添加到set中
            this.unique.add(tv.getId());
        }
        //构建输出的value
        MapWritable map = new MapWritable();
        map.put(new IntWritable(-1), new IntWritable(this.unique.size()));
        this.v.setValue(map);
        //设置kpi
        if (k.getStatsCommonDimension().getKpiDimension().getKpiName().equals(KpiType.NEW_USER.kpiName)) {
            this.v.setKpi(KpiType.valueOfKpiName(key.getStatsCommonDimension().getKpiDimension().getKpiName()));
        }
//        输出
        context.write(key, this.v);
    }
}
