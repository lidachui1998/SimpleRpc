package com.lidachui.simpleRpc.common;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * LogExceptionUtil
 *
 * @author: lihuijie
 * @date: 2024/8/26 16:38
 * @version: 1.0
 */
public class LogExceptionUtil {

    /**
     * 获取异常消息
     *
     * @param ex 前任
     * @return {@code String }
     */
    public static String getExceptionMessage(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        pw.flush();
        pw.close();
        return sw.toString();
    }
}
