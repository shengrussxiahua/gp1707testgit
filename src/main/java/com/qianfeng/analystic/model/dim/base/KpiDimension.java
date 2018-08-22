package com.qianfeng.analystic.model.dim.base;

import com.qianfeng.common.GlobalConstants;
import org.apache.commons.lang.StringUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhoujiaojiao
 */
public class KpiDimension extends BaseDimension{
    private Integer id;

    private String kpiName;

    public KpiDimension(){}

    public KpiDimension(String kpiName) {
        this.kpiName = kpiName;
    }
    public KpiDimension(Integer id, String kpiName) {
        this.id = id;
        this.kpiName = kpiName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName == null ? null : kpiName.trim();
    }



    @Override
    public int compareTo(BaseDimension o) {
        if (this == o) {
            return 0;
        }
        KpiDimension other = (KpiDimension) o;

        int tmp = this.id - other.id;
        if (tmp != 0) {
            return tmp;
        }
        return this.kpiName.compareTo(other.kpiName);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.id);
        out.writeUTF(this.kpiName);

    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.kpiName = in.readUTF();
    }

    @Override
    public String toString() {
        return "KpiDimension{" +
                "id=" + id +
                ", kpiName='" + kpiName + '\'' +
                '}';
    }

    public List<KpiDimension> buildList(String kpiName){

        if (StringUtils.isEmpty(kpiName)) {
            kpiName = GlobalConstants.DEFAULT_NAME;
        }
        List<KpiDimension> li = new ArrayList<>();

        li.add(new KpiDimension(kpiName));
        li.add(new KpiDimension(GlobalConstants.ALL_OF_VALUE));

        return li;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KpiDimension that = (KpiDimension) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(kpiName, that.kpiName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, kpiName);
    }
}