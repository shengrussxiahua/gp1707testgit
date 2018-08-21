package com.qianfeng.util;

import com.qianfeng.common.GlobalConstants;

import java.sql.Connection;

/**
 * 获取和关闭数据库的连接
 */
public class JdbcUtil {
    public static Connection getConn(){
        //静态加载驱动
        static{
            Class.forName((GlobalConstants.DRIVER));
        }



    }
    /**
     * 获取连接
     *
     */
    public static Connection getConn(){

    }
}
