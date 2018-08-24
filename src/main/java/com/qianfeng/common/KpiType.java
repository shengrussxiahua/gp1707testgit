package com.qianfeng.common;

/**
 * kpi的枚举  (kpi,例如,新增用户,新增总用户,活跃用户,指标)
 */
public enum KpiType {
    NEW_USER("new install user"),

    BROWSER_NEW_USER("browser_new_user");

    public String kpiName; //属性

    KpiType(String kpiName) {

        this.kpiName = kpiName;
    }

    public static KpiType valueOfKpiName(String name) {
        for (KpiType kpi : values()) {
            if (name.equals(kpi.kpiName)) {
                return kpi;
            }
        }
        return null;
    }
}
