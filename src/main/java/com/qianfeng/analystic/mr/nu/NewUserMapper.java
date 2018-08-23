package com.qianfeng.analystic.mr.nu;

import com.qianfeng.analystic.model.dim.StatsUserDimension;
import org.apache.hadoop.hbase.mapreduce.TableMapper;

/**
 * 统计新增的用户:launch事件中uuid的去重个数
 */
public class NewUserMapper extends TableMapper<StatsUserDimension,> {
}
