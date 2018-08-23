package com.qianfeng.analystic.model.dim;

import com.qianfeng.analystic.model.dim.base.BaseDimension;
import com.qianfeng.analystic.model.dim.base.DateDimension;
import com.qianfeng.analystic.model.dim.base.KpiDimension;
import com.qianfeng.analystic.model.dim.base.PlatformDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * map阶段和reduce阶段输出公共维度类型的封装
 */


public class StatsCommonDimension extends BaseStatsDimension {
    private DateDimension dateDimension = new DateDimension();
    private PlatformDimension platformDimension = new PlatformDimension();
    private KpiDimension kpiDimension = new KpiDimension();

    public StatsCommonDimension() {

    }

    public StatsCommonDimension(DateDimension dateDimension, PlatformDimension platformDimension, KpiDimension kpiDimension) {
        this.dateDimension = dateDimension;
        this.platformDimension = platformDimension;
        this.kpiDimension = kpiDimension;
    }

    @Override
    public int compareTo(BaseDimension o) {
        if (this == o) {
            return 0;
        }

        StatsCommonDimension other = (StatsCommonDimension) o;

        int tmp = this.dateDimension.compareTo(other.dateDimension);
        if (tmp != 0) {
            return tmp;
        }
        tmp = this.platformDimension.compareTo(other.platformDimension);
        if (tmp != 0) {
            return tmp;
        }
        return this.kpiDimension.compareTo(other.kpiDimension);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.dateDimension.write(dataOutput);
        this.platformDimension.write(dataOutput);
        this.kpiDimension.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.dateDimension.readFields(dataInput);
        this.platformDimension.readFields(dataInput);
        this.kpiDimension.readFields(dataInput);
    }

    /**
     * 克隆一个当前实例
     *
     * @param dimension
     * @return
     */
    public static StatsCommonDimension clone(StatsCommonDimension dimension) {
        PlatformDimension platformDimension = new PlatformDimension(dimension.platformDimension.getPlatformName());
        KpiDimension kpiDimension = new KpiDimension(dimension.kpiDimension.getKpiName());
        DateDimension dateDimension = new DateDimension(dimension.dateDimension.getYear(),
                dimension.dateDimension.getSeason(), dimension.dateDimension.getMonth(),
                dimension.dateDimension.getWeek(), dimension.dateDimension.getDay(),
                dimension.dateDimension.getCalendar(), dimension.dateDimension.getType());
        return new StatsCommonDimension(dateDimension, platformDimension, kpiDimension);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsCommonDimension that = (StatsCommonDimension) o;
        return Objects.equals(dateDimension, that.dateDimension) &&
                Objects.equals(platformDimension, that.platformDimension) &&
                Objects.equals(kpiDimension, that.kpiDimension);
    }

    @Override
    public int hashCode() {

        return Objects.hash(dateDimension, platformDimension, kpiDimension);
    }

    public DateDimension getDateDimension() {
        return dateDimension;
    }

    public void setDateDimension(DateDimension dateDimension) {
        this.dateDimension = dateDimension;
    }

    public PlatformDimension getPlatformDimension() {
        return platformDimension;
    }

    public void setPlatformDimension(PlatformDimension platformDimension) {
        this.platformDimension = platformDimension;
    }

    public KpiDimension getKpiDimension() {
        return kpiDimension;
    }

    public void setKpiDimension(KpiDimension kpiDimension) {
        this.kpiDimension = kpiDimension;
    }
}
