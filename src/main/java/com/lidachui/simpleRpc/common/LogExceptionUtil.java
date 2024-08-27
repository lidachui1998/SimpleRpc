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
        if (ex == null) {
            return "No exception message provided.";
        }

        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            ex.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            // 捕获并返回异常处理过程中发生的任何错误
            return "Error while getting exception message: " + e.getMessage();
        }
    }
}
