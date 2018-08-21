package com.qianfeng.etil.mr.toHbase;

import com.qianfeng.common.EventLogConstants;
import com.qianfeng.common.GlobalConstants;
import com.qianfeng.util.TimeUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import java.io.IOException;


/**
 * 将清洗的数据存储到hbase的runner类
 */
public class ToHbaseRunner implements Tool {
    private static final Logger logger = Logger.getLogger(ToHbaseRunner.class);
    private Configuration conf = null;

    @Override
    public void setConf(Configuration configuration) {
        this.conf = HBaseConfiguration.create();
    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }

    public static void main(String[] args) {
        try {
            ToolRunner.run(new Configuration(), new ToHbaseRunner(), args);
        } catch (Exception e) {
            logger.warn("运行清洗数据到hbase中异常", e);
        }
    }

    //yarn jar .jar package.class -d 2018-08-17
    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = this.getConf();
        //处理参数
        processArgs(args, conf);
        Job job = Job.getInstance(conf, "to base");

        job.setJarByClass(ToHbaseRunner.class);
        //map阶段
        job.setMapperClass(ToHbaseMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Put.class);

        //判断hbase的表是否存在
        isTableExists(job);

        //设置输入

        //reducer的设置
        //本地提交本地运行，——addDependencyJars：true  则为本地提交集群运行
        TableMapReduceUtil.initTableReducerJob(EventLogConstants.HBASE_TABLE_NAME,
                null, job, null, null, null, null, true);

        job.setNumReduceTasks(0);

        //将不能识别的资源文件添加成分布式的缓存文件

        //设置输入路径
        setInputPath(job, args);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    //设置数据输入的路径
    private void setInputPath(Job job, String[] args) {
        try {
            String date = job.getConfiguration().get(GlobalConstants.RUNNING_DATE);
            //拆分日期，构建输入数据路径
            String[] fields = date.split("-");
            Path inputPath = new Path("/logs/" + fields[1] + "/" + fields[2]);
            FileSystem fs = FileSystem.get(job.getConfiguration());
            if (fs.exists(inputPath)) {
                FileInputFormat.addInputPath(job, inputPath);
            } else {
                throw new RuntimeException("数据输入路径不存在.inputPath:" + inputPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processArgs(String[] args, Configuration conf) {
        String date = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-d")) {
                if (i + 1 < args.length) {
                    date = args[i + 1];
                }
            }
        }
        //如果时间为空或者非法，都将运行昨天的数据
        if (StringUtils.isEmpty(date) || !TimeUtil.isValidateDate(date)) {
            date = TimeUtil.getYesterday();
        }
        //将时间存储到conf中
//        conf.set(GlobalConstants.RUNNING_DATE,date);
        conf.set(GlobalConstants.RUNNING_DATE, date);
    }

    private void isTableExists(Job job) {
        Connection conn = null;
        Admin admin = null;
        try {
            conn = ConnectionFactory.createConnection(job.getConfiguration());
            admin = conn.getAdmin();

            //
            TableName tn = TableName.valueOf(EventLogConstants.HBASE_TABLE_NAME);
            if (!admin.tableExists(tn)) {
                HTableDescriptor hdc = new HTableDescriptor(tn);
                HColumnDescriptor hcd = new HColumnDescriptor(Bytes.toBytes(EventLogConstants.HBASE_COLUMV_FAMILY));
                hdc.addFamily(hcd);

                admin.createTable(hdc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
            if (admin != null) {
                try {
                    admin.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }
    }


}
