package com.qianfeng.analystic.mr.nu;

import com.qianfeng.analystic.model.dim.StatsCommonDimension;
import com.qianfeng.analystic.model.dim.StatsUserDimension;
import com.qianfeng.analystic.model.dim.base.BrowserDimension;
import com.qianfeng.analystic.model.dim.base.DateDimension;
import com.qianfeng.analystic.model.dim.base.KpiDimension;
import com.qianfeng.analystic.model.dim.base.PlatformDimension;
import com.qianfeng.analystic.model.dim.value.map.TimeOutputValue;
import com.qianfeng.common.DateEnum;
import com.qianfeng.common.EventLogConstants;
import com.qianfeng.common.KpiType;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;


/**
 * 统计新增的用户:launch事件中uuid的去重个数
 */
public class NewUserMapper extends TableMapper<StatsUserDimension, TimeOutputValue> {
    private static final Logger logger = Logger.getLogger(NewUserMapper.class);
    private byte[] family = Bytes.toBytes(EventLogConstants.HBASE_COLUMV_FAMILY);
    private StatsUserDimension k = new StatsUserDimension();
    private TimeOutputValue v = new TimeOutputValue();
    private KpiDimension newUserKpi = new KpiDimension(KpiType.NEW_USER.kpiName);

    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        //从hbase中读取数据
        String serverTime = Bytes.toString(value.getValue(family, Bytes.toBytes(EventLogConstants.EVENT_COLUMN_NAME_SERVER_TIME)));
        String uuid = Bytes.toString(value.getValue(family, Bytes.toBytes(EventLogConstants.EVENT_COLUMN_NAME_UUID)));
        String platform = Bytes.toString(value.getValue(family, Bytes.toBytes(EventLogConstants.EVENT_COLUMN_NAME_PLATFORM)));
        //    String serverTime = Bytes.toString(value.getValue(family, Bytes.toBytes(EventLogConstants.EVENT_COLUMN_NAME_SERVER_TIME)));
        if (StringUtils.isEmpty(uuid) || StringUtils.isEmpty(serverTime)) {
            logger.warn("uuid && servertime must not enpty:" + uuid + serverTime);
            return;
        }
        //构造输出的value
        long longOfServerTime = Long.valueOf(serverTime);
        this.v.setId(uuid);
        this.v.setTime(longOfServerTime);
        //构造输出的key
        DateDimension dateDimension = DateDimension.buildDate(longOfServerTime, DateEnum.DAY);
        List<PlatformDimension> platformDimensions = PlatformDimension.buildList(platform);
        StatsCommonDimension statsCommonDimension = this.k.getStatsCommonDimension();
        statsCommonDimension.setDateDimension(dateDimension);
        //循环平台维度输出
        for (PlatformDimension pl : platformDimensions) {
            statsCommonDimension.setKpiDimension(newUserKpi);
            statsCommonDimension.setPlatformDimension(pl);
            //设置默认的浏览器维度
            this.k.setBrowserDimension(new BrowserDimension("", ""));
            this.k.setStatsCommonDimension(statsCommonDimension);
            //输出
            context.write(this.k, this.v);
        }

    }
}
