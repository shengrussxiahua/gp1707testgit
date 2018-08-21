//package com.qianfeng.ip;
//
//
//import com.qianfeng.common.EventLogConstants;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//import org.jboss.netty.util.internal.ConcurrentHashMap;
//
//import java.util.EventListener;
//import java.util.Map;
//
//
//
//
///**
// * 正行日志解析工具
// */
//public class LogUtil {
//    private static final Logger logger = Logger.getLogger(LogUtil.class);
//
//    /**
//     * 单行日志的解析
//     * @param log
//     * @return
//     */
//    public static Map<String, String> hadnIeLog(String log) {
//        Map<String, String> info = new ConcurrentHashMap<String,String>();
//        if (StringUtils.isNotEmpty(log.trim())){
//            //拆分单行日志
//String[] fields = log.split(EventLogConstants.LOG_SPARTOR);
//if (fields.length == 4){
//    //存储数据到info中
//    info.put(EventLogConstants.EVENT_COLUMN_NAME_IP,fields[0]);
//    info.put(EventLogConstants.EVENT_COLUMN_NAME_SERVER_TIME,fields[1].replaceAll());
//    //处理参数列表
//    handleParms(info,fields[3]);
//    handeIp(info);
//    handleUserAgent(info);
//
//}
//        }
//        return info;
//    }
//
//    private static void handleUserAgent(Map<String, String> info) {
//        if (info.containsKey(EventLogConstants.EVENT_COLUMN_NAME_USERAGENT)){
//            ua = UserAgentUtil
//        }
//    }
//
//    private static void handeIp(Map<String, String> info) {
//
//    }
//
//    private static void handleParms(Map<String, String> info, String field) {
//    }
//
//
//
//}
