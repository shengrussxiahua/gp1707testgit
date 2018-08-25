package com.qianfeng.util;

import com.qianfeng.common.GlobalConstants;

import java.sql.*;

/**
 * 获取和关闭数据库的连接
 */
public class JdbcUtil {

    //静态加载驱动
    static {
        try {
            Class.forName((GlobalConstants.DRIVER));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取mysql连接
     */
    public static Connection getConn() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(GlobalConstants.URL, GlobalConstants.USERNAME, GlobalConstants.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;

    }

    /**
     * 关闭相关对象
     * 关闭连接
     */
    public static void Close(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                //do nothing
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                //do nothing
            }
        }
        if (rs != null) {

            try {
                rs.close();
            } catch (SQLException e) {
                //do nothing
            }


        }
    }

    public static void main(String[] args) {

        System.out.println(getConn());
    }
}
