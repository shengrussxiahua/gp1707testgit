package com.qianfeng.analystic.model.dim.value.reduce;

import com.qianfeng.analystic.model.dim.value.BaseOutputValueWritable;
import com.qianfeng.common.KpiType;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 用于模块和浏览器模块reduce阶段的输出的value的类型
 */
public class TextOutputVlaue extends BaseOutputValueWritable {
    private KpiType kpi;//泛指
    private MapWritable value = new MapWritable();

    @Override
    public KpiType getKpi() {
        return this.kpi;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        WritableUtils.writeEnum(dataOutput, kpi);
        this.value.write(dataOutput);//对象的序列化

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        WritableUtils.readEnum(dataInput, KpiType.class);
        this.value.readFields(dataInput);
    }

    public void setKpi(KpiType kpi) {
        this.kpi = kpi;
    }

    public MapWritable getValue() {
        return value;
    }

    public void setValue(MapWritable value) {
        this.value = value;
    }

}
