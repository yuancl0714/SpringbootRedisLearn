package com.cl.interview.javabasic.juc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenLei
 * @date 2020/7/2 15:43
 */
public class RunAndStart {
    public static void main(String[] args) {
        String[] strings = getDateForString("01-APR-2020 00:00:32*(CONNECT_DATA=(CID=(PROGRAM=JDBC Thin Client)(HOST=_jdbc_)(USER" +
                "=wasup))(SERVICE_NAME=bamc))*(ADDRESS=(PROTOCOL=tcp)(HOST=84.33.94.27)(POST=59555))*establish*bamc*0");
        for (String string : strings) {
            System.out.println(string);
        }
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap = getIpTimes(strings[1], hashMap);
        System.out.println(hashMap.get(strings[1]));
    }

    public static String[] getDateForString(String str) {
        String[] strings = new String[2];
        strings[0] = str.substring(0, 17);
        int index = str.lastIndexOf("HOST=");
        strings[1] = str.substring(index + 5, index + 16);
        return strings;
    }

    public static Map<String, Integer> getIpTimes(String ip, Map<String, Integer> map) {
        if (map.get(ip) == null) {
            map.put(ip, 1);
        } else {
            map.replace(ip, map.get(ip) + 1);
        }
        return map;
    }

}
