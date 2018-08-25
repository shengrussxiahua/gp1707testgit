package com.qianfeng.analystic.mr.service.impl;

import com.qianfeng.analystic.model.dim.base.*;
import com.qianfeng.analystic.mr.service.IDimensionConvert;
import com.qianfeng.util.JdbcUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 根据维度获取维度id的接口实现
 */
public class IDimensionConvertImpl implements IDimensionConvert {
    private Logger logger = Logger.getLogger(IDimensionConvertImpl.class);
    // 缓存  key;维度 value:维度对应的id
    private Map<String, Integer> cache = new LinkedHashMap<String, Integer>() {

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
            return this.size() > 1000;
        }
    };

    /**
     * 获取对应维度的id
     *
     * @param dimension
     * @return
     */
    @Override
    public int getDimensionByValue(BaseDimension dimension) {
        try {
            //生成维度缓存key
            String cacheKey = buildCacheKey(dimension);
            if (this.cache.containsKey(cacheKey)) {
                return this.cache.get(cacheKey);
            }
            //代码走到这里,代表cache中没有对维度
            //去mysql中先查找,如果有则返回id,如果没有先插入再返回维度id
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
            int id = 1;
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


    /**
     * 构建sql
     * 第一个查询id的sql,第二个插入sql
     *
     * @param dimension
     * @return
     */
    private String[] buildBrowserSqls(BaseDimension dimension) {
        String query = "select `id` from `dimension_browser` where  `browser_name` = ? and `browser_version` = ?";
        String insert = "insert into `dimension_browser`(`browser_name`,`browser_version`) values(?,?)";
        return new String[]{query, insert};
    }

    private String[] buildKpiSqls(BaseDimension dimension) {
        String query = "select `id` from `dimension_kpi` where `kpi_name` = ?";
        String insert = "insert into `dimension_kpi`(`kpi_name`) values(?)";
        return new String[]{query, insert};
    }

    private String[] buildPlatformSqls(BaseDimension dimension) {
        String query = "select `id` from `dimension_platform` where `platform_name` = ?";
        String insert = "insert into `dimension_platform`(`platform_name`) values(?)";
        return new String[]{query, insert};
    }

    private String[] buildDateSqls(BaseDimension dimension) {
        String query = "select `id` from `dimension_date` where `year`  = ? and `season` = ? and `month` = ? and `week` = ? and `day` = ? and `calendar` = ? and `type` = ?";
        String insert = "insert into `dimension_date`(`year`,`season`, `month`,`week`,`day`,`calendar`,`type`) values(?,?,?,?,?,?,?)";
        return new String[]{query, insert};
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

        return sb.length() == 0 ? null : sb.toString();

    }

    //执行sql
    private int executeSqls(String[] sqls, BaseDimension dimension, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //先查询
        try {
            ps = conn.prepareStatement(sqls[0]);
            this.setArgs(dimension, ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            //代码走到这,代表没有查询到对应的id,则插入并查询
            ps = conn.prepareStatement(sqls[1], Statement.RETURN_GENERATED_KEYS);
            this.setArgs(dimension, ps);
            ps.executeUpdate();//返回int ,影响了多少行
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            logger.warn("执行维度sql异常.", e);
        } finally {
            JdbcUtil.Close(conn, ps, rs);
        }

        throw new RuntimeException("查询和插入sql都异常");
    }

    /**
     * 设置参数
     *
     * @param dimension
     * @param ps
     */


    private void setArgs(BaseDimension dimension, PreparedStatement ps) {
        int i = 0;
        try {
            if (dimension instanceof PlatformDimension) {
                PlatformDimension platform = (PlatformDimension) dimension;
                ps.setString(++i, platform.getPlatformName());
            } else if (dimension instanceof BrowserDimension) {
                BrowserDimension browser = (BrowserDimension) dimension;
                ps.setString(++i, browser.getBrowserName());
                ps.setString(++i, browser.getBrowserVersion());
            } else if (dimension instanceof KpiDimension) {
                KpiDimension kpi = (KpiDimension) dimension;
                ps.setString(++i, kpi.getKpiName());
            } else if (dimension instanceof DateDimension) {
                DateDimension date = (DateDimension) dimension;
                ps.setInt(++i, date.getYear());
                ps.setInt(++i, date.getSeason());
                ps.setInt(++i, date.getMonth());
                ps.setInt(++i, date.getWeek());
                ps.setInt(++i, date.getDay());
                ps.setDate(++i, new java.sql.Date(date.getCalendar().getTime()));//date包不同,需要转化成sql包
                ps.setString(++i, date.getType());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
