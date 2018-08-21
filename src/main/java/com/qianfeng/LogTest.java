package com.qianfeng;

import com.qianfeng.ip.LogUtil;

import java.util.Map;

public class LogTest {
    public static void main(String[] args) {
        Map<String, String> info = LogUtil.handleLog("192.168.31.1^A1534425338.412^A192.168.31.110^A/1603.JPG?en=e_pv&p_url=http%3A%2F%26&b_rst=1920*1080*/");
        for (Map.Entry<String, String> en : info.entrySet()) {
            System.out.println(en.getKey() + ":" + en.getValue());
        }
    }
}
