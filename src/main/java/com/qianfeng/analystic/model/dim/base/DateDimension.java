package com.qianfeng.analystic.model.dim.base;


import com.qianfeng.common.DateEnum;
import com.qianfeng.util.TimeUtil;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author zhoujiaojiao
 */
public class DateDimension extends BaseDimension {
    private int id;
    private int year;
    private int season;
    private int month;
    private int week;
    private int day;
    private Date calendar = new Date();// 日期
    private String type;//指标类型 例如:天指标

    public DateDimension() {
    }

    public DateDimension(Integer year) {
        this.year = year;
    }

    public DateDimension(Integer year, Integer season) {
        this(year);
        this.season = season;
    }

    public DateDimension(Integer year, Integer season, Integer month) {
        this(year, season);
        this.month = month;
    }

    public DateDimension(Integer year, Integer season, Integer month, Integer week) {
        this(year, season, month);
        this.week = week;
    }

    public DateDimension(Integer year, Integer season, Integer month, Integer week, Integer day) {
        this(year, season, month, week);
        this.day = day;
    }

    public DateDimension(Integer year, Integer season, Integer month, Integer week, Integer day, Date calendar) {
        this(year, season, month, week, day);
        this.calendar = calendar;
    }

    public DateDimension(Integer year, Integer season, Integer month, Integer week, Integer day, Date calendar, String type) {
        this(year, season, month, week, day, calendar);
        this.type = type;
    }

    public DateDimension(Integer id, Integer year, Integer season, Integer month, Integer week, Integer day, Date calendar, String type) {
        this(year, season, month, week, day, calendar, type);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Date getCalendar() {
        return calendar;
    }

    public void setCalendar(Date calendar) {
        this.calendar = calendar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    @Override
    public int compareTo(BaseDimension o) {
        if (this == o) {
            return 0;
        }
        DateDimension other = (DateDimension) o;

        int tmp = this.id - other.id;
        if (tmp != 0) {
            return tmp;
        }

        tmp = this.year - other.year;
        if (tmp != 0) {
            return tmp;
        }

        tmp = this.season - other.season;
        if (tmp != 0) {
            return tmp;
        }

        tmp = this.month - other.month;
        if (tmp != 0) {
            return tmp;
        }

        tmp = this.week - other.week;
        if (tmp != 0) {
            return tmp;
        }

        tmp = this.day - other.week;
        if (tmp != 0) {
            return tmp;
        }

        tmp = this.calendar.compareTo(other.calendar);
        if (tmp != 0) {
            return tmp;
        }
        tmp = this.day - other.day;
        if (tmp != 0) {
            return tmp;
        }

        // TODO: 2018/8/20 compareTo
        tmp = this.type.compareTo(other.type);
        return tmp;

    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.id);
        out.writeInt(this.year);
        out.writeInt(this.season);
        out.writeInt(this.month);
        out.writeInt(this.week);
        out.writeInt(this.day);
        // TODO: 2018/8/19 如何设置write calendar？
//        date类型写成long
        out.writeLong(this.calendar.getTime());//date写成long
        out.writeUTF(this.type);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.year = in.readInt();
        this.season = in.readInt();
        this.month = in.readInt();
        this.week = in.readInt();
        this.day = in.readInt();
        this.calendar.setTime(in.readLong());
        this.type = in.readUTF();

    }

    @Override
    public String toString() {
        return "DateDimension{" +
                "id=" + id +
                ", year=" + year +
                ", season=" + season +
                ", month=" + month +
                ", week=" + week +
                ", day=" + day +
                ", calendar=" + calendar +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateDimension that = (DateDimension) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(year, that.year) &&
                Objects.equals(season, that.season) &&
                Objects.equals(month, that.month) &&
                Objects.equals(week, that.week) &&
                Objects.equals(day, that.day) &&
                Objects.equals(calendar, that.calendar) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, year, season, month, week, day, calendar, type);
    }

    /**
     * 根据时间戳和指标类型来获得时间维度对象
     * 在某个时间维度上得到记录
     *
     * @param time
     * @param type
     * @return
     */
    public static DateDimension buildDate(long time, DateEnum type) {
        Calendar calendar = Calendar.getInstance();
        // 清空
        calendar.clear();

        // 获取年份
        int year = TimeUtil.getDateInfo(time, DateEnum.YEAR);
        // 获取年度指标的dateDimension对象，
        // 年指标，截止日期为当年的1月1号
        if (type.equals(DateEnum.YEAR)) {
            calendar.set(year, Calendar.JANUARY, 1);
            return new DateDimension(year, 0, 0, 0, 1, calendar.getTime(), type.dateType);
        }

        int season = TimeUtil.getDateInfo(time, DateEnum.SEASON);
        // 季度指标，截止日期为当前季度的第一个月第一天
        if (type.equals(DateEnum.SEASON)) {
            int month = (season * 3 - 2);
            calendar.set(year, month - 1, 1);
            return new DateDimension(year, season, month, 0, 1, calendar.getTime(), type.dateType);
        }

        int month = TimeUtil.getDateInfo(time, DateEnum.MONTH);
        // 月指标，截止日期为当月的1号
        if (type.equals(DateEnum.MONTH)) {
            calendar.set(year, month - 1, 1);
            return new DateDimension(year, season, month, 0, 1, calendar.getTime(), type.dateType);
        }

        int week = TimeUtil.getDateInfo(time, DateEnum.WEEK);
        // 周指标，截止日期为当周的第一天的0时0分0秒
        if (type.equals(DateEnum.WEEK)) {
            long firstDayOfWeek = TimeUtil.getFirstDayOfWeek(time);
            year = TimeUtil.getDateInfo(firstDayOfWeek, DateEnum.YEAR);
            season = TimeUtil.getDateInfo(firstDayOfWeek, DateEnum.SEASON);
            month = TimeUtil.getDateInfo(firstDayOfWeek, DateEnum.MONTH);

            return new DateDimension(year, season, month, week, 0, new Date(firstDayOfWeek), type.dateType);
        }

        int day = TimeUtil.getDateInfo(time, DateEnum.WEEK);
        // 天指标，截止日期为当周的第一天的0时0分0秒
        if (type.equals(DateEnum.DAY)) {
            calendar.set(year, month - 1, day);
            return new DateDimension(year, season, month, week, day, calendar.getTime(), type.dateType);
        }

        throw new RuntimeException("暂不支持时间枚举类型的时候获取。type：" + type.dateType);

    }

}