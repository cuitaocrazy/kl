package com.yada.btg.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ltl on 2019/12/11.
 * <p>
 * Description:日志工具类
 */
public class LogUtil {
    public static void trace(String msg) {
        getLogger().trace(msg);
    }

    public static void debug(String msg) {
        getLogger().debug(msg);
    }

    public static void info(String msg) {
        getLogger().info(msg);
    }

    public static void warn(String msg) {
        getLogger().warn(msg);
    }


    public static void error(String msg, Throwable t) {
        getLogger().error(msg, t);
    }

    /**
     * 获取logger
     *
     * @return logger
     */
    private static Logger getLogger() {
        return LoggerFactory.getLogger(findCaller().getClassName());
    }

    /**
     * 获取调用类
     *
     * @return 调用类名
     */
    private static StackTraceElement findCaller() {
        // 获取堆栈信息
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        // 最原始被调用的堆栈信息
        StackTraceElement caller = null;
        // 日志类名称
        String logClassName = LogUtil.class.getName();
        // 循环遍历到日志类标识
        int i = 0;
        for (int len = callStack.length; i < len; i++) {
            if (logClassName.equals(callStack[i].getClassName())) {
                break;
            }
        }
        //取此值作为类名，如有问题再调试
        caller = callStack[i + 3];
        return caller;
    }
}
