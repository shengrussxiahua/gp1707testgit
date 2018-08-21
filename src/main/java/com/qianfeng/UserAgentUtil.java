package com.qianfeng;


import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;


/**
 * 解析useragent代理对象
 */
public class UserAgentUtil {
    //获取日志的打印对象
    private static final Logger logger = Logger.getLogger(UserAgentUtil.class);
    private static UASparser ua = null;

    //初始化
    static {
        try {
            ua = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析浏览器代理对象
     *
     * @param agent
     * @return
     */
    public static UserAgentInfo parserUserAgent(String agent) {
        UserAgentInfo uainfo = new UserAgentInfo();
        if (StringUtils.isEmpty(agent)) {
            logger.warn("agent is null.but we need not null");
        }
        //正常解析
        try {
            cz.mallat.uasparser.UserAgentInfo info = ua.parse(agent);
            //设置属性
            uainfo.setBrowserName(info.getUaFamily());
            uainfo.setBrowserVersion(info.getBrowserVersionInfo());
            uainfo.setOsName(info.getOsName());
            uainfo.setOsVersion(info.getOsFamily());
        } catch (IOException e) {
            logger.warn("useragetn parse 异常.", e);
        }

        return null;
    }


    /**
     * 用于封装解析出来的字段,浏览器名,版本,操作系统,版本
     */
    public static class UserAgentInfo {
        private String browserName;
        private String browserVersion;
        private String osName;
        private String osVersion;

        public String getBrowserName() {
            return browserName;
        }

        public void setBrowserName(String browserName) {
            this.browserName = browserName;
        }

        public String getBrowserVersion() {
            return browserVersion;
        }

        public void setBrowserVersion(String browserVersion) {
            this.browserVersion = browserVersion;
        }

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        @Override
        public String toString() {
            return "UserAgentInfo{" +
                    "browserName='" + browserName + '\'' +
                    ", browserVersion='" + browserVersion + '\'' +
                    ", osName='" + osName + '\'' +
                    ", osVersion='" + osVersion + '\'' +
                    '}';
        }
    }

}
