package com.qianfeng.etil.mr.toHbase;


import com.qianfeng.ip.LogUtil;
import com.qianfeng.common.EventLogConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Put;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.zip.CRC32;


/**
 * 将hdfs中收集的数据清晰后存储到hbase中
 */
public class ToHbaseMapper extends Mapper<LongWritable, Text, NullWritable, Put> {
    private static final Logger logger = Logger.getLogger(ToHbaseMapper.class);
    //输入输出的记录
    private int inputRecords, outputRecords, filterRecords = 0;
    private final byte[] family = Bytes.toBytes(EventLogConstants.HBASE_COLUMV_FAMILY);

    private CRC32 crc = new CRC32();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        this.inputRecords++;
        String log = value.toString();
        if (StringUtils.isEmpty(log.trim())) {
            this.filterRecords++;
            return;
        }
        //正常调用日志的工具方法解析log
        Map<String, String> info = LogUtil.handleLog(log);
        //根据事件来存储数据
        String eventName = info.get(EventLogConstants.EVENT_COLUMN_NAME_EVENT_NAME);
        EventLogConstants.EventEnum event = EventLogConstants.EventEnum.valueOfAlias(eventName);
        switch (event) {
            case LANUCH:
            case PAGEVIEW:
            case CHARGEREQUEST:
            case CHARGESUCCESS:
            case CHARGEREFUND:
            case EVENT:
                handleLogToHbase(info, eventName, context);
                break;
            default:
                logger.warn("事件类型暂时不支持数据的清洗.eventName=" + eventName);
                this.filterRecords++;
                break;
        }
    }

    /**
     * 将每一行数据写出
     *
     * @param info
     * @param context
     */
    private void handleLogToHbase(Map<String, String> info, String eventName, Context context) {
        if (!info.isEmpty()) {
            //获取构造row—key的字段
            String serverTime = info.get(EventLogConstants.EVENT_COLUMN_NAME_SERVER_TIME);
            String uuid = info.get(EventLogConstants.EVENT_COLUMN_NAME_UUID);
            String umid = info.get(EventLogConstants.EVENT_COLUMN_NAME_MEMBER_ID);
            if (StringUtils.isNotEmpty(serverTime)) {
                //构建row—key
                String rowKey = buildRowkey(serverTime, uuid, umid, eventName);
                //获取hbase put对象
                Put put = new Put(Bytes.toBytes(rowKey));
                //循环info,将所有的k-v数据存储到row-key行中
                for (String s : info.keySet()) {
                    //将k-v添加到put中
                    if (StringUtils.isNotEmpty(s)) {
                        put.addColumn(family, Bytes.toBytes(s), Bytes.toBytes(info.get(s)));
                    }
                }
                try {
                    context.write(NullWritable.get(), put);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                outputRecords++;
            } else {
                this.filterRecords++;
            }
        }
    }

    //构建row-key
    private String buildRowkey(String serverTime, String uuid, String umid, String eventName) {
//        StringBuffer sb = new StringBuffer(serverTime);
//        sb.append("_");
        StringBuffer sb = new StringBuffer(serverTime+"_");
        if (StringUtils.isNotEmpty(serverTime)) {
            this.crc.reset();
            if (StringUtils.isNotEmpty(uuid)) {
                this.crc.update(uuid.getBytes());
            }
            if (StringUtils.isNotEmpty(umid)) {
                this.crc.update(umid.getBytes());
            }
            if (StringUtils.isNotEmpty(eventName)) {
                this.crc.update(eventName.getBytes());
            }
            sb.append(this.crc.getValue() % 1000000000L);
        }

        return sb.toString();
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        logger.info("+++++++++++inputRecords:" + inputRecords + "  filterRecords:" + filterRecords + "  outputRecords:" + outputRecords);
    }
}
