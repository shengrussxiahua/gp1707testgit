package com.qianfeng.analystic.mr.service.impl;

import com.qianfeng.analystic.model.dim.base.*;
import com.qianfeng.analystic.mr.service.IDimensionConvert1;
import com.qianfeng.util.JdbcUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class IDimensionConvertImpl2 implements IDimensionConvert1 {
    private static final Logger logger = Logger.getLogger(IDimensionConvertImpl2.class);
    //key维度:value维度对应的id 缓存
    private Map<String, Integer> cache = new LinkedHashMap<String, Integer>() {
        @Override
        //移除最古老的entry
        protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
            return this.size() > 1000;
        }
    };

    /**
     * 获取维度id
     * 0、先查询缓存中是否存在对应维度，如果直接取出返回
     * 1、先用根据维度属性去查询数据库，如果有就返回维度对应的id
     * 2、如果没有，则先插入再返回
     * plaformDimension("website")
     * plaformDimension("website")
     *
     * @param dimension
     * @return
     */
    @Override
    public int getDimensionByValue(BaseDimension dimension) {
        try {
            //生成缓存key
            String cacheKey = buildCacheKey(dimension);
            if (this.cache.containsKey(cacheKey)) {
                return this.cache.get(cacheKey);
            }
            //代码走到这儿，cache缓存中没有，去查询数据库,
            // 如果有返回id,如果没有将先插入再返回维度id
            Connection conn = JdbcUtil.getConn();
            String[] sqls = null;
            if (dimension instanceof PlatformDimension) {
                sqls = buildPlatformSqls(dimension);
            } else if (dimension instanceof KpiDimension) {
                sqls = buildKpiSqls(dimension);
            } else if (dimension instanceof BrowserDimension) {
                sqls = buildBrowserSqls(dimension);
            } else if (dimension instanceof DateDimension) {
                sqls = buildDateSqls(dimension);
            }

            //执行sql
            Connection connection = JdbcUtil.getConn();
            int id = -1;
            synchronized (this) {
                id = this.executeSqls(sqls, dimension, conn);
            }
            //将获得的id添加到缓存
            this.cache.put(cacheKey, id);
            return id;
        } catch (Exception e) {
            logger.warn("获取维度id异常", e);
        }
        throw new RuntimeException("获取维度id运行异常");
    }

    //执行sql
    private int executeSqls(String[] sqls, BaseDimension dimension, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //先查询
            ps = conn.prepareStatement(sqls[0]);
            this.setArgs(dimension, ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            //查询不到，然后插入再取值
            ps = conn.prepareStatement(sqls[1], Statement.RETURN_GENERATED_KEYS);
            this.setArgs(dimension, ps);
            ps.executeUpdate();//返回int ,影响了多少行
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.warn("执行维度sql异常.", e);
        } finally {
            JdbcUtil.Close(conn, ps, rs);
        }
        throw new RuntimeException("查询和插入sql都异常.");
    }

    private void setArgs(BaseDimension dimension, PreparedStatement ps) {

    }

    /**
     * 构建sql
     * 第一个查询id的sql,第二个插入sql
     */
    private String[] buildDateSqls(BaseDimension dimension) {
        String select = "select `id` from `dimension_date` where `year`  = ? and `season` = ? and `month` = ? and `week` = ? and `day` = ? and `calendar` = ? and `type` = ?";
        String insert = "insert into `dimension_date`(`year`,`season`, `month`,`week`,`day`,`calendar`,`type`) values(?,?,?,?,?,?,?)";
        return new String[]{select, insert};
    }

    private String[] buildBrowserSqls(BaseDimension dimension) {
        String select = "select `id` from `dimension_browser` where  `browser_name` = ? and `browser_version` = ?";
        String insert = "insert into `dimension_browser`(`browser_name`,`browser_version`) values(?,?)";
        return new String[]{select, insert};
    }

    private String[] buildKpiSqls(BaseDimension dimension) {
        String select = "select `id` from `dimension_kpi` where `kpi_name` = ?";
        String insert = "insert into `dimension_kpi`(`kpi_name`) values(?)";
        return new String[]{select, insert};
    }

    private String[] buildPlatformSqls(BaseDimension dimension) {
        String select = "select `id` from `dimension_platform` where `platform_name` = ?";
        String insert = "insert into `dimension_platform`(`platform_name`) values(?)";
        return new String[]{select, insert};
    }

    //构建缓存key
    private String buildCacheKey(BaseDimension dimension) {
        StringBuffer sb = new StringBuffer();
        if (dimension instanceof PlatformDimension) {
            sb.append("platform_");
            PlatformDimension platform = (PlatformDimension) dimension;
            sb.append(platform.getPlatformName());
        } else if (dimension instanceof BrowserDimension) {
            sb.append("browser_");
            BrowserDimension browser = (BrowserDimension) dimension;
            sb.append(browser.getBrowserName());
            sb.append(browser.getBrowserVersion());
        } else if (dimension instanceof KpiDimension) {
            sb.append("kpi_");
            KpiDimension kpi = (KpiDimension) dimension;
            sb.append(kpi.getKpiName());
        } else if (dimension instanceof DateDimension) {
            sb.append("date_");
            DateDimension date = (DateDimension) dimension;
            sb.append(date.getYear()).append(date.getSeason())
                    .append(date.getMonth()).append(date.getWeek())
                    .append(date.getDay()).append(date.getType());
        }
//        } else if (dimension instanceof LocationDimension) {
//            sb.append("local_");
//            LocationDimension local = (LocationDimension) dimension;
//            sb.append(local.getCountry());
//            sb.append(local.getProvince());
//            sb.append(local.getCity());
//        } else if (dimension instanceof EventDimension) {
//            sb.append("event_");
//            EventDimension event = (EventDimension) dimension;
//            sb.append(event.getCategory());
//            sb.append(event.getAction());
//        } else if (dimension instanceof PaymentTypeDimension) {
//            sb.append("payment_");
//            PaymentTypeDimension payment = (PaymentTypeDimension) dimension;
//            sb.append(payment.getPaymentType());
//        } else if (dimension instanceof CurrencyTypeDimension) {
//            sb.append("currency_");
//            CurrencyTypeDimension currency = (CurrencyTypeDimension) dimension;
//            sb.append(currency.getCurrencyName());
//        }
        return sb.length() == 0 ? null : sb.toString();
    }


}
