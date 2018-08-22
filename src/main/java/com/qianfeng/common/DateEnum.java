package com.qianfeng.common;

/**
 * 时间枚举
 *
 * @author zjj
 */
public enum DateEnum {
    /**
     *
     */
    YEAR("year"),
    /**
     *
     */
    SEASON("season"),
    /**
     *
     */
    MONTH("month"),
    /**
     *
     */
    WEEK("week"),
    /**
     *
     */
    DAY("day"),
    /**
     *
     */
    HOUR("hour");

    public String dateType;

    DateEnum(String dateType) {
        this.dateType = dateType;
    }

    public static DateEnum valueOfType(String type) {
        for (DateEnum dateEnum : values()) {
            if (type.equals(dateEnum.dateType)) {
                return dateEnum;
            }

        }
        return null;
    }
}
