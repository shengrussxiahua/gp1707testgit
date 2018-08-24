package com.qianfeng.analystic.model.dim.value.map;

import com.qianfeng.analystic.model.dim.value.BaseOutputValueWritable;
import com.qianfeng.common.KpiType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 用于map阶段的输出的value的类型
 */
public class TimeOutputValue extends BaseOutputValueWritable {
    private String id;//泛指
    private long time;

    @Override
    public KpiType getKpi() {
        return null;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.id);
        dataOutput.writeLong(this.time);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.id = dataInput.readUTF();
        this.time = dataInput.readLong();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
